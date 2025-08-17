package com.evans.springbatch.job;

import com.evans.springbatch.common.OriginTransaction;
import com.evans.springbatch.common.Transaction;
import com.evans.springbatch.convert.TransConvert;
import org.springframework.batch.item.ItemProcessor;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
public class TransProcessor implements ItemProcessor<OriginTransaction, Transaction> {
    @Override
    public Transaction process(final OriginTransaction originTrans) throws Exception {
        return TransConvert.INSTANCES.toTransaction(originTrans);
    }

}
