package org.toxyc.bizum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BizumApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BizumApplication.class);
        app.setRegisterShutdownHook(false);
        app.run(args);
    }
}
