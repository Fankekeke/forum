package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.CollectInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface CollectInfoMapper extends BaseMapper<CollectInfo> {

    // 获取收藏贴子列表
    IPage<LinkedHashMap<String, Object>> collectInfoByPage(Page page, @Param("collectInfo") CollectInfo collectInfo);

    // 获取收藏贴子列表
    List<LinkedHashMap<String, Object>> collectInfoByUser(@Param("userId") Integer userId);
}
