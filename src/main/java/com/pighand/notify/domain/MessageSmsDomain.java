package com.pighand.notify.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.gson.JsonObject;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.api.springdoc.dataType.EmptyObject;
import com.pighand.framework.spring.base.BaseDomain;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 短信消息
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
@TableName(value = "message_sms")
@Data
public class MessageSmsDomain extends BaseDomain implements Serializable {
    @TableId
    @RequestFieldException("messageSmsCreate")
    @RequestFieldException("messageSmsUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    private Long projectId;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "云平台：1阿里云 2腾讯云")
    private Boolean cloudPlatform;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 32)
    private String appid;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 32)
    private String secret;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 16)
    @Schema(description = "适用场景")
    private String scene;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 32)
    @Schema(description = "短信签名名称")
    private String signName;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 32)
    @Schema(description = "短信模板code")
    private String templateCode;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "短信模板参数", implementation = EmptyObject.class)
    private JsonObject templateParam;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
