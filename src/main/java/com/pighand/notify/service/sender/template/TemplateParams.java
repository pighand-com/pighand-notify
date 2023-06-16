package com.pighand.notify.service.sender.template;

import com.pighand.framework.spring.util.CodeType;
import com.pighand.framework.spring.util.CodeUtil;
import com.pighand.notify.common.EnumTemplateParams;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模板参数处理
 *
 * <p>支持参数 {@link com.pighand.notify.common.EnumTemplateParams}
 *
 * @author wangshuli
 */
public class TemplateParams {

    /**
     * 模板替换信息
     *
     * <p>{ { 参数类型: 当前参数查找正则 } : 模板值替换方法 }
     *
     * <p>{CODE} 随机6位验证码（大写、小写、数字）
     *
     * <p>{CODE_n} 随机n位验证码（大写、小写、数字）
     *
     * <p>{CODE_type_n} 随机n位type类型验证码（type：{@link com.pighand.framework.spring.util.CodeType}）
     *
     * <p>{TIMESTAMP} 时间戳
     */
    private static final Map<ParamKey, DisposeInterface> params =
            Map.of(
                    // CODE
                    new ParamKey(EnumTemplateParams.CODE, "\\{CODE\\}"),
                    (matching) -> CodeUtil.randomCode(CodeType.NUMBER_LETTER, 6),

                    // CODE_n
                    new ParamKey(EnumTemplateParams.CODE, "\\{CODE_\\d*\\}"),
                    (matching) -> {
                        String num = matching.replace("{CODE_", "").replace("}", "");
                        return CodeUtil.randomCode(CodeType.NUMBER_LETTER, Integer.parseInt(num));
                    },

                    // CODE_type_n
                    new ParamKey(EnumTemplateParams.CODE, "\\{CODE\\d_\\d*\\}"),
                    (matching) -> {
                        String[] typeAndNum =
                                matching.replace("{CODE", "").replace("}", "").split("_");

                        return CodeUtil.randomCode(
                                CodeType.get(Integer.parseInt(typeAndNum[0])),
                                Integer.parseInt(typeAndNum[1]));
                    },

                    // TIMESTAMP
                    new ParamKey(EnumTemplateParams.TIMESTAMP, "\\{TIMESTAMP\\}"),
                    (matching) -> {
                        return System.currentTimeMillis();
                    });

    /**
     * 替换模板参数
     *
     * @param templates 模板信息
     * @param returnTemplateParams 需要返回的替换模板参数值
     * @return
     */
    public static TemplateParamsInfo replace(
            String[] templates, List<EnumTemplateParams> returnTemplateParams) {
        TemplateParamsInfo templateParamsInfo = new TemplateParamsInfo();

        if (null == templates) {
            return templateParamsInfo;
        }

        String[] returnTemplates = new String[templates.length];
        Map<EnumTemplateParams, String> returnParams =
                new HashMap<>(Optional.ofNullable(returnTemplateParams).map(List::size).orElse(0));

        for (int i = 0; i < templates.length; i++) {
            String template = templates[i];

            for (ParamKey paramKey : params.keySet()) {
                // 查找模板中的参数
                Pattern paramPattern = Pattern.compile(paramKey.getPattern());
                Matcher paramMatcher = paramPattern.matcher(template);

                if (paramMatcher.find()) {
                    // 使用替换方法，计算替换内容
                    String replaceValue = returnParams.get(paramKey.getType());
                    if (replaceValue == null) {
                        replaceValue =
                                params.get(paramKey).dispose(paramMatcher.group(0)).toString();
                    }

                    // 根据returnTemplateParams判断是否需要返回参数值
                    if (returnTemplateParams != null
                            && returnTemplateParams.contains(paramKey.getType())) {
                        returnParams.put(paramKey.getType(), replaceValue);
                    }

                    template = template.replaceAll(paramKey.getPattern(), replaceValue);
                }
            }

            returnTemplates[i] = template;
        }

        templateParamsInfo.setTemplates(returnTemplates);
        templateParamsInfo.setReturnParams(returnParams);

        return templateParamsInfo;
    }

    /** 模板参数替换方法 */
    private interface DisposeInterface {

        /**
         * 处理模板参数
         *
         * @param matching
         * @return
         */
        Object dispose(String matching);
    }

    /** 模板参数key */
    @Data
    private static class ParamKey {
        /** 模板参数类型 */
        private EnumTemplateParams type;

        /** 查找正则 */
        private String pattern;

        public ParamKey(EnumTemplateParams type, String pattern) {
            this.type = type;
            this.pattern = pattern;
        }
    }
}
