package com.ijson.blog.service;

import com.ijson.blog.dao.entity.ConfigEntity;
import com.ijson.blog.service.model.info.TencentInfo;
import com.ijson.blog.service.model.info.WebSiteInfo;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/23 11:29 AM
 */
public interface WebSiteService {
    ConfigEntity updateWebSite(WebSiteInfo post);

    ConfigEntity updateSwitch(String type);

    ConfigEntity findAllConfig();

    ConfigEntity updateTencent(TencentInfo post);

}
