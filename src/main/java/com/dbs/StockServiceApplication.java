package com.dbs;

import com.dbs.service.DriveQuickstart;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

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

        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException exception) {
            LOG.error("Host not found : ", exception);
        }

        LOG.info("\n----------------------------------------------------------\n\t" +
                        "Rainbow Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                host,
                env.getProperty("server.port"));
    }
}
