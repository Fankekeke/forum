package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.TagInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface TagInfoMapper extends BaseMapper<TagInfo> {

    // 分页获取tag数据
    IPage<LinkedHashMap<String, Object>> tagInfoByPage(Page page, @Param("tagInfo") TagInfo tagInfo);
}
