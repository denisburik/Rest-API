package br.com.school.product.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(" br.com.school.product")
public class Application {
    public static void main(String[] args) {
        var application = new SpringApplication(Application.class);
        application.run(args);
    }
}
