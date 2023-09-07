package com.lambda.sample.service;

import com.lambda.sample.beans.TempBean;
import com.lambda.sample.executor.ExecutorUtil;
import com.lambda.sample.executor.task.WriteToCsvTask;
import com.lambda.sample.jdbc.repository.TestRepository;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
//@Component
public class FileServiceThreaded {
    @Autowired
    private TestRepository testRepo;

    @Autowired
    ExecutorUtil executorUtil;

    @Value("${smallcase.fileName}")
    String fileName;

    public void createFile() {

        try {
            List<Future<Integer>> futures = new ArrayList<>();
            FileWriter writer = new FileWriter("/tmp/" + fileName);

            ColumnPositionMappingStrategy<TempBean> mappingStrategy = new ColumnPositionMappingStrategy<>();

            String[] columns = {"id", "first_name", "last_name", "email", "mobile_no"};

            log.info(" columns {}", Arrays.toString(columns));
            mappingStrategy.setColumnMapping(columns);
            mappingStrategy.setType(TempBean.class);

            // Creating StatefulBeanToCsv object
            StatefulBeanToCsvBuilder<TempBean> builder =
                    new StatefulBeanToCsvBuilder<TempBean>(writer);

            StatefulBeanToCsv<TempBean> beanWriter =
                    builder.withMappingStrategy(mappingStrategy).build();


            int totSize = testRepo.findCountOfAllUsers("ganesh");
            log.info(" totSize {}",totSize);
            int pageSize = 2000000;
            int loopSize = totSize / pageSize + 1;

            log.info(" loop size {}",loopSize);

            for (int i = 0; i < loopSize; i++) {
                Pageable pageable = PageRequest.of(i, pageSize);
                log.info(" pulling page {}",i);
                List<TempBean> records = testRepo.findAllUsersByCol2("ganesh", pageable);

                log.info(" record count {}", records.size());

                // Write list to StatefulBeanToCsv object
                WriteToCsvTask task = new WriteToCsvTask(records,beanWriter);
                futures.add(executorUtil.checkAndSubmit(task));

            }

            //await completion
            for(Future<Integer> future:futures)
                future.wait();

            // closing the writer object
            writer.close();

        } catch (Exception e) {
            log.error(" Exception whle writing file {} stacktrace {}", e.getMessage(), e.getStackTrace());
        }
    }

}
