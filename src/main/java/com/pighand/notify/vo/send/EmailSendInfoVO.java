package com.pighand.notify.vo.send;

import com.pighand.notify.domain.SenderEmailDomain;
import lombok.Data;

import java.util.List;

/**
 * 邮件发送信息
 *
 * @author wangshuli
 */
@Data
public class EmailSendInfoVO extends SenderEmailDomain {

    /** 接收人 */
    private String to;

    /** 接收人 */
    private List<String> tos;

    /** 标题 */
    private String title;

    /** 内容 */
    private String content;

    /** 附件 */
    private List<Attachment> attachments;

    /** 附件信息 */
    @Data
    public class Attachment {
        /** 附件名 */
        private String name;

        /** 文件地址 */
        private String filePath;
    }
}
