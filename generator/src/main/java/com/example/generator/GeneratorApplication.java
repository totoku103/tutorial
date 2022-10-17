package com.example.generator;

import com.example.generator.process.SendLimitProcess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GeneratorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(GeneratorApplication.class, args);
        SendLimitProcess process = app.getBean(SendLimitProcess.class);
        process.process();
    }

}
