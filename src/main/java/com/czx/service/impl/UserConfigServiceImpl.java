package com.czx.service.impl;

import com.czx.mapper.UserConfigMapper;
import com.czx.pojo.UserConfig;
import com.czx.service.UserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserConfigServiceImpl implements UserConfigService {
    
    @Autowired
    private UserConfigMapper userConfigMapper;
    
    @Override
    public UserConfig getConfigByUserId(Integer userId) {
        return userConfigMapper.findByUserId(userId);
    }
    
    @Override
    @Transactional
    public boolean saveOrUpdateConfig(UserConfig userConfig) {
        UserConfig existingConfig = userConfigMapper.findByUserId(userConfig.getUserId());
        
        if (existingConfig != null) {
            // 更新现有配置
            existingConfig.setKuajingmaihuoCookie(userConfig.getKuajingmaihuoCookie());
            existingConfig.setAgentsellerCookie(userConfig.getAgentsellerCookie());
            existingConfig.setMallid(userConfig.getMallid());
            existingConfig.setUpdatedAt(LocalDateTime.now());
            
            // 如果配置有变化，清除缓存数据
            if (!existingConfig.getKuajingmaihuoCookie().equals(userConfig.getKuajingmaihuoCookie()) ||
                !existingConfig.getAgentsellerCookie().equals(userConfig.getAgentsellerCookie()) ||
                !existingConfig.getMallid().equals(userConfig.getMallid())) {
                existingConfig.setParentMsgId(null);
                existingConfig.setParentMsgTimestamp(null);
                existingConfig.setToolId(null);
            }
            
            return userConfigMapper.update(existingConfig) > 0;
        } else {
            // 创建新配置
            userConfig.setCreatedAt(LocalDateTime.now());
            userConfig.setUpdatedAt(LocalDateTime.now());
            return userConfigMapper.insert(userConfig) > 0;
        }
    }
    
    @Override
    @Transactional
    public boolean deleteConfig(Integer userId) {
        return userConfigMapper.deleteByUserId(userId) > 0;
    }
    
    @Override
    @Transactional
    public boolean updateCache(Integer userId, String parentMsgId, String parentMsgTimestamp, String toolId) {
        UserConfig config = userConfigMapper.findByUserId(userId);
        if (config != null) {
            config.setParentMsgId(parentMsgId);
            config.setParentMsgTimestamp(parentMsgTimestamp);
            config.setToolId(toolId);
            config.setUpdatedAt(LocalDateTime.now());
            return userConfigMapper.update(config) > 0;
        }
        return false;
    }
}
