package com.evans.springbatch.convert;

import com.evans.springbatch.common.OriginTransaction;
import com.evans.springbatch.common.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
@Mapper
public interface TransConvert {

    TransConvert INSTANCES =  Mappers.getMapper(TransConvert.class);

    Transaction toTransaction(OriginTransaction originTrans);

}
