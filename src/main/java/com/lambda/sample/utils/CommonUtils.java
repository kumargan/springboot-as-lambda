package com.lambda.sample.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String getString(Object obj) {
        String str = "";
        try {
            str = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error(" could not convert obj into string ", e);
        }
        return str;
    }

}
