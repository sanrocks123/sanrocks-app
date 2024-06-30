package com.sanrocks;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

@SpringBootApplication(exclude = {GsonAutoConfiguration.class})
public class GatewayServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceMain.class, args);
    }
}
