package com.evans.springbatch.util;

import java.util.List;
import java.util.function.Consumer;

/**
 * @Author: Evans
 * @Date: 2025-08-17
 */
public class BatchUtil {

    /**
     * 分批处理列表
     */
    public static <T> void batchProcess(List<T> list, int batchSize, Consumer<List<T>> processor) {
        if (list == null || list.isEmpty()) return;
        int totalSize = list.size();
        int batches = (totalSize + batchSize - 1) / batchSize;

        for (int i = 0; i < batches; i++) {
            int start = i * batchSize;
            int end = Math.min((i + 1) * batchSize, totalSize);
            processor.accept(list.subList(start, end));
        }
    }

}
