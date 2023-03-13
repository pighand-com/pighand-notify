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
 * 内容消息
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
@TableName(value = "message_content")
@Data
public class MessageContentDomain extends BaseDomain implements Serializable {
    @TableId
    @RequestFieldException("messageContentCreate")
    @RequestFieldException("messageContentUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    private Long projectId;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 16)
    @Schema(description = "适用场景")
    private String scene;

    @Length(max = 32)
    @Schema(description = "标题")
    private String title;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 65535)
    @Schema(description = "模板")
    private String template;

    @Schema(description = "默认发送类型")
    private Integer defSenderType;

    @Schema(description = "默认发送者id")
    private Long defSenderId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
