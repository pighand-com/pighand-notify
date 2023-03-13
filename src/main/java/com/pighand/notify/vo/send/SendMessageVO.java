package com.pighand.notify.vo.send;

import com.pighand.framework.spring.api.annotation.field.Field;
import com.pighand.framework.spring.api.annotation.field.FieldException;
import com.pighand.notify.common.EnumSenderType;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

/**
 * send message request
 *
 * @author wangshuli
 */
@Data
public class SendMessageVO extends SendCommonVO {

    @Field
    @Schema(description = "发送邮箱账号。优先根据邮箱账号查找发送者；没有或没配置，使用senderId查找发送者")
    private String senderEmail;

    @FieldException("sendEmail")
    @Schema(description = "发送者类型。与senderId同时使用")
    private EnumSenderType senderType;

    @Field
    @Schema(description = "发送者id。没有或没配置，使用def_sender_id查找发送者")
    private Long senderId;
}
