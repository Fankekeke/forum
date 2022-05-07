package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.CollectInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface ICollectInfoService extends IService<CollectInfo> {

    // 获取收藏贴子列表
    IPage<LinkedHashMap<String, Object>> collectInfoByPage(Page page, CollectInfo collectInfo);

    // 获取收藏贴子列表
    List<LinkedHashMap<String, Object>> collectInfoByUser(Integer userId);
}
