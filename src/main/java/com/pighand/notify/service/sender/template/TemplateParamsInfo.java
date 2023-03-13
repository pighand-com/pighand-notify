package com.pighand.notify.service.sender.template;

import com.pighand.notify.common.EnumTemplateParams;
import lombok.Data;

import java.util.Map;

/**
 * 模板信息
 *
 * @author wangshuli
 */
@Data
public class TemplateParamsInfo {
    /** 模板信息 */
    String[] templates;

    /** 返回的模板参数 */
    Map<EnumTemplateParams, Object> returnParams;
}
