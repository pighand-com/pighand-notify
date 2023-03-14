package com.pighand.notify.controller;

import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import com.pighand.notify.domain.MessageContentDomain;
import com.pighand.notify.service.MessageContentService;
import com.pighand.notify.vo.MessageContentVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 内容消息
 *
 * @author wangshuli
 * @createDate 2023-03-09 11:28:59
 */
@RestController(path = "message/content", docName = "内容消息")
public class MessageContentController extends BaseController<MessageContentService> {

    /**
     * @param messageContentVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "messageContentCreate")
    public Result<MessageContentVO> create(
            @Validated({ValidationGroup.Create.class}) @RequestBody
                    MessageContentVO messageContentVO) {
        messageContentVO = super.service.create(messageContentVO);

        return new Result(messageContentVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "/{id}", docSummary = "详情")
    public Result<MessageContentDomain> find(@PathVariable(name = "id") Long id) {
        MessageContentDomain messageContentDomain = super.service.find(id);

        return new Result(messageContentDomain);
    }

    /**
     * @param messageContentVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "messageContentQuery")
    public Result<PageOrList<MessageContentVO>> query(MessageContentVO messageContentVO) {
        PageOrList<MessageContentVO> result = super.service.query(messageContentVO);

        return new Result(result);
    }

    /**
     * @param messageContentVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "messageContentUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody MessageContentVO messageContentVO) {
        messageContentVO.setId(id);
        super.service.update(messageContentVO);

        return new Result();
    }

    /**
     * @param id
     */
    @Delete(path = "{id}", docSummary = "删除")
    public Result delete(@PathVariable(name = "id") Long id) {
        super.service.delete(id);
        return new Result();
    }
}
