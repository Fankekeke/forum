package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.MessageInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface MessageInfoMapper extends BaseMapper<MessageInfo> {

    // 分页查询消息信息
    IPage<LinkedHashMap<String, Object>> messageInfoByPage(Page page, @Param("messageInfo") MessageInfo messageInfo);
}
