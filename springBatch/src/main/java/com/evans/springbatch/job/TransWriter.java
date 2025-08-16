package com.evans.springbatch.job;

import com.evans.springbatch.common.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
@Slf4j
public class TransWriter implements ItemWriter<Transaction> {
    @Override
    public void write(List<? extends Transaction> list) throws Exception {
        log.info("data:{}", list);
    }
}
