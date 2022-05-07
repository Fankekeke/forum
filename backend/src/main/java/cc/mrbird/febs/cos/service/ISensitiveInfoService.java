package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.SensitiveInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface ISensitiveInfoService extends IService<SensitiveInfo> {

    // 分页查询敏感词列表
    IPage<LinkedHashMap<String, Object>> sensitiveByPage(Page page, SensitiveInfo sensitiveInfo);
}
