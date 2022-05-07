package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.CollectInfo;
import cc.mrbird.febs.cos.service.ICollectInfoService;
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
@RequestMapping("/cos/collect-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectInfoController {

    private final ICollectInfoService collectInfoService;

    /**
     * 获取收藏贴子列表
     * @param page
     * @param collectInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, CollectInfo collectInfo) {
        return R.ok(collectInfoService.collectInfoByPage(page, collectInfo));
    }

    /**
     * 获取收藏贴子列表
     * @return
     */
    @GetMapping("/list/{userId}")
    public R list(@PathVariable("userId") Integer userId) {
        return R.ok(collectInfoService.collectInfoByUser(userId));
    }

    /**
     * 添加收藏信息
     * @param collectInfo
     * @return
     */
    @PostMapping
    public R save(CollectInfo collectInfo) {
        Integer count = collectInfoService.count(Wrappers.<CollectInfo>lambdaQuery().eq(CollectInfo::getPostId, collectInfo.getPostId())
                .eq(CollectInfo::getUserId, collectInfo.getUserId()));
        if (count > 0) {
         Boolean result = collectInfoService.update(Wrappers.<CollectInfo>lambdaUpdate().set(CollectInfo::getDeleteFlag, collectInfo.getDeleteFlag())
                 .eq(CollectInfo::getPostId, collectInfo.getPostId()).eq(CollectInfo::getUserId, collectInfo.getUserId()));
            return R.ok(result);
        } else {
            collectInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
            return R.ok(collectInfoService.save(collectInfo));
        }
    }

    /**
     * 删除收藏信息
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(collectInfoService.update(Wrappers.<CollectInfo>lambdaUpdate().set(CollectInfo::getDeleteFlag, 1).in(CollectInfo::getId, ids)));
    }


}
