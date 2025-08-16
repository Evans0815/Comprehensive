package com.evans.springbatch.job;

import lombok.Getter;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
@Getter
public class JobListenerStep<T, R> implements Step {

    private final Step[] steps;

    public JobListenerStep(Step... steps) {
        this.steps = steps;
    }

    @Override
    public String getName() {
        return "JobListenerStep";
    }

    @Override
    public boolean isAllowStartIfComplete() {
        return false;
    }

    @Override
    public int getStartLimit() {
        return 0;
    }

    @Override
    public void execute(StepExecution stepExecution) throws JobInterruptedException {

    }

}
