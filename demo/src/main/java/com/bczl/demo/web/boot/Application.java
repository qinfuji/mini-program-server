package com.bczl.demo.web.boot;

import com.bczl.web.VertxWebApplicationBuilder;
import com.bczl.web.server.VertxDeployListener;
import com.bczl.web.server.VertxEnvironmentListener;

public class Application {
    public static void main(String[] args) {
        new VertxWebApplicationBuilder().listeners(new VertxEnvironmentListener()).listeners(new VertxDeployListener()).run(args);
    }
}
