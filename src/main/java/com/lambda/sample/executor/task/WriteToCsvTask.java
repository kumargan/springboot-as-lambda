package com.lambda.sample.executor.task;

import com.lambda.sample.beans.TempBean;
import com.opencsv.bean.StatefulBeanToCsv;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class WriteToCsvTask implements Callable<Integer> {

    private StatefulBeanToCsv<TempBean> beanWriter = null;
    List<TempBean> records = null;

    public WriteToCsvTask(List<TempBean> records, StatefulBeanToCsv<TempBean> beanWriter) {
        this.records = records;
        this.beanWriter = beanWriter;
    }

    @Override
    public Integer call() {

        try {
            beanWriter.write(records);
        } catch (Exception e1) {
            log.error(" Exception in task, fix the code {} stack trace {} ", e1.getMessage(), e1.getStackTrace());
        }
        return 1;
    }

}
