package com.lambda.sample.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class FileUtils {

    public static boolean fileExist(File file){

        try {
           return file.exists();
        }catch(Exception e){
           log.error("Error in finding file");
        }
        return false;
    }


    public static boolean deleteFileIfExist(File file){
        try {
            if(fileExist(file)){
                log.info("File Already exist , Deleting it");
                return file.delete();
            }
        }catch(Exception e){
            log.error("Error in Deleting file {} {}",e.getMessage(),e);
        }
        return false;
    }
}
