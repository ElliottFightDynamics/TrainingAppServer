package com.efd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by volodymyr on 13.06.17.
 */
@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        try {
            SpringApplication.run(Application.class, args);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }

}
