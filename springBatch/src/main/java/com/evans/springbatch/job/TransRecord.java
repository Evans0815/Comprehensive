package com.evans.springbatch.job;

import com.evans.springbatch.common.TransVO;
import com.evans.springbatch.listener.ListerRecord;
import lombok.Data;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
@Data
public class TransRecord implements ListerRecord {

    private TransVO transVO;

    @Override
    public String describe() {
        return "";
    }
}
