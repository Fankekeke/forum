package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.MessageInfo;
import cc.mrbird.febs.cos.service.IMessageInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/message-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageInfoController {

    private final IMessageInfoService messageInfoService;

    /**
     * 分页查询消息信息
     * @param page
     * @param messageInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, MessageInfo messageInfo) {
        return R.ok(messageInfoService.messageInfoByPage(page, messageInfo));
    }

    /**
     * 新增系统消息
     * @param messageInfo
     * @return
     */
    @PostMapping
    public R save(MessageInfo messageInfo) {
        messageInfo.setReadStatus(0);
        messageInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(messageInfoService.save(messageInfo));
    }

    /**
     * 设置系统消息已读
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(messageInfoService.update(Wrappers.<MessageInfo>lambdaUpdate().set(MessageInfo::getReadStatus, 1)
                .in(MessageInfo::getId, ids)));
    }

}
