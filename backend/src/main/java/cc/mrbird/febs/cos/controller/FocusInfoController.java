package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.CollectInfo;
import cc.mrbird.febs.cos.entity.FocusInfo;
import cc.mrbird.febs.cos.service.IFocusInfoService;
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
@RequestMapping("/cos/focus-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FocusInfoController {

    private final IFocusInfoService focusInfoService;

    /**
     * 分页查询关注人信息
     * @param page
     * @param focusInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, FocusInfo focusInfo) {
        return R.ok(focusInfoService.focusInfoByPage(page, focusInfo));
    }

    /**
     * 分页查询关注人信息
     * @param userId
     * @return
     */
    @GetMapping("/list/{userId}")
    public R list(@PathVariable("userId") Integer userId) {
        return R.ok(focusInfoService.focusInfoByUser(userId));
    }

    /**
     * 添加关注信息
     * @param focusInfo
     * @return
     */
    @PostMapping
    public R save(FocusInfo focusInfo) {
        Integer count = focusInfoService.count(Wrappers.<FocusInfo>lambdaQuery().eq(FocusInfo::getCollectUserId, focusInfo.getCollectUserId())
                .eq(FocusInfo::getUserId, focusInfo.getUserId()));
        if (count > 0) {
            Boolean result = focusInfoService.update(Wrappers.<FocusInfo>lambdaUpdate().set(FocusInfo::getDeleteFlag, focusInfo.getDeleteFlag())
                    .eq(FocusInfo::getUserId, focusInfo.getUserId()).eq(FocusInfo::getCollectUserId, focusInfo.getCollectUserId()));
            return R.ok(result);
        } else {
            focusInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
            return R.ok(focusInfoService.save(focusInfo));
        }
    }

    /**
     * 删除关注
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(focusInfoService.update(Wrappers.<FocusInfo>lambdaUpdate().set(FocusInfo::getDeleteFlag, 1).in(FocusInfo::getId, ids)));
    }

}
