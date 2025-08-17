package com.evans.springbatch;

import com.evans.springbatch.config.BatchConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@SpringBootApplication
@MapperScan("com.evans.springbatch.mapper")
public class SpringBatchApplication {

    @Resource
    private BatchConfig batchConfig;

    private static String[] ARGS;

    public static void main(String[] args) {
        ARGS = args;
        ConfigurableApplicationContext context = SpringApplication.run(SpringBatchApplication.class, args);
        // 业务执行完毕，关闭上下文
        context.close();
    }

    @Bean
    public void batch() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
            JobParametersInvalidException, JobRestartException {
        log.info("args:{}", Arrays.toString(ARGS));
        batchConfig.starter(ARGS);
    }

}
