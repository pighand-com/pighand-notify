package com.pighand.notify.vo.send;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pighand.framework.spring.api.annotation.field.Field;
import com.pighand.notify.common.EnumTemplateParams;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * send common request
 *
 * @author wangshuli
 */
@Data
@JsonIgnoreProperties({"isFormatSendContents", "sendContents"})
public class SendCommonVO implements Serializable {

    @Field
    @Schema(description = "需要Response返回的内置模板参数")
    private List<EnumTemplateParams> returnTemplateParams;

    @Field
    @NotNull
    @Schema(description = "发型消息id")
    private Long messageId;

    @Field
    @NotNull
    @Schema(description = "接收人")
    private List<String> to;

    /** 是否已格式化发送内容 */
    private Boolean isFormatSendContents;

    /** 发送内容 */
    private String[] sendContents;
}
