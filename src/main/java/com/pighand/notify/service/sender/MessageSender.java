package com.pighand.notify.service.sender;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.pighand.notify.common.EnumSenderType;
import com.pighand.notify.common.EnumTemplateParams;
import com.pighand.notify.domain.MessageContentDomain;
import com.pighand.notify.domain.SenderEmailDomain;
import com.pighand.notify.mapper.MessageContentMapper;
import com.pighand.notify.mapper.SenderEmailMapper;
import com.pighand.notify.service.sender.template.TemplateParams;
import com.pighand.notify.service.sender.template.TemplateParamsInfo;
import com.pighand.notify.util.EmailUtil;
import com.pighand.notify.vo.send.EmailSendInfoVO;
import com.pighand.notify.vo.send.SendMessageVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 发送模板消息
 *
 * @author wangshuli
 */
@Service
public class MessageSender extends BaseSenderAbstract<SendMessageVO> {

    @Autowired SenderEmailMapper senderEmailMapper;
    @Autowired MessageContentMapper messageContentMapper;

    /**
     * 发送邮件
     *
     * @param message
     * @throws Exception
     */
    private void sendByEmail(SendMessageVO message) throws Exception {
        LambdaQueryChainWrapper<SenderEmailDomain> queryChainWrapper =
                ChainWrappers.lambdaQueryChain(senderEmailMapper);

        if (StringUtils.hasText(message.getSenderEmail())) {
            queryChainWrapper.eq(SenderEmailDomain::getAccount, message.getSenderEmail());
        } else {
            queryChainWrapper.eq(SenderEmailDomain::getId, message.getSenderId());
        }

        queryChainWrapper.last("limit 1");
        SenderEmailDomain senderEmail = queryChainWrapper.one();

        if (senderEmail == null) {
            throw new Exception("未找到发送者");
        }

        EmailSendInfoVO emailSend = new EmailSendInfoVO();
        emailSend.setAccount(senderEmail.getAccount());
        emailSend.setName(senderEmail.getName());
        emailSend.setPassword(senderEmail.getPassword());
        emailSend.setProtocol(senderEmail.getProtocol());
        emailSend.setHost(senderEmail.getHost());
        emailSend.setPort(senderEmail.getPort());
        emailSend.setSsl(senderEmail.getSsl());
        emailSend.setTos(message.getTo());

        String[] sendContents = message.getSendContents();
        emailSend.setTitle(sendContents[0]);
        emailSend.setContent(sendContents[1]);

        EmailUtil.send(emailSend);
    }

    @Override
    protected Map<EnumTemplateParams, Object> replaceSendContent(SendMessageVO message)
            throws Exception {
        MessageContentDomain messageContentDomain =
                messageContentMapper.selectById(message.getMessageId());

        if (messageContentDomain == null) {
            throw new Exception("未找发送内容");
        }

        if (message.getSenderId() == null) {
            message.setSenderId(messageContentDomain.getDefSenderId());
        }

        EnumSenderType senderType =
                message.getSenderType() != null
                        ? message.getSenderType()
                        : EnumSenderType.get(messageContentDomain.getDefSenderType());

        if (null == senderType) {
            throw new Exception("消息类型不正确");
        }
        message.setSenderType(senderType);

        TemplateParamsInfo templateParamsInfo =
                TemplateParams.replace(
                        new String[] {
                            messageContentDomain.getTitle(), messageContentDomain.getTemplate()
                        },
                        message.getReturnTemplateParams());
        String[] templates = templateParamsInfo.getTemplates();

        message.setSendContents(templates);

        return templateParamsInfo.getReturnParams();
    }

    @Override
    protected Boolean internalSendAsync(SendMessageVO message) throws Exception {
        return false;
    }

    @Override
    protected void internalSend(SendMessageVO message) throws Exception {
        if (message.getSenderType().equals(EnumSenderType.EMAIL)) {
            this.sendByEmail(message);
        }
    }
}
