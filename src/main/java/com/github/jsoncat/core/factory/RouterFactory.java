package com.github.jsoncat.core.factory;

import com.github.jsoncat.annotation.GetMapping;
import com.github.jsoncat.annotation.PostMapping;
import com.github.jsoncat.annotation.RestController;
import com.github.jsoncat.common.util.UrlUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Save routing-related mapping information
 *
 * @author shuang.kou
 * @createTime 2020年09月29日 13:27:00
 **/
public class RouterFactory {
    // get request url -> target method.
    // eg: "^/user/[\u4e00-\u9fa5_a-zA-Z0-9]+/?$" -> UserController.get(java.lang.Integer)
    public static final Map<String, Method> GET_REQUEST_MAPPINGS = new HashMap<>();
    // post request url -> target method.
    public static final Map<String, Method> POST_REQUEST_MAPPINGS = new HashMap<>();
    // formatted get request url -> original url
    // eg : "^/user/[\u4e00-\u9fa5_a-zA-Z0-9]+/?$" -> /user/{id}
    public static final Map<String, String> GET_URL_MAPPINGS = new HashMap<>();
    // formatted post request url -> original url
    public static final Map<String, String> POST_URL_MAPPINGS = new HashMap<>();

    public static void loadRoutes() {
        Set<Class<?>> classes = ClassFactory.CLASSES.get(RestController.class);
        for (Class<?> aClass : classes) {
            RestController restController = aClass.getAnnotation(RestController.class);
            if (null != restController) {
                Method[] methods = aClass.getDeclaredMethods();
                String baseUrl = restController.value();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        GetMapping getMapping = method.getAnnotation(GetMapping.class);
                        if (getMapping != null) {
                            String url = baseUrl + getMapping.value();
                            String formattedUrl = UrlUtil.formatUrl(url);
                            GET_REQUEST_MAPPINGS.put(formattedUrl, method);
                            GET_URL_MAPPINGS.put(formattedUrl, url);
                        }
                    }
                    if (method.isAnnotationPresent(PostMapping.class)) {
                        PostMapping postMapping = method.getAnnotation(PostMapping.class);
                        if (postMapping != null) {
                            String url = baseUrl + postMapping.value();
                            String formattedUrl = UrlUtil.formatUrl(url);
                            POST_REQUEST_MAPPINGS.put(formattedUrl, method);
                            POST_URL_MAPPINGS.put(formattedUrl, url);
                        }
                    }
                }
            }
        }
    }

}
