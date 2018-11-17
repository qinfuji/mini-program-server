package com.bczl.web.action;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * handler 请求
 */
public class ActionRequest {

    private HttpMethod method;
    private HttpVersion version;
    private String absoluteURI;
    private String scheme;
    private String host;
    private String uri;
    private String path;
    private String query;
    private SocketAddress localAddress;
    private SocketAddress remoteAddress;
    private Map<String, String> params;
    private String bodyString;
    private Map<String, String> headers;

    public ActionRequest() {
    }

    public ActionRequest(RoutingContext routingContext) {
        HttpServerRequest serverRequest = routingContext.request();
        this.method = serverRequest.method();
        this.version = serverRequest.version();
        this.absoluteURI = serverRequest.absoluteURI();
        this.scheme = serverRequest.scheme();
        this.host = serverRequest.host();
        this.uri = serverRequest.uri();
        this.path = serverRequest.path();
        this.query = serverRequest.query();
        this.localAddress = serverRequest.localAddress();
        this.remoteAddress = serverRequest.remoteAddress();
        this.parameters(routingContext);
        this.headers(routingContext);
    }

    void parameters(RoutingContext routingContext) {
        this.params = builtInMap(routingContext.request().params());
        if (routingContext.request().method() == HttpMethod.POST) {
            this.bodyString = routingContext.getBodyAsString();
        }
    }

    void headers(RoutingContext routingContext) {
        MultiMap _headers = routingContext.request().headers();
        Iterator<Map.Entry<String, String>> iterable = _headers.iterator();
        Map<String, String> headerMap = null;
        while (iterable.hasNext()) {
            Map.Entry<String, String> entry = iterable.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (headerMap == null) {
                headerMap = new HashMap<>();
            }
            headerMap.putIfAbsent(key, value);
        }
        this.headers = headerMap;
    }

    Map<String, String> builtInMap(MultiMap multiMap) {
        return Stream.of(multiMap)
                .flatMap(multi -> multi.entries().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v2));
    }

    public String getParameter(String name) {
        return this.getParams() == null ? null : this.getParams().get(name);
    }

    public ActionRequest addParameter(String name, String value) {
        if (this.getParams() == null) {
            this.params = new HashMap<>();
        }
        this.params.put(name, value);
        return this;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public String getAbsoluteURI() {
        return absoluteURI;
    }

    public void setAbsoluteURI(String absoluteURI) {
        this.absoluteURI = absoluteURI;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public SocketAddress getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(SocketAddress localAddress) {
        this.localAddress = localAddress;
    }

    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getBodyString() {
        return bodyString;
    }

    public void setBodyString(String bodyString) {
        this.bodyString = bodyString;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
