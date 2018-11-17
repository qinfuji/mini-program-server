package com.bczl.web.action;

import java.util.HashMap;
import java.util.Map;

/**
 * 模型与视图
 */
public class ModelAndView {

    private View view;

    private Map<String, Object> models;

    public ModelAndView() {
    }

    public ModelAndView(View view) {
        this.view = view;
    }

    public ModelAndView(View view, Map<String, Object> models) {
        this.view = view;
        this.models = models;
    }

    Map<String, Object> getModelMap() {
        if (this.models == null) {
            this.models = new HashMap<>();
        }
        return this.models;
    }

    public void addModel(String name, Object model) {
        getModelMap().put(name, model);
    }

    public Object getModel(String name) {
        return getModelMap().get(name);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Map<String, Object> getModels() {
        return this.getModelMap();
    }

    public void setModels(Map<String, Object> models) {
        this.models = models;
    }

    public interface View {

        String template();
    }
}
