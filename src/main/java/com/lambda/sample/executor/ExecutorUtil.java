package com.lambda.sample.executor;

import com.lambda.sample.executor.task.WriteToCsvTask;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Slf4j
@Component
public class ExecutorUtil {

    private ThreadPoolExecutor executor;
    private Integer queuelength = 5;

    @Value("${csv.writer.threads}")
    private Integer publisherThreadCount;

    @PostConstruct
    public void init(){
        log.info(" publisher threads created {}",publisherThreadCount);
        RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                log.error(" Rejected request by threadpool, please fix code. this should not be thrown");
            }
        };

        ThreadFactory factory = Executors.defaultThreadFactory();
        executor = new ThreadPoolExecutor(
                publisherThreadCount,
                publisherThreadCount,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queuelength),
                factory,
                rejectedExecutionHandler
        );
    }

    public synchronized Future<Integer> checkAndSubmit(WriteToCsvTask task){
        Future<Integer> future = null;
        if(executor.getQueue().size() < queuelength){
            future = executor.submit(task);
        }
        return future;
    }

}
