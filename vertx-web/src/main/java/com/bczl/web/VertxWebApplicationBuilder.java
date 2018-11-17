package com.bczl.web;

import com.bczl.web.server.VertxDeployListener;
import com.bczl.web.server.VertxEnvironmentListener;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.*;

public class VertxWebApplicationBuilder {
    //private static final Logger LOG = LoggerFactory.getLogger(VertxWebApplicationBuilder.class);

    private Set<Class> parents;
    private Set<Class> siblings;
    private Set<Class> children;

    private List<VertxDeployListener> deployListeners;
    private List<VertxEnvironmentListener> environmentListeners;

    public VertxWebApplicationBuilder parent(Class... sources) {
        if (this.parents == null) {
            this.parents = new LinkedHashSet<>();
        }
        this.parents.addAll(new LinkedHashSet<>(Arrays.asList(sources)));
        return this;
    }

    public VertxWebApplicationBuilder sources(Class... sources) {
        if (this.siblings == null) {
            this.siblings = new LinkedHashSet<>();
        }
        this.siblings.addAll(new LinkedHashSet<>(Arrays.asList(sources)));
        return this;
    }


    public VertxWebApplicationBuilder child(Class... sources) {
        if (this.children == null) {
            this.children = new LinkedHashSet<>();
        }
        this.children.addAll(new LinkedHashSet<>(Arrays.asList(sources)));
        return this;
    }

    public VertxWebApplicationBuilder listeners(VertxDeployListener... listeners) {
        if (this.deployListeners == null) {
            this.deployListeners = new ArrayList<>();
        }
        this.deployListeners.addAll(Arrays.asList(listeners));
        return this;
    }

    public VertxWebApplicationBuilder listeners(VertxEnvironmentListener... listeners) {
        if (this.environmentListeners == null) {
            this.environmentListeners = new ArrayList<>();
        }
        this.environmentListeners.addAll(Arrays.asList(listeners));
        return this;
    }

    public ApplicationListener<ApplicationEvent> applicationListener() {
        return applicationEvent -> {
            if (applicationEvent instanceof ApplicationEnvironmentPreparedEvent) {
                ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent = (ApplicationEnvironmentPreparedEvent) applicationEvent;
                ConfigurableEnvironment environment = applicationEnvironmentPreparedEvent.getEnvironment();
                if (this.environmentListeners != null) {
                    this.environmentListeners.forEach(listener -> listener.config(environment));
                }
            }
            if (applicationEvent instanceof ApplicationReadyEvent) {
                ApplicationReadyEvent applicationReadyEvent = (ApplicationReadyEvent) applicationEvent;
                ApplicationContext context = applicationReadyEvent.getApplicationContext();
                if (this.deployListeners != null) {
                    this.deployListeners.forEach(listener -> listener.deploy(context));
                }
            }
        };
    }

    public void run(String[] args) {
        if (this.deployListeners == null || this.deployListeners.isEmpty()) {
            System.exit(0);
        }

        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.bannerMode(Banner.Mode.OFF);

        if (this.parents != null && !this.parents.isEmpty()) {
            builder.parent(this.parents.toArray(new Class[0]));
        }
        if (this.siblings != null && !this.siblings.isEmpty()) {
            builder.sources(this.siblings.toArray(new Class[0]));
        }
        if (this.children != null && !this.children.isEmpty()) {
            builder.child(this.children.toArray(new Class[0]));
        }

        builder.sources(VertxWebApp.class)
                .logStartupInfo(false)
                .listeners(applicationListener())
                .run(args);
    }
}
