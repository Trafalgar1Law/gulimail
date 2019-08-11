package com.atguigu.gulimall.commons.to;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ：Jesse
 * @ProjectName: gulimall
 * @date ：Created in 2019/8/11
 * @description：
 */
@Data
public class SkuStockVo {
    /**
     * 仓库id
     */
    @ApiModelProperty(name = "skuId",value = "仓库id")
    private Long skuId;
    /**
     * 库存数
     */
    @ApiModelProperty(name = "stock",value = "库存数")
    private Integer stock;
}
