package com.atguigu.gulimall.pms.vo;

import com.atguigu.gulimall.pms.entity.SpuInfoEntity;
import com.atguigu.gulimall.pms.vo.InnerVo.BaseAttrVo;
import com.atguigu.gulimall.pms.vo.InnerVo.SkuVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

//spu全量信息【spu基本信息，spu的基本属性，sku的信息，sku的促销信息】
@Data
public class SpuAllSaveVo extends SpuInfoEntity {
    //当前spu的所有基本属性
    private List<BaseAttrVo> baseAttrs;

    private List<SkuVo> skus;

    private String[] spuImages;

}

