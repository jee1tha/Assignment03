package com.dbs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

@SpringBootApplication
public class StockServiceApplication {

    private final Environment env;

    @Inject
    public StockServiceApplication(Environment env) {
        this.env = env;
    }

    private static final Logger LOG = LoggerFactory.getLogger(StockServiceApplication.class);

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(StockServiceApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("***** Application started *****");
    }
}
