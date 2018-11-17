package com.bczl.web.handler;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 路由处理接口
 * <p>
 * Created by licheng1 on 2017/3/31.
 */
public interface RouteHandler extends Comparable<RouteHandler> {

    String CONTENT_TYPE_FORM_DATA = "multipart/form-data";

    String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";

    /**
     * 路由地址
     *
     * @return uri
     */
    String route();

    /**
     * 请求处理
     *
     * @param routingContext 路由上下文
     */
    void handle(RoutingContext routingContext);

    default String postBody(RoutingContext routingContext) {
        return routingContext.getBodyAsString();
    }

    default Map<String, String> httpParams(RoutingContext routingContext) {
        HttpMethod method = routingContext.request().method();
        Map<String, String> params = routingContext.get("_parameterMap");
        if (params == null || params.isEmpty()) {
            if (Objects.equals(method, HttpMethod.GET)) {
                params = httpGetParams(routingContext);
            } else if (Objects.equals(method, HttpMethod.POST)) {
                params = httpPostParams(routingContext);
            }
            routingContext.put("_parameterMap", params);
        }
        return params;
    }

    default Map<String, String> httpGetParams(RoutingContext routingContext) {
        MultiMap paramsMap = routingContext.request().params();
        return Stream.of(paramsMap)
                .flatMap(multiMap -> multiMap.entries().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    default Map<String, String> httpPostParams(RoutingContext routingContext) {
        Map<String, String> params;
        String contentType = contentType(routingContext);
        if (Objects.nonNull(contentType) && (contentType.contains(CONTENT_TYPE_FORM_DATA) ||
                contentType.contains(CONTENT_TYPE_FORM_URLENCODED))) {
            params = Stream.of(routingContext.request().params())
                    .flatMap(multiMap -> multiMap.entries().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } else {
            params = parseBodyParams(postBody(routingContext));
        }
        return params;
    }

    default Map<String, String> parseBodyParams(String body) {
        Map<String, String> params = new HashMap<>();
        if (Objects.nonNull(body)) {
            Arrays.stream(body.split("&"))
                    .map(paramEntry -> paramEntry.split("="))
                    .filter(paramArray -> paramArray.length == 2)
                    .filter(paramArray -> Objects.nonNull(paramArray[0]) && !"".equals(paramArray[0]))
                    .forEach(array -> params.put(array[0], array[1]));
        }
        return params;
    }

    default String contentType(RoutingContext routingContext) {
        return routingContext.request().getHeader("Content-Type");
    }

    default String getPath(RoutingContext routingContext) {
        return routingContext.request().path();
    }

    default void response(String res, RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        if (!response.ended()) response.end(res);
        if (!response.closed()) response.close();
    }
}