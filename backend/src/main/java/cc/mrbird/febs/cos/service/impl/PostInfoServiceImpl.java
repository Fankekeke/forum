package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.PostInfo;
import cc.mrbird.febs.cos.dao.PostInfoMapper;
import cc.mrbird.febs.cos.service.IPostInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class PostInfoServiceImpl extends ServiceImpl<PostInfoMapper, PostInfo> implements IPostInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> postInfoByPage(Page page, PostInfo postInfo) {
        return baseMapper.postInfoByPage(page, postInfo);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getPostByTag(Integer tagId) {
        return baseMapper.getPostByTag(tagId);
    }

    @Override
    public LinkedHashMap<String, Object> postDetail(Integer postId) {
        return baseMapper.postDetail(postId);
    }

    @Override
    public List<LinkedHashMap<String, Object>> postByKey(String key) {
        return baseMapper.postByKey(key);
    }

    @Override
    public List<LinkedHashMap<String, Object>> recommend(Integer tagId, List<Long> collectUserIds) {
        return baseMapper.recommend(tagId, StringUtils.join(collectUserIds.toArray(), ","));
    }
}
