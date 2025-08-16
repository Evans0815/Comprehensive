package com.evans.springbatch.common;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
@Data
public class Transaction {

    private Long transId;
    private String orderNo;
    private BigDecimal amount;
    private Date transTime;

}
