package com.evans.springbatch.mapper;

import com.evans.springbatch.common.Transaction;

import java.util.List;

/**
 * @Author: Evans
 * @Date: 2025-08-17
 */
public interface TransactionMapper {

    int batchInsert(List<? extends Transaction> list);

}
