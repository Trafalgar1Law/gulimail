package com.atguigu.gulimall.commons.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ：Jesse
 * @ProjectName: gulimall-core
 * @date ：Created in 2019/8/6
 * @description：Sku的营销信息
 */
@Data
public class SkuSaleInfoTo {
   //要有skuId,不然无法知道是哪个sku的
    private Long skuId;

    private BigDecimal buyBounds;
    private BigDecimal growBounds;
    private Integer[] work;

    private Integer fullCount;
    private BigDecimal discount;
    private Integer ladderAddOther;

    //sku的满减表
    private BigDecimal fulPrice;
    private BigDecimal reducePrice;
    private Integer fullAddOther;

}
