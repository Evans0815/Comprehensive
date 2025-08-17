package com.evans.springbatch.config;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
@Configuration
public class BatchInMemoryConfig {

    // 自定义批处理配置器，使用内存存储元数据
    @Bean
    public BatchConfigurer batchConfigurer() {
        return new DefaultBatchConfigurer() {
            @Override
            protected JobRepository createJobRepository() throws Exception {
                MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
                // 关联内存事务管理器
                factory.setTransactionManager(inMemoryTransactionManager());
                factory.afterPropertiesSet();
                return factory.getObject();
            }

            // 忽略数据源设置，不将元数据存储到数据库
            @Override
            public void setDataSource(DataSource dataSource) {
                // 有意留空，避免使用业务数据源存储批处理元数据
            }
        };
    }

    /*
     * 重命名事务管理器，避免与默认的 transactionManager 冲突
     * 配置无资源的事务管理器（内存模式专用）
     */
    @Bean(name = "inMemoryTransactionManager")
    public PlatformTransactionManager inMemoryTransactionManager() {
        return new ResourcelessTransactionManager(); // 内存模式常用的事务管理器
    }

}
