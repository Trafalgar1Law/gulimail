package com.atguigu.gulimall.commons.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ：Jesse
 * @ProjectName: gulimall
 * @date ：Created in 2019/8/10
 * @description：检索时会带上sku的检索条件
 * 1 sku的基本信息
 * 2 sku的品牌，分类
 * 3 sku的检索属性信息
 */
@Data
public class EsSkuVo {

    private Long id;
    private Long brandId;
    private String brandName;

    private Long productCategoryId;
    private String productCategoryName;
    private String pic;
    private String name;
    private BigDecimal price;
    private Integer sale;
    private Integer stock;
    private Integer sort;

    //sku需要检索的属性
    private List<EsSkuAttributeValue> attributeValues;

}

