package com.evans.springbatch.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: Evans
 * @Date: 2025-08-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OriginTransaction extends Transaction {

    /**
     * 同步标志
     * 0-未同步
     * 1-同步成功
     * 2-同步失败
     */
    private int syncFlag;

    @Override
    public String toString() {
        return "OriginTransaction{" +
                super.toString() +
                "syncFlag=" + syncFlag +
                '}';
    }
}
