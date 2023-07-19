package org.hoangdao.taskmanagement;

import org.hoangdao.taskmanagement.service.CrawlerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.IOException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class Main {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Main.class).getBean(CrawlerService.class)
                .crawMagazine();
    }
}