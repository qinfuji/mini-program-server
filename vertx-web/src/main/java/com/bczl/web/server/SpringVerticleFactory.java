package com.bczl.web.server;

import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring verticle 工厂
 * <p>
 * Created by licheng1 on 2017/3/28.
 */
@Component
public class SpringVerticleFactory implements VerticleFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public String prefix() {
        return "springContext";
    }

    @Override
    public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
        String clazzName = VerticleFactory.removePrefix(verticleName);
        Object verticle = applicationContext.getBean(Class.forName(clazzName));
        return Verticle.class.cast(verticle);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean blockingCreate() {
        return true;
    }

}
