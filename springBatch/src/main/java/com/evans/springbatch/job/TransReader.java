package com.evans.springbatch.job;

import com.evans.springbatch.common.OriginTransaction;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Evans
 * @Date: 2025-08-16
 */
public class TransReader implements ItemReader<OriginTransaction>, StepExecutionListener {

    private final int chunkSize;
    private final CompletableFuture<List<OriginTransaction>> transListFuture;
    private ItemReader<OriginTransaction> delegate; // 委托的ListItemReader
    private StepExecution stepExecution;
    private final long timeout;

    public TransReader(CompletableFuture<List<OriginTransaction>> transListFuture, int chunkSize, int timeout) {
        this.transListFuture = transListFuture;
        this.chunkSize = chunkSize;
        this.timeout = timeout;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }

    @Override
    public OriginTransaction read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (stepExecution == null) {
            throw new IllegalStateException("StepExecution 未注入，需确保 TransReader 配置正确");
        }

        if (delegate == null) {
            try {
                // 等待异步结果返回
                List<OriginTransaction> transList = transListFuture.get(timeout, TimeUnit.SECONDS);

                if (transList == null || transList.isEmpty()) {
                    stepExecution.setStatus(BatchStatus.FAILED);
                    throw new JobExecutionException("异步查询返回空结果，终止批处理");
                }

//                delegate = new JdbcPagingItemReader<>(transList);
            } catch (ExecutionException e) {
                stepExecution.setStatus(BatchStatus.FAILED);
                stepExecution.getExecutionContext().putString("errorMessage", "异步查询失败: " + e.getCause().getMessage());
                throw new JobExecutionException("异步查询执行失败，终止批处理", e);
            } catch (TimeoutException e) {
                // 处理超时
                stepExecution.setStatus(BatchStatus.FAILED);
                throw new JobExecutionException(String.format("异步查询超时（%d %s），终止批处理", timeout, TimeUnit.SECONDS), e);
            } catch (InterruptedException e) {
                // 处理中断
                Thread.currentThread().interrupt();
                stepExecution.setStatus(BatchStatus.FAILED);
                throw new JobExecutionException("异步查询被中断，终止批处理", e);
            }
        }

        // 调用委托Reader的read方法
        return delegate.read();
    }

}
