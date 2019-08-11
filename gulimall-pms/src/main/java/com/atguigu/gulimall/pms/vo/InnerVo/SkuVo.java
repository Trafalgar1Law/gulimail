package com.atguigu.gulimall.pms.vo.InnerVo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuVo {
    /**
     * "skuName": "", //sku名字
     * "skuDesc": "", //sku描述
     * "skuTitle" : "", //sku标题
     * "skuSubtitle" :"", //sku副标题
     * "weight" : 0, //sku重量
     * "price": 0, //商品价格
     * "images": ["string"], //sku图片
     */
//sku的基本信息
    private String skuName;

    private String skuDesc;

    private String skuTitle;

    private String skuSubtitle;

    private BigDecimal price;

    private BigDecimal weight;

    private String[] images;
//当前sku对应的销售属性
    private List<SaleAttrVo> saleAttrs;

    /**
     * "buyBounds": 0, //赠送的购物积分
     * "growBounds": 0, //赠送的成长积分
     * "work": [0,1,1,0], //积分生效情况
     * "fullCount": 0, //满几件
     * "discount": 0, //打几折
     * "ladderAddOther": 0, //阶梯价格是否可以与其他优惠叠加
     * "fullPrice": 0, //满多少
     * "reducePrice": 0, //减多少
     * "fullAddOther": 0, //满减是否可以叠加其他优惠
     */

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
