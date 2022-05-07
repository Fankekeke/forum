package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.SensitiveInfo;
import cc.mrbird.febs.cos.entity.TagInfo;
import cc.mrbird.febs.cos.service.ISensitiveInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
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
@RequestMapping("/cos/sensitive-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SensitiveInfoController {

    private final ISensitiveInfoService sensitiveInfoService;

    /**
     * 获取敏感词列表
     * @return
     */
    @GetMapping("/list")
    public R List() {
        return R.ok(sensitiveInfoService.list(Wrappers.<SensitiveInfo>lambdaQuery().eq(SensitiveInfo::getDeleteFlag, 0)));
    }

    /**
     * 分页查询敏感词列表
     * @param page
     * @param sensitiveInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, SensitiveInfo sensitiveInfo) {
        return R.ok(sensitiveInfoService.sensitiveByPage(page, sensitiveInfo));
    }

    /**
     * 更新敏感词列表
     * @return
     */
    @PostMapping("/list")
    public R saveList(String keys) {
        List<SensitiveInfo> sensitiveInfoList = JSONUtil.toList(keys, SensitiveInfo.class);
        return R.ok(sensitiveInfoService.saveOrUpdateBatch(sensitiveInfoList));
    }

    /**
     * 添加敏感词列表
     * @param sensitiveInfo
     * @return
     */
    @PostMapping
    public R save(SensitiveInfo sensitiveInfo) {
        sensitiveInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(sensitiveInfoService.save(sensitiveInfo));
    }

    /**
     * 修改敏感词列表
     * @param sensitiveInfo
     * @return
     */
    @PutMapping
    public R edit(SensitiveInfo sensitiveInfo) {
        return R.ok(sensitiveInfoService.updateById(sensitiveInfo));
    }

    /**
     * 删除敏感词列表
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(sensitiveInfoService.update(Wrappers.<SensitiveInfo>lambdaUpdate().set(SensitiveInfo::getDeleteFlag, 1)
                .in(SensitiveInfo::getId, ids)));
    }
}
