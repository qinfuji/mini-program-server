package com.bczl.web.server;

import com.bczl.web.VertxWebApp;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.VerticleFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * vertx-spring服务器部署监听器
 */
public class VertxDeployListener {

    private static final Logger LOG = LoggerFactory.getLogger(VertxDeployListener.class);


    public void deploy(ApplicationContext context) {

        /*存储applicationContext(全局)*/
        VertxWebApp.setCtx(context);
        /*获取vertx verticle工厂*/
        VerticleFactory factory = context.getBean( SpringVerticleFactory.class);
        /*获取vertx实例*/
        Vertx vertx = context.getBean(Vertx.class);
        /*注册verticle工厂*/
        vertx.registerVerticleFactory(factory);
        /*获取vertx的options*/
        VertxOptions options = context.getBean(VertxOptions.class);
        /*部署verticle*/
        Map<String, Verticle> verticleMap = context.getBeansOfType(Verticle.class);

        //部署实例配置
        DeploymentOptions deploymentOptions = new DeploymentOptions()
                .setInstances(options.getEventLoopPoolSize());
        if (verticleMap != null && !verticleMap.isEmpty()) {
            //部署Verticle
            for (String verticleName : verticleMap.keySet()) {
                String clazzName = verticleMap.get(verticleName).getClass().getName();
                clazzName = factory.prefix() + ":" + clazzName;
                if (deploymentOptions != null) {
                    vertx.deployVerticle(clazzName, deploymentOptions);
                } else {
                    vertx.deployVerticle(clazzName);
                }
            }
        }
    }
}
