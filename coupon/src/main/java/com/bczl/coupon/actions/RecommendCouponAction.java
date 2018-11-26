package com.bczl.coupon.actions;

import com.bczl.web.action.ActionRequest;
import com.bczl.web.action.ModelAndView;
import com.bczl.web.action.RouteAction;
import io.vertx.core.Future;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 推荐权
 * 角色:用户
 * 前置条件：1、用户使用完毕后的优惠券可以进行推荐
 *           2、当前优惠券任然有剩余昂
 *           3、用户已经登录，并且授权获取到了用户基本信息
 *  输出：在我的推荐中显示推荐的券
 */
@Component
public class RecommendCouponAction implements RouteAction {

    @Value("${actions.recommendCouponAction.route}")
    private String route;

    @Value("${actions.recommendCouponAction.template}")
    private String template;

    @Override
    public String route() {
        return route;
    }

    @Override
    public Future<ModelAndView> execute(ActionRequest actionRequest) {
        return null;
    }
}
