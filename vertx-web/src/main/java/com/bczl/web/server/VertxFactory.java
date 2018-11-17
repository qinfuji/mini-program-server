package com.bczl.web.server;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class VertxFactory {

    @Autowired
    private Environment env;

    @Bean
    public Vertx getVertx(){
        return Vertx.vertx(vertxOptions());
    }

    @Bean
    public VertxOptions vertxOptions(){
        int eventloopPoolSize =   env.getProperty(VertxEnvironmentListener.SYS_PROP_EVENTLOOP_POOLSIZE , Integer.class , 4  );
        int workerPoolSize = env.getProperty(VertxEnvironmentListener.SYS_PROP_WORKER_POOLSIZE, Integer.class , 4);
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setEventLoopPoolSize(eventloopPoolSize);
        vertxOptions.setWorkerPoolSize(workerPoolSize);

        return vertxOptions;
    }
}
