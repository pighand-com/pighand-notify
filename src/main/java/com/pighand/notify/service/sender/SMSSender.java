package com.pighand.notify.service.sender;

import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.google.gson.Gson;
import com.pighand.notify.common.EnumSMSPlatform;
import com.pighand.notify.common.EnumTemplateParams;
import com.pighand.notify.vo.send.SendSMSVO;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import darabonba.core.client.ClientOverrideConfiguration;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 发送短信
 *
 * @author wangshuli
 */
public class SMSSender extends BaseSenderAbstract<SendSMSVO> {

    /**
     * 腾讯云
     *
     * @param message
     */
    private void tencent(SendSMSVO message) throws TencentCloudSDKException {
        Credential cred = new Credential("secretId", "secretKey");
        SmsClient client = new SmsClient(cred, "ap-guangzhou");

        SendSmsRequest req = new SendSmsRequest();
        req.setSmsSdkAppId("1400009099");
        req.setSignName("");
        req.setTemplateId("");

        String[] templateParamSet = {"1234"};
        req.setTemplateParamSet(templateParamSet);

        String[] phoneNumberSet = message.getTo().toArray(String[]::new);
        req.setPhoneNumberSet(phoneNumberSet);

        SendSmsResponse res = client.SendSms(req);

        SendSmsResponse.toJsonString(res);
    }

    /**
     * 阿里云
     *
     * @param message
     */
    private void aliyun(SendSMSVO message) throws ExecutionException, InterruptedException {
        StaticCredentialProvider provider =
                StaticCredentialProvider.create(
                        com.aliyun.auth.credentials.Credential.builder()
                                .accessKeyId("<your-accessKeyId>")
                                .accessKeySecret("<your-accessKeySecret>")
                                .build());

        AsyncClient client =
                AsyncClient.builder()
                        .region("undefined")
                        .credentialsProvider(provider)
                        .overrideConfiguration(
                                ClientOverrideConfiguration.create()
                                        .setEndpointOverride("dysmsapi.aliyuncs.com"))
                        .build();

        com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest sendSmsRequest =
                com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest.builder()
                        .phoneNumbers(String.join(",", message.getTo()))
                        .signName("your_value")
                        .build();

        CompletableFuture<com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse> response =
                client.sendSms(sendSmsRequest);

        com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse resp = response.get();
        System.out.println(new Gson().toJson(resp));

        client.close();
    }

    @Override
    protected Map<EnumTemplateParams, Object> replaceSendContent(SendSMSVO message)
            throws Exception {
        return null;
    }

    @Override
    protected Boolean internalSendAsync(SendSMSVO message) throws Exception {
        if (message.getPlatform().equals(EnumSMSPlatform.ALIYUN)) {
            this.aliyun(message);
            return true;
        }

        return null;
    }

    @Override
    protected void internalSend(SendSMSVO message) throws Exception {}
}
