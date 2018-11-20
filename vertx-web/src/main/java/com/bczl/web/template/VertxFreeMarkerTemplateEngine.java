package com.bczl.web.template;

import io.vertx.core.*;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * vertx freeMarker template engine
 * <p>
 * Created by licheng1 on 2017/3/31.
 */
@Component
public class VertxFreeMarkerTemplateEngine {

    //@Autowired
    private Vertx vertx;

    TemplateEngine engine = new FreeMarkerTemplateEngine();

    public void render(RoutingContext routingContext, String templatePath, Handler<AsyncResult<String>> handler) {

        try {
            Map<String, Object> model = new HashMap<>(1);
            model.put("context", routingContext);

            Context context = vertx.getOrCreateContext();
            context.runOnContext(v -> {
                try {
                    String res = engine.render(templatePath, model);
                    handler.handle(Future.succeededFuture(res));
                } catch (Exception e) {
                    handler.handle(Future.failedFuture(e));
                }
            });
        } catch (Exception e) {
            handler.handle(Future.failedFuture(e));
        }
    }

    public Vertx getVertx() {
        return vertx;
    }

    public void setVertx(Vertx vertx) {
        this.vertx = vertx;
    }
}
