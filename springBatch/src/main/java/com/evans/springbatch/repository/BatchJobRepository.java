package com.evans.springbatch.repository;

import org.springframework.batch.core.Job;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
public interface BatchJobRepository {

    Job getBatchJob();

}
