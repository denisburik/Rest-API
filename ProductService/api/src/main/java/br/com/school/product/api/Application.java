package br.com.school.product.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        var application = new SpringApplication(Application.class);
        application.run(args);
    }
}
