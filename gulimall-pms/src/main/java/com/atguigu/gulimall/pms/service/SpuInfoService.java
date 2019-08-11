package com.atguigu.gulimall.pms.service;

import com.atguigu.gulimall.pms.vo.InnerVo.BaseAttrVo;
import com.atguigu.gulimall.pms.vo.InnerVo.SkuVo;
import com.atguigu.gulimall.pms.vo.SpuAllSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.pms.entity.SpuInfoEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import java.util.List;


/**
 * spu信息
 *
 * @author Jesse
 * @email Jesse@atguigu.com
 * @date 2019-08-01 21:54:13
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo queryPageByCatId(QueryCondition queryCondition, Integer catId);

    void spuBigSaveAll(SpuAllSaveVo spuAllSaveVo);

    void saveSpuInfoImages(Long spuId, String[] spuImages);

    Long saveSpuBaseInfo(SpuAllSaveVo spuAllSaveVo);

    void saveSkuInfos(Long spuId, List<SkuVo> skus);

    void saveSpuBaseAttrs(Long spuId, List<BaseAttrVo> baseAttrs);

    void updateSpuStatus(Long spuId, Integer status);

}

