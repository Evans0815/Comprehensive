package com.evans.springbatch.repository;

import com.evans.springbatch.common.OriginTransaction;
import com.evans.springbatch.common.Transaction;
import com.evans.springbatch.job.*;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
@Component
public class TransStepRepository implements BatchStepRepository {

    @Value("${spring.batch.chunk-size}")
    private int chunkSize;
    @Resource
    private ItemReader<OriginTransaction> transReader;
    @Resource
    private ItemProcessor<OriginTransaction, Transaction> transProcessor;
    @Resource
    private ItemWriter<Transaction> transWriter;
    @Resource
    private StepBuilderFactory stepBuilderFactory;
    @Resource
    private ThreadPoolTaskExecutor batchTaskExecutor;

    @Override
    public Step getBatchStep() {

        TaskletStep transSyncStep = stepBuilderFactory.get("transSyncStep")
                .<OriginTransaction, Transaction>chunk(chunkSize)
                .reader(transReader)
                .processor(transProcessor)
                .writer(transWriter)
                .faultTolerant()
//                .skip(Throwable.class)
//                .skipLimit(999999)
                .taskExecutor(batchTaskExecutor)
                .throttleLimit(batchTaskExecutor.getCorePoolSize())
                .build();


        return new JobListenerStep<>(transSyncStep);
    }

}
