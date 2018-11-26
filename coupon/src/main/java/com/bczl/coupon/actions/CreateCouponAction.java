package com.bczl.coupon.actions;

import com.bczl.web.action.ActionRequest;
import com.bczl.web.action.FreemarkerView;
import com.bczl.web.action.ModelAndView;
import com.bczl.web.action.RouteAction;
import io.vertx.core.Future;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 创建优惠券、转发红包券
 * 角色：商户
 * 前置条件：商户已经登录
 * 创建位置：后台、手机端
 * 书剑类型：优惠券、传递红包？
 */
@Component
public class CreateCouponAction implements RouteAction {
    @Value("${actions.createCouponAction.route}")
    private String route;

    @Value("${actions.createCouponAction.template}")
    private String template;

    @Override
    public String route() {
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
