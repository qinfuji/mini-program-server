package com.bczl.web.action;


import com.bczl.tools.JackSonUtils;
import com.bczl.web.handler.RouteHandler;
import com.bczl.web.template.VertxFreeMarkerTemplateEngine;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 请求处理器上下文
 */
@Component
public class ActionContext {

    private static final Logger LOG = LogManager.getLogger(ActionContext.class);

    @Autowired
    private VertxFreeMarkerTemplateEngine engine;

    //private RedisClient redisClient;

    public RouteHandler build(RouteAction action) {
        return new ActionHandler(action);
    }

    class ActionHandler implements RouteHandler {

        private RouteAction action;
        //private CacheStrategy cacheStrategy;

        public ActionHandler(RouteAction action) {
            this.action = action;
            //this.cacheStrategy = cacheStrategy(this.action);
        }

        @Override
        public String route() {
            return this.action.route();
        }

        @Override
        public void handle(RoutingContext routingContext) {
            ActionRequest request = request(routingContext);
            Future<String> cacheFuture = Future.future();
//            if (isCached(this.cacheStrategy)) {
//                hitCache(request, this.cacheStrategy).setHandler(result -> {
//                    String cache = null;
//                    if (result.succeeded()) {
//                        cache = result.result();
//                    } else {
//                        LOG.error("get cache fail", result.cause());
//                    }
//                    cacheFuture.complete(cache);
//                });
//            } else {
//                cacheFuture.complete();
//            }
            cacheFuture.complete(null);
            cacheFuture.compose(cache -> {
                if (cache == null || "".equals(cache)) {
                    try {
                        LOG.info("start execute ");
                        return this.action.execute(request).compose(modelAndView -> {
                            if (modelAndView == null) {
                                return Future.failedFuture("unknown error");
                            }
                            ModelAndView.View view = modelAndView.getView();
                            String templatePath = view.template();
                            Map<String, Object> models = modelAndView.getModels();
                            models.forEach(routingContext::put);

                            return render(templatePath, routingContext);
                        }).compose(res -> {
                            Future<String> fut = Future.future();
//                            if (res != null && isCached(this.cacheStrategy)) {
//                                storeCache(res, request, this.cacheStrategy)
//                                        .setHandler(storeResult -> fut.complete(res));
//                            } else {
//                                fut.complete(res);
//                            }
                            fut.complete(res);
                            return fut;
                        });
                    } catch (Exception e) {
                        return Future.failedFuture(e);
                    }
                } else {
                    return Future.succeededFuture(cache);
                }
            }).setHandler(resp -> {
                Future<String> fut = Future.future();
                if (resp.succeeded()) {
                    fut.complete(resp.result());
                } else {
                    render("templates/api_error.ftl", routingContext)
                            .setHandler(errResult -> {
                                if (errResult.succeeded()) {
                                    fut.complete(errResult.result());
                                } else {
                                    fut.complete("error");
                                }
                            });
                }
                fut.setHandler(r -> response(r.result(), routingContext));
            });
        }

        @Override
        public int compareTo(RouteHandler o) {
            if (o instanceof ActionHandler) {
                ActionHandler actionHandler = ActionHandler.class.cast(o);
                return this.action.order() - actionHandler.getAction().order();
            }
            return 0;
        }

        public RouteAction getAction() {
            return action;
        }

        public void setAction(RouteAction action) {
            this.action = action;
        }

        @Override
        public String toString() {
            return this.action.getClass().getSimpleName();
        }
    }

    Future<String> render(String templatePath, RoutingContext routingContext) {
        Future<String> renderFuture = Future.future();
        this.engine.render(routingContext, templatePath, result -> {
            if (result.succeeded()) {
                renderFuture.complete(result.result());
            } else {
                renderFuture.fail(result.cause());
            }
        });
        return renderFuture.compose(output -> {
            try {
                if (output != null) {
                    output = output.replaceAll("[\\r\\n\\t]", "");
                    if (output.startsWith("[")) {
                        List<Object> list = JackSonUtils.json2List(output, Object.class);
                        output = JackSonUtils.bean2Json(list);
                    } else if (output.startsWith("{")) {
                        Map<String, Object> map = JackSonUtils.json2Map(output, String.class, Object.class);
                        output = JackSonUtils.bean2Json(map);
                    }
                }
                return Future.succeededFuture(output);
            } catch (IOException e) {
                return Future.failedFuture(e);
            }
        });
    }

//    Future<String> hitCache(ActionRequest request, CacheStrategy cacheStrategy) {
//        String key = routeCacheKey(request, cacheStrategy.getParamKeys());
//        try {
//            String cache = redisClient.get(key);
//            return Future.succeededFuture(cache);
//        } catch (Exception e) {
//            return Future.failedFuture(e);
//        }
//    }

//    Future<Void> storeCache(String data, ActionRequest request, CacheStrategy cacheStrategy) {
//        String key = routeCacheKey(request, cacheStrategy.getParamKeys());
//        try {
//            int expire = cacheStrategy.getExpire();
//            redisClient.setex(key, data, expire);
//            return Future.succeededFuture();
//        } catch (Exception e) {
//            return Future.failedFuture(e);
//        }
//    }

//    String routeCacheKey(ActionRequest request, String... paramKeys) {
//        String path = request.getPath();
//
//        StringBuilder builder = new StringBuilder("routeCache::");
//        builder.append(path);
//
//        if (paramKeys != null && paramKeys.length > 0) {
//            Arrays.sort(paramKeys);
//            Map<String, String> params = request.getParams();
//            builder.append("?");
//            for (String key : paramKeys) {
//                String value = params.get(key);
//                value = value == null ? "" : value;
//                builder.append(key)
//                        .append("=")
//                        .append(value)
//                        .append("&");
//            }
//            builder.setLength(builder.length() - 1);
//        }
//
//        return builder.toString();
//    }
//
//
//
//    boolean isCached(CacheStrategy cacheStrategy) {
//        return cacheStrategy != null && cacheStrategy.isCached();
//    }

    ActionRequest request(RoutingContext routingContext) {
        return new ActionRequest(routingContext);
    }

//    CacheStrategy cacheStrategy(RouteAction action) {
//        Cacheable anno = action.getClass().getAnnotation(Cacheable.class);
//        if (anno != null) {
//            return CacheStrategy.build(anno.expire(), anno.paramKeys());
//        }
//        return null;
//    }


    public VertxFreeMarkerTemplateEngine getEngine() {
        return engine;
    }

    public void setEngine(VertxFreeMarkerTemplateEngine engine) {
        this.engine = engine;
    }

//    public RedisClient getRedisClient() {
//        return redisClient;
//    }
//
//    public void setRedisClient(RedisClient redisClient) {
//        this.redisClient = redisClient;
//    }
}
