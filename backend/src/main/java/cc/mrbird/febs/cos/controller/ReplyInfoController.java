package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.*;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.UserService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/reply-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReplyInfoController {

    private final IReplyInfoService replyInfoService;

    private final ISensitiveInfoService sensitiveInfoService;

    private final IFocusInfoService focusInfoService;

    private final ICollectInfoService collectInfoService;

    private final IMessageInfoService messageInfoService;

    private final UserService userService;

    private final IPostInfoService postInfoService;

    /**
     * 分页查询回复信息
     * @param page
     * @param replyInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, ReplyInfo replyInfo) {
        return R.ok(replyInfoService.replyInfoByPage(page, replyInfo));
    }

    /**
     * 获取具体的帖子回复信息
     * @param postId
     * @return
     */
    @GetMapping("/list/{id}")
    public R replyListByPostId(@PathVariable(value = "id") Integer postId) {
        PostInfo postInfo = postInfoService.getById(postId);
        postInfoService.update(Wrappers.<PostInfo>lambdaUpdate().set(PostInfo::getPageviews, postInfo.getPageviews() + 1).eq(PostInfo::getId, postId));
        return R.ok(replyInfoService.replyListByPostId(postId));
    }

    /**
     * 添加回复信息
     * @param replyInfo
     * @return
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public R save(ReplyInfo replyInfo) {
        String check = this.contentCheck(replyInfo.getContent());
        if (StrUtil.isNotEmpty(check)) {
            return R.error(500, check);
        } {
            // 获取贴子信息
            PostInfo postInfo = postInfoService.getById(replyInfo.getPostId());
            // 获取回复人信息
            User user = userService.getById(replyInfo.getUserId());
            List<MessageInfo> messageInfoList = new ArrayList<>();
            List<CollectInfo> collectInfoList = collectInfoService.list(Wrappers.<CollectInfo>lambdaQuery().eq(CollectInfo::getPostId, replyInfo.getPostId()).eq(CollectInfo::getDeleteFlag, 0));
            String message = "您收藏的贴子《"+postInfo.getTitle()+"》，"+user.getUsername()+"进行了回复，快去看看吧";
            for (CollectInfo collectInfo : collectInfoList) {
                messageInfoList.add(new MessageInfo(collectInfo.getUserId(), message, DateUtil.formatDateTime(new Date()), 0));
            }

            String message1 = "您的贴子《"+postInfo.getTitle()+"》，"+user.getUsername()+"进行了回复，快去看看吧";
            MessageInfo messageInfo = new MessageInfo(postInfo.getUserId(), message1, DateUtil.formatDateTime(new Date()), 0);

            messageInfoList.add(messageInfo);
            messageInfoService.saveBatch(messageInfoList);
            replyInfo.setDeleteFlag(0);
            replyInfo.setSendCreate(DateUtil.formatDateTime(new Date()));
            return R.ok(replyInfoService.save(replyInfo));
        }
    }

    /**
     * 删除回复信息
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(replyInfoService.update(Wrappers.<ReplyInfo>lambdaUpdate().set(ReplyInfo::getDeleteFlag, 1)
                .in(ReplyInfo::getId, ids)));
    }

    /**
     * 规范检查
     * @param content
     */
    public String contentCheck(String content) {
        String result = "";
        List<SensitiveInfo> sensitiveInfoList = sensitiveInfoService.list(Wrappers.<SensitiveInfo>lambdaQuery().eq(SensitiveInfo::getDeleteFlag, 0));
        for (SensitiveInfo item : sensitiveInfoList) {
            if (content.contains(item.getKeyName())) {
                result = item.getKeyName() + "使用不规范，请更改";
            }
        }
        return result;
    }

}
