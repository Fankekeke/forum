package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.service.CacheService;
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
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/post-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PostInfoController {

    private final IPostInfoService postInfoService;

    private final ISensitiveInfoService sensitiveInfoService;

    private final ICollectInfoService collectInfoService;

    private final IMessageInfoService messageInfoService;

    private final IFocusInfoService focusInfoService;

    private final UserService userService;

    private final CacheService cacheService;

    /**
     * 分页获取系统用户信息
     * @param page
     * @param user
     * @return
     */
    @GetMapping("/user/page")
    public R userPage(Page page, User user) {
        return R.ok(userService.page(page, Wrappers.<User>lambdaQuery()
                .like(StrUtil.isNotEmpty(user.getUsername()), User::getUsername, user.getUsername())
                .eq(StrUtil.isNotEmpty(user.getStatus()), User::getStatus, user.getStatus())
                .eq(User::getUserType, 2)));
    }

    /**
     * 用户状态更改
     * @param flag
     * @param userId
     * @return
     */
    @PostMapping("/user/audit")
    public R userStatusAudit(Integer flag, Long userId) throws Exception {
        userService.update(Wrappers.<User>lambdaUpdate().set(User::getStatus, flag).eq(User::getUserId, userId));
        User user = userService.getById(userId);
        // 重新将用户信息，用户角色信息，用户权限信息 加载到 redis中
        cacheService.saveUser(user.getUsername());
        cacheService.saveRoles(user.getUsername());
        cacheService.savePermissions(user.getUsername());
        return R.ok(true);
    }

    /**
     * 获取关注信息
     * @param userId
     * @param postId
     * @return
     */
    @GetMapping("/collcet")
    public R collectByUser(Long userId, Integer postId) {
        PostInfo postInfo = postInfoService.getById(postId);
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("collect", collectInfoService.count(Wrappers.<CollectInfo>lambdaQuery().eq(CollectInfo::getUserId, userId).eq(CollectInfo::getPostId, postId).eq(CollectInfo::getDeleteFlag, 0)));
                put("focus", focusInfoService.count(Wrappers.<FocusInfo>lambdaQuery().eq(FocusInfo::getUserId, userId).eq(FocusInfo::getCollectUserId, postInfo.getUserId()).eq(FocusInfo::getDeleteFlag, 0)));
            }
        };
        return R.ok(result);
    }

    /**
     * 获取模块下的贴子
     * @param tagId
     * @return
     */
    @GetMapping("/tag/{tagId}")
    public R getPostByTag(@PathVariable("tagId") Integer tagId) {
        return R.ok(postInfoService.getPostByTag(tagId));
    }

    /**
     * 获取贴子详细信息
     * @param postId
     * @return
     */
    @GetMapping("/{postId}")
    public R postDetail(@PathVariable("postId") Integer postId) {
        return R.ok(postInfoService.postDetail(postId));
    }

    /**
     * 分页获取帖子信息
     * @param page
     * @param postInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, PostInfo postInfo) {
        return R.ok(postInfoService.postInfoByPage(page, postInfo));
    }

    /**
     * 模糊查询帖子信息
     * @return
     */
    @GetMapping("/list/{key}")
    public R list(@PathVariable("key") String key) {
        return R.ok(postInfoService.postByKey(key));
    }

    /**
     * 推荐贴子
     * @param userId
     * @return
     */
    @GetMapping("/recommend/{userId}")
    public R recommend(@PathVariable("userId") Integer userId) {
        Integer tagIds = -1;
        List<Long> collectUserIdList = new ArrayList<>();
        // 查询收藏贴子类型
        List<CollectInfo> collectInfoList = collectInfoService.list(Wrappers.<CollectInfo>lambdaQuery().eq(CollectInfo::getUserId, userId).eq(CollectInfo::getDeleteFlag, 0));
        if (collectInfoList.size() != 0) {
            PostInfo postInfo = postInfoService.getById(collectInfoList.get(0).getPostId());
            tagIds = Integer.valueOf(postInfo.getTagIds().split(",")[0]);
        }
        // 查询关注人
        List<FocusInfo> focusInfoList = focusInfoService.list(Wrappers.<FocusInfo>lambdaQuery().eq(FocusInfo::getUserId, userId));
        for (FocusInfo focusInfo : focusInfoList) {
            collectUserIdList.add(focusInfo.getCollectUserId());
        }
        return R.ok(postInfoService.recommend(tagIds, collectUserIdList));
    }

    /**
     * 添加帖子信息
     * @param postInfo
     * @return
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public R save(PostInfo postInfo) {
        String check = this.contentCheck(postInfo.getContent());
        if (StrUtil.isNotEmpty(check)) {
            return R.error(500, check);
        } else {
            // 获取发帖人信息
            User user = userService.getById(postInfo.getUserId());
            // 消息通知
            List<FocusInfo> focusInfoList = focusInfoService.list(Wrappers.<FocusInfo>lambdaQuery().eq(FocusInfo::getCollectUserId, postInfo.getUserId()).eq(FocusInfo::getDeleteFlag, 0));
            List<MessageInfo> messageInfoList = new ArrayList<>();
            String message = "您关注的"+user.getUsername()+"发布了新贴子 《"+postInfo.getTitle()+"》，快去回复吧";
            for (FocusInfo focusInfo : focusInfoList) {
                messageInfoList.add(new MessageInfo(focusInfo.getUserId(), message, DateUtil.formatDateTime(new Date()), 0));
            }
            messageInfoService.saveBatch(messageInfoList);
            postInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
            postInfo.setDeleteFlag(0);
            postInfo.setPageviews(0);
            return R.ok(postInfoService.save(postInfo));
        }
    }

    /**
     * 修改帖子信息
     * @param postInfo
     * @return
     */
    @PutMapping
    public R edit(PostInfo postInfo) {
        String check = this.contentCheck(postInfo.getContent());
        if (StrUtil.isNotEmpty(check)) {
            return R.error(500, check);
        } else {
            return R.ok(postInfoService.updateById(postInfo));
        }
    }

    /**
     * 删除帖子信息
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(postInfoService.update(Wrappers.<PostInfo>lambdaUpdate().set(PostInfo::getDeleteFlag, 1).in(PostInfo::getId, ids)));
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
