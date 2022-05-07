package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.PostInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface PostInfoMapper extends BaseMapper<PostInfo> {

    // 分页获取帖子信息
    IPage<LinkedHashMap<String, Object>> postInfoByPage(Page page, @Param("postInfo") PostInfo postInfo);

    // 获取模块下的贴子
    List<LinkedHashMap<String, Object>> getPostByTag(@Param("tagId") Integer tagId);

    // 获取贴子详细信息
    LinkedHashMap<String, Object> postDetail(@Param("postId") Integer postId);

    // 模糊查询帖子信息
    List<LinkedHashMap<String, Object>> postByKey(@Param("key") String key);

    // 推荐贴子
    List<LinkedHashMap<String, Object>> recommend(@Param("tagId") Integer tagId, @Param("collectUserIds") String collectUserIds);
}
