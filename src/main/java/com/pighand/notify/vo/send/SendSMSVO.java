package com.pighand.notify.vo.send;

import com.pighand.notify.common.EnumSMSPlatform;
import lombok.Data;

/**
 * send sms request
 *
 * @author wangshuli
 */
@Data
public class SendSMSVO extends SendCommonVO {
    /** 短信平台 */
    private EnumSMSPlatform platform;
}
