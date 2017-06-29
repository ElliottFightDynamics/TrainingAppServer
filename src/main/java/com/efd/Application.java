package com.efd;

import com.efd.core.Secure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by volodymyr on 13.06.17.
 */
@SpringBootApplication
public class Application {

    public static void main(String args[]) {
        try {
            SpringApplication.run(Application.class, args);
        } catch (Exception ignored) {}
    }

}
