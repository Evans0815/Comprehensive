package com.evans.springbatch;

import com.evans.springbatch.common.OriginTransaction;
import com.evans.springbatch.mapper.OriginTransactionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
class SpringBatchApplicationTests {

    @Resource
    private OriginTransactionMapper transactionMapper;

    @Test
    void contextLoads() {

    }

    @Test
    void batchInsert() {
        int c = 3;
        Random random = new Random();
        List<OriginTransaction> list = new ArrayList<>();
        for (int i = 0; i < c; i++) {
            OriginTransaction originTrans = new OriginTransaction();
            originTrans.setOrderNo(UUID.randomUUID().toString().replace("-",""));
            originTrans.setTransTime(new Date());
            originTrans.setAmount(BigDecimal.valueOf(random.nextInt(100) + 1));
            list.add(originTrans);
        }
        transactionMapper.batchInsert(list);
    }

}
