package com.lambda.sample.utils.log.converter;


import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.lambda.sample.constants.Constants;
import org.slf4j.MDC;

public class MessageRTTConverter extends MessageConverter {

  public String convert(ILoggingEvent event) {
    return enhance(super.convert(event));
  }

  private String enhance(String incoming) {
      return MDC.get(Constants.RTT);
  }
}
