package com.bczl.web.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.vertx.ext.web.impl.ConcurrentLRUCache;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * freeMarker 渲染模板引擎
 * <p>
 * Created by licheng1 on 2017/3/31.
 */
public class FreeMarkerTemplateEngine implements TemplateEngine {

    final ConcurrentLRUCache<String, Template> cache;
    Configuration configuration = new Configuration(Configuration.VERSION_2_3_24);
    String extension;

    ReentrantLock lock = new ReentrantLock();

    {
        cache = new ConcurrentLRUCache<>(10000);
        configuration.setClassForTemplateLoading(this.getClass(), "/");
        doSetExtension("ftl");
    }

    private void doSetExtension(String ext) {
        this.extension = ext.charAt(0) == 46 ? ext : "." + ext;
    }

    private String adjustLocation(String location) {
        if (!location.endsWith(this.extension)) {
            location = location + this.extension;
        }

        return location;
    }

    @Override
    public String render(String templatePath, Map<String, Object> model) throws Exception {

        Template template = this.cache.get(templatePath);
        if (template == null) {
            lock.lock();
            template = configuration.getTemplate(adjustLocation(templatePath));
            lock.unlock();
            this.cache.put(templatePath, template);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            template.process(model, new OutputStreamWriter(baos));
            return baos.toString();
        } finally {
            baos.close();
        }
    }
}
