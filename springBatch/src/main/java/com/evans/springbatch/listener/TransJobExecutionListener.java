package com.evans.springbatch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
@Slf4j
@Component
public class TransJobExecutionListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("before job");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("after job");
        try {
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger errorCount = new AtomicInteger(0);

            jobExecution.getStepExecutions().forEach(stepExecution -> {
                successCount.addAndGet(stepExecution.getWriteCount());
                errorCount.addAndGet(stepExecution.getSkipCount());
            });

            if (errorCount.get() > 0) {
                log.error("发送通知:执行存在异常。errorCount:{}", errorCount.get());
            }
            else {
                if (successCount.get() > 0) {
                    log.info("发送通知:执行成功。successCount:{}", successCount.get());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
