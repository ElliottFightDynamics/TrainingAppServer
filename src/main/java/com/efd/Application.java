package com.efd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by volodymyr on 13.06.17.
 */
@SpringBootApplication
public class Application {

    public static void main(String args[]) {

        SpringApplication.run(Application.class, args);

    }

}
