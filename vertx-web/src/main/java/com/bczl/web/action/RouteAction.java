package com.bczl.web.action;


import io.vertx.core.Future;

/**
 * 请求处理器action
 */
public interface RouteAction {

    /**
     * 监听路由地址
     *
     * @return 路由地址
     */
    String route();

    /**
     * 请求处理
     *
     * @param request 请求
     * @return 模型及视图
     */
    Future<ModelAndView> execute(ActionRequest request);

    default int order() {
        return 0;
    }
}
