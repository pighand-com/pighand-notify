package com.pighand.notify.grpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pighand.framework.spring.response.GrpcResult;
import com.pighand.framework.spring.util.VerifyUtils;
import com.pighand.notify.common.EnumSenderType;
import com.pighand.notify.common.EnumTemplateParams;
import com.pighand.notify.sdk.grpc.protobuf.ContentRequest;
import com.pighand.notify.sdk.grpc.protobuf.ContentResponse;
import com.pighand.notify.sdk.grpc.protobuf.SendServiceGrpc;
import com.pighand.notify.service.sender.MessageSender;
import com.pighand.notify.vo.send.SendMessageVO;

import io.grpc.stub.StreamObserver;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * @author wangshuli
 */
@Slf4j
@Component
@AllArgsConstructor
public class SendRpc extends SendServiceGrpc.SendServiceImplBase {

    private final MessageSender messageSender;

    @Override
    public void content(ContentRequest request, StreamObserver<ContentResponse> responseObserver) {
        new GrpcResult<>(
                responseObserver,
                ContentResponse.newBuilder(),
                () -> {
                    SendMessageVO sendVO = new SendMessageVO();
                    sendVO.setReturnTemplateParams(
                            request.getReturnTemplateParamsList().stream()
                                    .map(item -> EnumTemplateParams.valueOf(item.name()))
                                    .toList());
                    sendVO.setMessageId(request.getMessageId());
                    sendVO.setTo(request.getToList());
                    sendVO.setSenderEmail(request.getSenderEmail());
                    sendVO.setSenderType(EnumSenderType.get(request.getSenderType().getNumber()));
                    sendVO.setSenderId(request.getSenderId());

                    VerifyUtils.validateParams(sendVO, true);

                    return messageSender.send(sendVO);
                },
                (responseBuilder, result, code) -> {
                    responseBuilder.setCode(code.toString());

                    result.forEach((key, value) -> responseBuilder.putData(key.name(), value));
                },
                (responseBuilder, result, code, message) -> {
                    responseBuilder.setCode(code.toString());
                    responseBuilder.setError(message);

                    if (result != null) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            String jsonData = objectMapper.writeValueAsString(result);
                            responseBuilder.setErrorData(jsonData);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }
}
