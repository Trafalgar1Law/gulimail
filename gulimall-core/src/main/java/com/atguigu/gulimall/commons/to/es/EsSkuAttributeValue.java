package com.atguigu.gulimall.commons.to.es;

import lombok.Data;

/**
 * @author ：Jesse
 * @ProjectName: gulimall
 * @date ：Created in 2019/8/10
 * @description：
 */
@Data
public class EsSkuAttributeValue {


    private Long id;
    private Long productAttributeId;

    private String value;
    private Integer type;
    private String name;
    private Long spuId;
}
