package com.ijson.blog.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.dao.ConfigDao;
import com.ijson.blog.dao.entity.ConfigEntity;
import com.ijson.blog.model.Constant;
import com.ijson.blog.service.WebSiteService;
import com.ijson.blog.service.model.info.TencentInfo;
import com.ijson.blog.service.model.info.WebSiteInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/23 11:29 AM
 */
@Slf4j
@Service
public class WebSiteServiceImpl implements WebSiteService {

    @Autowired
    private ConfigDao configDao;

    @CachePut(value = "site")
    @Override
    public ConfigEntity updateWebSite(WebSiteInfo post) {
        ConfigEntity entity = configDao.findType(Constant.ConfigType.site);
        if (Objects.isNull(entity)) {
            entity = new ConfigEntity();
            entity.setType(Constant.ConfigType.site);
        }
        entity.setRegRoleId(post.getRegRoleId());
        entity.setSiteName(post.getSiteName());
        return configDao.updateWebSite(entity);
    }

    @CachePut(value = "site")
    @Override
    public ConfigEntity updateTencent(TencentInfo info) {
        ConfigEntity entity = configDao.findType(Constant.ConfigType.tencent);
        if (Objects.isNull(entity)) {
            entity = new ConfigEntity();
            entity.setType(Constant.ConfigType.tencent);
        }
        entity.setAppId(info.getAppId());
        entity.setAppKey(info.getAppKey());
        entity.setQqCallBackUrl(info.getQqCallBackUrl());
        return configDao.updateTencent(entity);
    }


    @CachePut(value = "site")
    @Override
    public ConfigEntity updateSwitch(String type) {

        log.info("updateSwitch {}", type);
        ConfigEntity configDaoType = configDao.findType(Constant.ConfigType.switchType);
        if (Objects.isNull(configDaoType)) {
            configDaoType = new ConfigEntity();
            configDaoType.setType(Constant.ConfigType.switchType);
        }
        switch (type) {
            case ConfigEntity.Fields.openReg:
                configDaoType.setOpenReg(!configDaoType.isOpenReg());
                break;
            case ConfigEntity.Fields.openCmt:
                configDaoType.setOpenCmt(!configDaoType.isOpenCmt());
                break;
            case ConfigEntity.Fields.openQQLogin:
                configDaoType.setOpenQQLogin(!configDaoType.isOpenQQLogin());
                break;
            default:
                type = null;
        }
        if (Strings.isNullOrEmpty(type)) {
            log.error("开关更新异常");
            return null;
        }
        return configDao.updateSwitch(configDaoType);
    }


    @CachePut(value = "site")
    @Override
    public ConfigEntity findAllConfig() {
        ConfigEntity entity = new ConfigEntity();

        List<ConfigEntity> allType = configDao.findAllType();

        allType.forEach(k -> {

            if (k.getType() == Constant.ConfigType.site) {
                entity.setSiteName(k.getSiteName());
                entity.setRegRoleId(k.getRegRoleId());
            }


            if (k.getType() == Constant.ConfigType.switchType) {
                entity.setOpenReg(k.isOpenReg());
                entity.setOpenCmt(k.isOpenCmt());
                entity.setOpenQQLogin(k.isOpenQQLogin());
            }

            if (k.getType() == Constant.ConfigType.tencent) {
                entity.setAppId(k.getAppId());
                entity.setAppKey(k.getAppKey());
                entity.setQqCallBackUrl(k.getQqCallBackUrl());
            }

        });

        return entity;
    }


}
