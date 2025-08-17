package com.evans.springbatch.job;

import com.evans.springbatch.common.Transaction;
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
    public TransWriter(int chunkSize, final TransactionMapper transactionMapper) {
        this.chunkSize = chunkSize;
        this.transactionMapper = transactionMapper;
    }


    @Override
    public void write(List<? extends Transaction> list) throws Exception {
        log.info("data:{}", list);

        if (list.isEmpty()) {
            log.info("没有需要写入的交易数据");
            return;
        }

        BatchUtil.batchProcess(list, chunkSize, transactionMapper::batchInsert);

        log.info("成功写入 {} 条交易数据到数据库", list.size());
    }
}
