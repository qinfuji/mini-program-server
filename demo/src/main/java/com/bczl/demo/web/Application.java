package com.bczl.demo.web;

import com.bczl.web.VertxWebApplicationBuilder;
import com.bczl.web.server.VertxDeployListener;
import com.bczl.web.server.VertxEnvironmentListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        new VertxWebApplicationBuilder().sources(Application.class).listeners(new VertxEnvironmentListener()).listeners(new VertxDeployListener()).run(args);
    }
}
