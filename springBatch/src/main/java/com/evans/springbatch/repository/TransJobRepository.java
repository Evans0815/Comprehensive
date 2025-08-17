package com.evans.springbatch.repository;

import com.evans.springbatch.common.OriginTransaction;
import com.evans.springbatch.common.Transaction;
import com.evans.springbatch.job.JobListenerStep;
import com.evans.springbatch.listener.TransJobExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
@Component
public class TransJobRepository implements BatchJobRepository {

    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private BatchStepRepository batchStepRepository;
    @Resource
    private TransJobExecutionListener transJobExecutionListener;

    @Override
    public Job getBatchJob() {
        JobListenerStep<OriginTransaction, Transaction> step = (JobListenerStep) batchStepRepository.getBatchStep();
        return jobBuilderFactory.get("TransSyncJob")
                .listener(transJobExecutionListener)
                .start(step.getSteps()[0])
                .build();
    }

}
