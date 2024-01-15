package com.cqdTaskProcessing.CQD.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
@EnableAsync
public class CqdConfig {

    //FIXME - remove me pleaseeeeeeeeeeeeeeeeeeeeeeee
//    @Bean
//    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//        return args -> {
//
//            String[] beanNames = ctx.getBeanDefinitionNames();
//            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                System.out.println(beanName);
//            }
//        };
//    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}