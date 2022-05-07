package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.SensitiveInfo;
import cc.mrbird.febs.cos.dao.SensitiveInfoMapper;
import cc.mrbird.febs.cos.service.ISensitiveInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class SensitiveInfoServiceImpl extends ServiceImpl<SensitiveInfoMapper, SensitiveInfo> implements ISensitiveInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> sensitiveByPage(Page page, SensitiveInfo sensitiveInfo) {
        return baseMapper.sensitiveByPage(page, sensitiveInfo);
    }
}
