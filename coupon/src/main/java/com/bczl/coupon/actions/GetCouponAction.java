package com.bczl.coupon.actions;

import com.bczl.web.action.ActionRequest;
import com.bczl.web.action.ModelAndView;
import com.bczl.web.action.RouteAction;
import io.vertx.core.Future;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 领券
 * 角色:用户
 * 前置条件：1、当前油糊圈还有剩余量
 *           2、当前用户已经登录并已经获取了用户基本信息。
 *  输出：领取的券在用户券中心展现。券处于未使用状态。
 */
@Component
public class GetCouponAction implements RouteAction {

    @Value("${actions.getCouponAction.route}")
    private String route;

    @Value("${actions.getCouponAction.template}")
    private String template;

    @Override
    public String route() {
        return null;
    }

    @Override
    public Future<ModelAndView> execute(ActionRequest actionRequest) {
        return null;
    }

    @Override
    public int order() {
        return 0;
    }
}
