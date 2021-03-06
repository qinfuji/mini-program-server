package com.bczl.demo.web.actions;

import com.bczl.web.action.ActionRequest;
import com.bczl.web.action.FreemarkerView;
import com.bczl.web.action.ModelAndView;
import com.bczl.web.action.RouteAction;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FirstAction implements RouteAction {

    @Value("${actions.firstAction.route}")
    private String route;

    @Value("${actions.firstAction.template}")
    private String template;

    @Autowired
    private Vertx vertx;

    @Override
    public String route() {
        System.out.println(vertx);
        System.out.println("route :"+ route);
        return route;
    }

    @Override
    public Future<ModelAndView> execute(ActionRequest request) {
        FreemarkerView view = new FreemarkerView();
        view.setTemplate(this.template);
        /*模型*/
        ModelAndView modelAndView = new ModelAndView(view);
        return Future.succeededFuture(modelAndView);
    }
}
