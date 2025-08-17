package com.evans.springbatch.repository;

import org.springframework.batch.core.Step;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
public interface BatchStepRepository {

    Step getBatchStep();

}
