package com.lulala.langchain4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.lulala"})
public class LangChain4JDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LangChain4JDemoApplication.class, args);
    }

}
