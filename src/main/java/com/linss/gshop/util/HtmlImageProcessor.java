package com.linss.gshop.util;

import java.util.regex.Pattern;

public class HtmlImageProcessor {
    private static final Pattern IMG_PATTERN = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");

    public static String process(String html) {
        // 实现图片地址替换/上传逻辑
        // 示例：提取base64图片上传并替换URL
        return html;
    }
}
