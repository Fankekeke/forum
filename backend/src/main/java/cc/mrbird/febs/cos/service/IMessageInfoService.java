package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.MessageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IMessageInfoService extends IService<MessageInfo> {

    // 分页查询消息信息
    IPage<LinkedHashMap<String, Object>> messageInfoByPage(Page page, MessageInfo messageInfo);
}
