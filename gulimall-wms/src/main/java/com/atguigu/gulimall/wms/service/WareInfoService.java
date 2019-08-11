package com.atguigu.gulimall.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.wms.entity.WareInfoEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;


/**
 * 仓库信息
 *
 * @author Jesse
 * @email Jesse@atguigu.com
 * @date 2019-08-02 22:26:30
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageVo queryPage(QueryCondition params);
}

