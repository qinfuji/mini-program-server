package com.bczl.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Objects;

@SpringBootApplication
public class VertxWebApp {

     private static ApplicationContext context;

     public static void setCtx(ApplicationContext ctx) {
          VertxWebApp.context = ctx;
     }

     public static ApplicationContext ctx() {
          return VertxWebApp.context;
     }

     public static Environment env() {
          Objects.requireNonNull(VertxWebApp.context, "spring application context is empty");
          return VertxWebApp.context.getEnvironment();
     }
}

