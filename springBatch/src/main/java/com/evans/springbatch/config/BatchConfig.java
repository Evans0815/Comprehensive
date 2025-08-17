package com.evans.springbatch.config;

import com.alibaba.fastjson.JSONObject;
import com.evans.springbatch.common.OriginTransaction;
import com.evans.springbatch.mapper.OriginTransactionMapper;
import com.evans.springbatch.mapper.TransactionMapper;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import com.evans.springbatch.job.TransProcessor;
import com.evans.springbatch.job.TransWriter;
import com.evans.springbatch.repository.BatchJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Value("${spring.batch.chunk-size}")
    private int chunkSize;
    @Resource
    private DataSource dataSource;
    @Resource
    private JobLauncher jobLauncher;
    @Resource
    private BatchJobRepository batchJobRepository;
    @Resource
    private TransactionMapper transactionMapper;
    @Resource
    private OriginTransactionMapper originTransactionMapper;

    public void starter(String[] args) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
            JobParametersInvalidException, JobRestartException {
        String batchId = UUID.randomUUID().toString();
        log.info("执行批次号:{}", batchId);
        JobParametersBuilder jobParamsBuilder = new JobParametersBuilder();
        jobParamsBuilder.addString("batchId", batchId);

        JSONObject json = new JSONObject();
        json.put("batchId", batchId);
        jobParamsBuilder.addParameter("jobParameter", new JobParameter(json.toJSONString()));

        jobLauncher.run(batchJobRepository.getBatchJob(), jobParamsBuilder.toJobParameters());
    }

    /**
     * 异步查询数据库的ItemReader，查询失败时主动终止批处理
     */
    @Bean
    @StepScope
    public JdbcPagingItemReader<OriginTransaction> transReader() {
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT *");
        queryProvider.setFromClause("FROM origin_transaction");
        // 动态条件 create_time = :createTime
        queryProvider.setWhereClause("WHERE sync_flag = 0");

        // 设置排序字段（必须，分页查询需要排序字段）
        Map<String, Order> sortKeys = new HashMap<>();
        // 按trans_id升序排序，确保分页一致性
        sortKeys.put("trans_id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        JdbcPagingItemReader<OriginTransaction> reader = getOriginTransJdbcPagingItemReader(queryProvider);

        // 设置参数，和WHERE条件中的:createTime对应
//        Map<String, Object> params = new HashMap<>();
//        params.put("createTime", createTime);
//        reader.setParameterValues(params);

        try {
            reader.afterPropertiesSet();
        } catch (Exception e) {
            // 查询失败时，主动抛出异常终止批处理
            String errorInfo = "初始化JdbcPagingItemReader失败";
            log.error(errorInfo);
            throw new RuntimeException(errorInfo, e);
        }

        return reader;
    }

    private JdbcPagingItemReader<OriginTransaction> getOriginTransJdbcPagingItemReader(MySqlPagingQueryProvider queryProvider) {
        RowMapper<OriginTransaction> rowMapper = (rs, rowNum) -> {
            OriginTransaction originTrans = new OriginTransaction();
            originTrans.setTransId(rs.getLong("trans_id"));
            originTrans.setOrderNo(rs.getString("order_no"));
            originTrans.setAmount(rs.getBigDecimal("amount"));
            originTrans.setTransTime(rs.getTimestamp("trans_time"));
            originTrans.setSyncFlag(rs.getInt("sync_flag"));
            return originTrans;
        };

        JdbcPagingItemReader<OriginTransaction> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setQueryProvider(queryProvider);
        reader.setRowMapper(rowMapper);
        return reader;
    }

    @Bean
    @StepScope
    public TransProcessor transProcessor() {
        return new TransProcessor();
    }

    @Bean
    @StepScope
    public TransWriter TransWriter() {
        return new TransWriter(chunkSize, transactionMapper, originTransactionMapper);
    }

}
