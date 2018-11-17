package com.bczl.web.template;

import java.util.Map;

/**
 * 模板引擎
 * <p>
 * Created by licheng1 on 2017/3/31.
 */
public interface TemplateEngine {

    /**
     * 渲染数据
     *
     * @param templatePath 模板文件路径
     * @param model        数据模型
     */
    String render(String templatePath, Map<String, Object> model) throws Exception;
}
