package com.yehuo.utils;

import cn.hutool.core.io.resource.NoResourceException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import cn.hutool.setting.dialect.PropsUtil;

public class ConfigUtils {

    /**
     * 加载配置对象
     * @param tClass
     * @param prefix
     * @return
     * @param <T>
     */

    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    /**
     * 加载配置对象, 支持区分环境
     * @param tClass
     * @param prefix
     * @param environment
     * @return
     * @param <T>
     */

    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }

        String[] fileExtensions = {".yml", ".yaml", ".properties"};
        Props props = null;

        for (String extension : fileExtensions) {
            String configFileName = configFileBuilder.append(extension).toString();
            try {
                props = PropsUtil.get(configFileName);
                // 如果成功加载，则跳出循环
                break;
            } catch (NoResourceException e) {
                // 忽略当前异常，尝试下一个扩展名
                configFileBuilder.setLength(configFileBuilder.length() - extension.length()); // 移除当前尝试的扩展名
            }
        }
        return props.toBean(tClass, prefix);
    }

}
