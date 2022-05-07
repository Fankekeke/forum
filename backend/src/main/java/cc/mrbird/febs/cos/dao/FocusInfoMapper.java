package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.FocusInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface FocusInfoMapper extends BaseMapper<FocusInfo> {

    // 分页查询关注人信息
    IPage<LinkedHashMap<String, Object>> focusInfoByPage(Page page, @Param("focusInfo") FocusInfo focusInfo);

    // 查询关注人信息
    List<LinkedHashMap<String, Object>> focusInfoByUser(@Param("userId") Integer userId);
}
