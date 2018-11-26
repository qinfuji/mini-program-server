package com.bczl.coupon.actions;

import com.bczl.web.action.ActionRequest;
import com.bczl.web.action.ModelAndView;
import com.bczl.web.action.RouteAction;
import io.vertx.core.Future;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 转发券，用户转发红包功能
 * 角色：普通用户
 * 前置条件：1、用户已经登录到小程序，并且已经获取到用户基本信息，包括用户微信唯一id，用户电话，位置信息
 *           2、当前券转发红包未达到最大转发深度。
 *  使用位置：小程序
 */
@Component
public class DeliveCouponAction implements RouteAction {

    @Value("${actions.deliveCouponAction.route}")
    private String route;

    @Value("${actions.deliveCouponAction.template}")
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
