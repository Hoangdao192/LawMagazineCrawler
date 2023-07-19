package org.hoangdao.taskmanagement;

import org.hoangdao.taskmanagement.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {
    @Autowired
    private CrawlerService crawlerService;

    @Override
    public void run(String... args) throws Exception {
        crawlerService.crawMagazine();
    }
}
