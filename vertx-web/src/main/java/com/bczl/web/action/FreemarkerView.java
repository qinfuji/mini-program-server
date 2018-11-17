package com.bczl.web.action;

/**
 * freemarker view
 */
public class FreemarkerView implements ModelAndView.View {

    private String template;

    public FreemarkerView() {
    }

    public FreemarkerView(String template) {
        this.template = template;
    }

    @Override
    public String template() {
        return this.template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
