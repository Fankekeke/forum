package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.FocusInfo;
import cc.mrbird.febs.cos.dao.FocusInfoMapper;
import cc.mrbird.febs.cos.service.IFocusInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class FocusInfoServiceImpl extends ServiceImpl<FocusInfoMapper, FocusInfo> implements IFocusInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> focusInfoByPage(Page page, FocusInfo focusInfo) {
        return baseMapper.focusInfoByPage(page, focusInfo);
    }

    @Override
    public List<LinkedHashMap<String, Object>> focusInfoByUser(Integer userId) {
        return baseMapper.focusInfoByUser(userId);
    }
}
