package com.lambda.sample;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.lambda.sample.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com" })
@EntityScan(basePackages = {"com.lambda.sample.beans"})
@EnableJpaRepositories(basePackages = "com.lambda.sample.jdbc.repository")
@Slf4j
public class SampleApplication implements RequestHandler<ScheduledEvent, String>{

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Autowired
    FileService fileService;

    @Override
    public String handleRequest(ScheduledEvent event, Context context)
    {

        main(new String[]{});
        log.info("started job, it will keep running until done, once done kill itself, Event received : {} ", event);

        return event.toString();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {

        log.info("*************** Started application *****************");
        Long startTime = System.currentTimeMillis();

        //printService.readFromDBAndPrint();

        fileService.createFile();

        Long endTime = System.currentTimeMillis();
        Long totalTime = (endTime-startTime)/(1000*60);
        log.info("*************** All tasks done. Total time taken in minutes : {} ****************",totalTime);
        System.exit(0);
    }

}