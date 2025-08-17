package com.evans.springbatch.mapper;

import com.evans.springbatch.common.OriginTransaction;
import com.evans.springbatch.common.Transaction;

import java.util.List;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
public interface OriginTransactionMapper {

    List<OriginTransaction> queryTrans();

    int batchInsert(List<OriginTransaction> list);

    int updateSyncFlag(List<? extends Transaction> list, int syncFlag);

}
