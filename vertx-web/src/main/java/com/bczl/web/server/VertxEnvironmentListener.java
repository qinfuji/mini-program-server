package com.bczl.web.server;

import org.springframework.core.env.*;


/**
 * vertx-spring服务环境配置监听器
 */
public class VertxEnvironmentListener {

    //private static final Logger LOG = LoggerFactory.getLogger(VertxEnvironmentListener.class);

    static final String SYS_PROP_EVENTLOOP_POOLSIZE = "vertx.eventLoopPoolSize";
    static final String SYS_PROP_WORKER_POOLSIZE = "vertx.workerPoolSize";

    public void config(ConfigurableEnvironment environment) {
        String _eventLoopPoolSize = (String) environment.getSystemProperties().get(SYS_PROP_EVENTLOOP_POOLSIZE);
        String _workerPoolSize = (String) environment.getSystemProperties().get(SYS_PROP_WORKER_POOLSIZE);
        MutablePropertySources sources = environment.getPropertySources();
        if (_eventLoopPoolSize == null || Integer.valueOf(_eventLoopPoolSize) < 1) {
            int core = Runtime.getRuntime().availableProcessors();
            core = core << 1;
            addCommandLineArgs(sources, SYS_PROP_EVENTLOOP_POOLSIZE, core + "");
            addSystemProperties(environment, SYS_PROP_EVENTLOOP_POOLSIZE, core + "");
        }
        if (_workerPoolSize == null || Integer.valueOf(_workerPoolSize) < 1) {
            addCommandLineArgs(sources, SYS_PROP_WORKER_POOLSIZE, "20");
            addSystemProperties(environment, SYS_PROP_WORKER_POOLSIZE, "20");
        }
    }

    void addCommandLineArgs(MutablePropertySources sources, String name, String value) {
        String _name = "commandLineArgs";
        String[] args = new String[]{"--" + name + "=" + value};
        if (sources.contains(_name)) {
            PropertySource<?> source = sources.get(_name);
            CompositePropertySource composite = new CompositePropertySource(_name);
            composite.addPropertySource(new SimpleCommandLinePropertySource(_name + "-" + args.hashCode(), args));
            composite.addPropertySource(source);
            sources.replace(_name, composite);
        } else {
            sources.addFirst(new SimpleCommandLinePropertySource(args));
        }
    }

    void addSystemProperties(ConfigurableEnvironment environment, String name, String value) {
        environment.getSystemProperties().put(name, value);
        System.setProperty(name, (value != null) ? value : "");
    }
}
