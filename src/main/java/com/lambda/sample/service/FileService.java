package com.lambda.sample.service;

import com.lambda.sample.beans.TempBean;
import com.lambda.sample.constants.Constants;
import com.lambda.sample.jdbc.repository.TestRepository;
import com.lambda.sample.utils.FileUtils;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class FileService {
    @Autowired
    private TestRepository testRepo;

    @Autowired
    S3Service s3Service;

    @Value("${smallcase.fileName}")
    String fileName;

    @Value("${s3.bucket}")
    String bucket;

    public void createFile() {

        try {
            FileUtils.deleteFileIfExist(new File("/tmp/" + fileName));

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
            log.debug(" totSize {}", totSize);
            int pageSize = 2000000;
            int loopSize = totSize / pageSize + 1;

            log.debug(" loop size {}", loopSize);

            for (int i = 0; i < loopSize; i++) {
                Pageable pageable = PageRequest.of(i, pageSize);
                List<TempBean> records = testRepo.findAllUsersByCol2("ganesh", pageable);

                log.info(" pulling page {} record count {}", i, records.size());

                // Write list to StatefulBeanToCsv object
                beanWriter.write(records);

            }

            // closing the writer object
            writer.close();

        } catch (Exception e) {
            log.error(" Exception while writing file {} stacktrace {}", e.getMessage(), e.getStackTrace());
        }

        pushFileToS3();
    }

    public void pushFileToS3() {
        s3Service.putObject(new File("/tmp/" + fileName), bucket
                , Constants.SMALL_CASE_FILE_PATH
                        .replace("date", new SimpleDateFormat("dd-MM-yyyy").format(new Date())));
    }
}
