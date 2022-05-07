package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.ReplyInfo;
import cc.mrbird.febs.cos.entity.TagInfo;
import cc.mrbird.febs.cos.service.ITagInfoService;
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
@RequestMapping("/cos/tag-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagInfoController {

    private final ITagInfoService tagInfoService;

    /**
     * 获取tag数据
     * @return
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(tagInfoService.list(Wrappers.<TagInfo>lambdaQuery().eq(TagInfo::getDeleteFlag, 0)));
    }

    /**
     * 分页获取tag数据
     * @param page
     * @param tagInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, TagInfo tagInfo) {
        return R.ok(tagInfoService.tagInfoByPage(page, tagInfo));
    }

    /**
     * 添加tag数据
     * @return
     */
    @PostMapping
    public R save(TagInfo tagInfo) {
        tagInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(tagInfoService.save(tagInfo));
    }

    /**
     * 修改tag数据
     * @param tagInfo
     * @return
     */
    @PutMapping
    public R edit(TagInfo tagInfo) {
        return R.ok(tagInfoService.updateById(tagInfo));
    }

    /**
     * 删除tag数据
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(tagInfoService.update(Wrappers.<TagInfo>lambdaUpdate().set(TagInfo::getDeleteFlag, 1)
                .in(TagInfo::getId, ids)));
    }
}
