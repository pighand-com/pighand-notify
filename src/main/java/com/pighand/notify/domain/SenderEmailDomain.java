package com.pighand.notify.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pighand.framework.spring.api.annotation.field.Field;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.field.ResponseField;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseDomain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 发送邮箱配置
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
@TableName(value = "sender_email")
@Data
public class SenderEmailDomain extends BaseDomain implements Serializable {
    @TableId
    @RequestFieldException("senderEmailCreate")
    @RequestFieldException("senderEmailUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    private Long projectId;

    @TableField("`account`")
    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 32)
    @Schema(description = "邮箱账号")
    private String account;

    @TableField("`name`")
    @Length(max = 16)
    @Schema(description = "发送者名称")
    private String name;

    @TableField("`password`")
    @Length(max = 64)
    @Schema(description = "邮箱密码")
    private String password;

    @Length(max = 4)
    @Schema(description = "发送协议：smtp、pop3、imap")
    private String protocol;

    @TableField("`host`")
    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 32)
    @Schema(description = "协议host")
    private String host;

    @TableField("`port`")
    @Schema(description = "端口")
    private Integer port;

    @TableField("`ssl`")
    @Schema(description = "启用SSL")
    private Boolean ssl;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
