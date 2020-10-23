package com.ershijin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ershijin.config.security.bean.SecurityProperties;
import com.ershijin.model.PageResult;
import com.ershijin.model.dto.OnlineUserDTO;
import com.ershijin.util.PageUtil;
import com.ershijin.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 在线用户服务
 */
@Service
public class OnlineUserService {
    @Autowired
    private SecurityProperties properties;

    /**
     * 查询列表，带分页
     * @param keyword /
     * @param page /
     * @return /
     */
    public PageResult list(String keyword, IPage page){
        List<OnlineUserDTO> onlineUserDtos = list(keyword);
//        return PageUtil.toPage(
//                PageUtil.toPage(pageable.getPageNumber(),pageable.getPageSize(), onlineUserDtos),
//                onlineUserDtos.size()
//        );
        return new PageResult(onlineUserDtos.size(), onlineUserDtos);
    }
    /**
     * 查询全部数据，不分页
     * @param keyword /
     * @return /
     */
    public List<OnlineUserDTO> list(String keyword){
        List<String> keys = RedisUtils.scan(properties.getOnlineKey() + "*");
        Collections.reverse(keys);
        List<OnlineUserDTO> onlineUsers = new ArrayList<>();
        for (String key : keys) {
            OnlineUserDTO onlineUserDto = (OnlineUserDTO) RedisUtils.get(key);
            if(StringUtils.isNotBlank(keyword)){
                if(onlineUserDto.toString().contains(keyword)){
                    onlineUsers.add(onlineUserDto);
                }
            } else {
                onlineUsers.add(onlineUserDto);
            }
        }
//        onlineUsers.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUsers;
    }

    /**
     * 踢出用户
     * @param key /
     */
    public void kickOut(String key){
        key = properties.getOnlineKey() + key;
        RedisUtils.del(key);
    }
}
