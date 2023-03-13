package com.pighand.notify.util;

import com.pighand.notify.vo.send.EmailSendInfoVO;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * 邮件发送
 *
 * @author wangshuli
 */
public class EmailUtil {
    /**
     * 发送
     *
     * @param emailSendVO
     * @throws MessagingException
     */
    public static void send(EmailSendInfoVO emailSendVO) throws Exception {
        Properties properties = System.getProperties();

        String protocol = emailSendVO.getProtocol();
        boolean ssl = emailSendVO.getSsl();
        properties.put("mail." + protocol + ".host", emailSendVO.getHost());
        properties.put("mail." + protocol + ".port", emailSendVO.getPort());

        String account = emailSendVO.getAccount();
        properties.put("mail." + protocol + ".user", account);

        // password
        String password = emailSendVO.getPassword();
        boolean hasPassword = StringUtils.hasText(password);
        properties.put("mail." + protocol + ".auth", hasPassword);
        if (hasPassword) {
            properties.put("mail." + protocol + ".pass", password);
        }

        // ssl
        properties.put("mail." + protocol + ".ssl", ssl);
        properties.put(
                "mail." + protocol + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session =
                Session.getInstance(
                        properties,
                        new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                if (ssl) {
                                    return new PasswordAuthentication(account, password);
                                }

                                return super.getPasswordAuthentication();
                            }
                        });

        MimeMessage message = new MimeMessage(session);

        // from
        message.setSubject(emailSendVO.getTitle());
        message.setFrom(new InternetAddress(emailSendVO.getAccount()));

        // to
        List<String> tos = Optional.ofNullable(emailSendVO.getTos()).orElse(new ArrayList<>(1));
        if (StringUtils.hasText(emailSendVO.getTo())) {
            tos.add(emailSendVO.getTo());
        }

        InternetAddress[] addressesTo =
                tos.stream()
                        .map(
                                item -> {
                                    try {
                                        return new InternetAddress(item);
                                    } catch (AddressException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                        .toArray(InternetAddress[]::new);

        message.setRecipients(Message.RecipientType.TO, addressesTo);

        // 内容
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(emailSendVO.getContent());

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // 附件
        if (emailSendVO.getAttachments() != null) {
            for (EmailSendInfoVO.Attachment attachment : emailSendVO.getAttachments()) {
                BodyPart attachmentPart = new MimeBodyPart();

                attachmentPart.setFileName(attachment.getName());

                DataSource source = new FileDataSource(attachment.getFilePath());
                attachmentPart.setDataHandler(new DataHandler(source));

                multipart.addBodyPart(messageBodyPart);
            }
        }

        message.setContent(multipart);
        Transport.send(message);
    }
}
