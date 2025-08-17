package com.evans.springbatch.job;

import com.evans.springbatch.common.Transaction;
import com.evans.springbatch.mapper.OriginTransactionMapper;
import com.evans.springbatch.mapper.TransactionMapper;
import com.evans.springbatch.util.BatchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
@Slf4j
public class TransWriter implements ItemWriter<Transaction> {

    private final int chunkSize;
    private final TransactionMapper transactionMapper;
    private final OriginTransactionMapper originTransactionMapper;
    public TransWriter(int chunkSize, final TransactionMapper transactionMapper, final OriginTransactionMapper originTransactionMapper) {
        this.chunkSize = chunkSize;
        this.transactionMapper = transactionMapper;
        this.originTransactionMapper = originTransactionMapper;
    }


    @Override
    public void write(List<? extends Transaction> list) throws Exception {
        if (list.isEmpty()) {
            log.info("没有需要写入的交易数据");
            return;
        }

        BatchUtil.batchProcess(list, chunkSize, partList -> {
            int i = transactionMapper.batchInsert(partList);
            originTransactionMapper.updateSyncFlag(partList, i > 0 ? 1 : 2);
        });

        log.info("成功写入 {} 条交易数据到数据库", list.size());
    }
}
