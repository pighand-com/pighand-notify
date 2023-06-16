package com.pighand.notify.controller;

import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.response.Result;
import com.pighand.notify.common.EnumSenderType;
import com.pighand.notify.common.EnumTemplateParams;
import com.pighand.notify.service.sender.MessageSender;
import com.pighand.notify.vo.send.SendMessageVO;

import org.springframework.validation.annotation.Validated;

import java.util.Map;

/**
 * 发送
 *
 * @author wangshuli
 */
@RestController(path = "send", docName = "发送")
public class SendController extends BaseController<MessageSender> {

    @Get(
            path = "content",
            fieldGroup = "sendMessage",
            docSummary = "发送消息（同步）",
            docDescription = "同步发送。发送成功后返回")
    public Result content(@Validated SendMessageVO sendVO) throws Exception {
        Map<EnumTemplateParams, String> result = super.service.send(sendVO);

        return new Result(result);
    }

    @Get(
            path = "content/async",
            fieldGroup = "sendMessage",
            docSummary = "发送消息（异步）",
            docDescription = "异步发送。请求成功后直接返回")
    public Result contentAsync(@Validated SendMessageVO sendVO) throws Exception {
        Map<EnumTemplateParams, String> result = super.service.sendAsync(sendVO);

        return new Result(result);
    }

    @Get(
            path = "email",
            fieldGroup = "sendEmail",
            docSummary = "邮箱发送（同步）",
            docDescription = "同步发送。发送成功后返回")
    public Result email(@Validated SendMessageVO sendVO) throws Exception {
        sendVO.setSenderType(EnumSenderType.EMAIL);
        Map<EnumTemplateParams, String> result = super.service.send(sendVO);

        return new Result(result);
    }

    @Get(
            path = "email/async",
            fieldGroup = "sendEmail",
            docSummary = "邮箱发送（异步）",
            docDescription = "异步发送。请求成功后直接返回")
    public Result emailAsync(@Validated SendMessageVO sendVO) throws Exception {
        sendVO.setSenderType(EnumSenderType.EMAIL);
        Map<EnumTemplateParams, String> result = super.service.sendAsync(sendVO);

        return new Result(result);
    }
}
