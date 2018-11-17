package com.bczl.tools;

import org.apache.commons.lang3.StringEscapeUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Apache commons组件工具
 * <p>
 * Created by licheng1 on 2017/1/12.
 */
public class CommonsUtils {

    /**
     * pojo对象转map
     *
     * @param obj pojo对象
     * @return map
     * @throws Exception
     */
    public static <V> Map<String, V> bean2Map(Object obj) throws Exception {
        Objects.requireNonNull(obj);
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptor = beanInfo.getPropertyDescriptors();
        Map<String, V> map = new HashMap<>();
        for (PropertyDescriptor property : propertyDescriptor) {
            String name = property.getName();
            if ("class".equals(name)) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter.invoke(obj);
            if (value == null) {
                continue;
            }
            map.put(name, (V) value);
        }

        return map;
    }

    /**
     * map转pojo对象
     *
     * @param map    map
     * @param tClass pojo类对象
     * @param <T>    pojo类型
     * @return pojo
     * @throws Exception
     */
    public static <T> T map2Bean(Map<String, Object> map, Class<T> tClass) throws Exception {
        Objects.requireNonNull(map);
        Objects.requireNonNull(tClass);
        BeanInfo beanInfo = Introspector.getBeanInfo(tClass);
        PropertyDescriptor[] propertyDescriptor = beanInfo.getPropertyDescriptors();
        T t = tClass.newInstance();
        for (PropertyDescriptor property : propertyDescriptor) {
            String name = property.getName();
            Object value = map.get(name);
            if (value == null) {
                continue;
            }
            Method setter = property.getWriteMethod();
            setter.invoke(t, value);
        }

        return t;
    }

    /**
     * 字符串url编码
     *
     * @param source  源字符串
     * @param charset 字符集
     * @return 编码结果
     * @throws UnsupportedEncodingException
     */
    public static String URLEncode(String source, String charset) throws Exception {
        String encode = URLEncoder.encode(source, defaultCharset(charset));
        return encode.replaceAll("\\+", "%20");
    }

    /**
     * 字符串url编码(默认编码UTF-8)
     *
     * @param source 源字符串
     * @return 编码结果
     * @throws UnsupportedEncodingException
     */
    public static String URLEncode(String source) throws Exception {
        return URLEncode(source, null);
    }

    /**
     * 字符串url解码
     *
     * @param source  源字符串
     * @param charset 字符集
     * @return 解码结果
     * @throws Exception
     */
    public static String URLDecode(String source, String charset) throws Exception {
        return URLDecoder.decode(source, defaultCharset(charset));
    }

    /**
     * 字符串html解码
     *
     * @param html 源字符串
     * @return 解码结果
     */
    public static String unescapeHtml4(String html) {
        return StringEscapeUtils.unescapeHtml4(html);
    }

    private static String defaultCharset(String charset) {
        if (Objects.isNull(charset) || Objects.equals(charset, ""))
            return "UTF-8";
        return charset;
    }
}
