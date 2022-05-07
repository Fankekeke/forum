package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.SensitiveInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface SensitiveInfoMapper extends BaseMapper<SensitiveInfo> {

    // 分页查询敏感词列表
    IPage<LinkedHashMap<String, Object>> sensitiveByPage(Page page, @Param("sensitiveInfo") SensitiveInfo sensitiveInfo);
}
