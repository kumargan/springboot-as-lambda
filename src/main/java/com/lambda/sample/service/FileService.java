package com.lambda.sample.service;

import com.lambda.sample.beans.TempBean;
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
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class FileService {
    @Autowired
    private TestRepository testRepo;

    @Value("${smallcase.fileName}")
    String fileName;

    public void createFile() {

        try {
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
            log.debug(" totSize {}",totSize);
            int pageSize = 2000000;
            int loopSize = totSize / pageSize + 1;

            log.debug(" loop size {}",loopSize);

            for (int i = 0; i < loopSize; i++) {
                Pageable pageable = PageRequest.of(i, pageSize);
                List<TempBean> records = testRepo.findAllUsersByCol2("ganesh", pageable);

                log.info(" pulling page {} record count {}",i, records.size());

                // Write list to StatefulBeanToCsv object
                beanWriter.write(records);

            }

            // closing the writer object
            writer.close();

        } catch (Exception e) {
            log.error(" Exception whle writing file {} stacktrace {}", e.getMessage(), e.getStackTrace());
        }
    }

}
