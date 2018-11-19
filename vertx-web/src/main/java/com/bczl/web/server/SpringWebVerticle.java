package com.bczl.web.server;


import com.bczl.web.action.ActionContext;
import com.bczl.web.action.RouteAction;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.TimeoutHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * vertx web server
 */
@Component
public class SpringWebVerticle extends AbstractVerticle implements ApplicationContextAware {

   private static final Logger LOG = LoggerFactory.getLogger(SpringWebVerticle.class);
    private ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        try {
            String server_port = ctx.getEnvironment().getProperty("server.port");
            Integer port;
            try {
                port = Integer.valueOf(server_port);
            } catch (NumberFormatException e) {
                throw new Exception("Argument of spring app 'server.port' is illegal.", e);
            }
            Router router = Router.router(vertx);
            routes(router);

            vertx.createHttpServer()
                    .requestHandler(router::accept)
                    .listen(port);
            LOG.info("start web server : port : "+port);
        } catch (Exception e) {
            LOG.error("start web server error" , e);
        }
    }

    /**
     * 路由
     * 该方法中的路由注册顺序由于vertx平台的实现，顺序不能打乱
     *
     * @param router 路由器
     */
    void routes(Router router) {

        /*通用路由*/
        commonRoutes(router);
        /*action路由*/
        actionRoutes(router);
    }

    /**
     * 通用路由
     *
     * @param router 路由器
     */
    void commonRoutes(Router router) {
        /*心跳*/
        router.route("/heartbeat").handler(ctx -> {
            ctx.response().setStatusCode(200).end();
            ctx.response().close();
        });

        /*请求处理超时：最大10秒*/
        router.route().handler(TimeoutHandler.create(10000));

        /*post请求体*/
        router.route().handler(BodyHandler.create());

        /*api统一响应json格式Content-Type头*/
        router.route().handler(ctx -> {
            ctx.response().putHeader("Content-Type", "application/json;charset=utf-8");
            ctx.next();
        });
        router.route().handler(ctx -> {
            ctx.response().putHeader("Access-Control-Allow-Origin", "*");
            ctx.response().putHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            ctx.next();
        });
    }

    void actionRoutes(Router router) {
        Map<String, RouteAction> actionMap = this.ctx.getBeansOfType(RouteAction.class);
        System.out.println(actionMap);
        ActionContext actionContext = this.ctx.getBean(ActionContext.class);
        if (actionMap != null && !actionMap.isEmpty()) {
            actionMap.entrySet().stream()
                    /*转换handler*/
                    .map(entry -> {
                        RouteAction action = entry.getValue();

                        return actionContext.build(action);
                    })
                    /*排序*/
                    .sorted(Comparable::compareTo)
                    /*绑定handler*/
                    .forEach(handler -> {
                        if (handler != null) {
                            String route = handler.route();
                            router.route(route).handler(handler::handle);
                        }
                    });
        }
    }
}
