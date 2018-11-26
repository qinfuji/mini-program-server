package com.bczl.coupon.actions;

import com.bczl.web.action.ActionRequest;
import com.bczl.web.action.ModelAndView;
import com.bczl.web.action.RouteAction;
import io.vertx.core.Future;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 使用券,
 * 角色：用户
 * 前置条件：当前用户有领取的券后使用的券， 商户使用小程序扫码功能使用该券，并支付商家。
 */
@Component
public class UseCouponAction implements RouteAction {

    @Value("${actions.useCouponAction.route}")
    private String route;

    @Value("${actions.useCouponAction.template}")
    private String template;

    @Override
    public String route() {
        return null;
    }

    @Override
    public Future<ModelAndView> execute(ActionRequest actionRequest) {
        return null;
    }
}
