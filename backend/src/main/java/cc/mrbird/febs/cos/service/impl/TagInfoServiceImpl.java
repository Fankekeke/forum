package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.TagInfo;
import cc.mrbird.febs.cos.dao.TagInfoMapper;
import cc.mrbird.febs.cos.service.ITagInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class TagInfoServiceImpl extends ServiceImpl<TagInfoMapper, TagInfo> implements ITagInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> tagInfoByPage(Page page, TagInfo tagInfo) {
        return baseMapper.tagInfoByPage(page, tagInfo);
    }
}
