package org.projects.historyorderservice;

import org.springframework.boot.SpringApplication;

public class TestHistoryOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(HistoryOrderServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
