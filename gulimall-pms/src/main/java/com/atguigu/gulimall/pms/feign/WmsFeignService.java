package com.atguigu.gulimall.pms.feign;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author ：Jesse
 * @ProjectName: gulimall
 * @date ：Created in 2019/8/11
 * @description：
 */
@FeignClient("gulimall-wms")
public interface WmsFeignService {
    @GetMapping("/wms/waresku/sku")
    public Resp<List<SkuStockVo>> skuInfoById(Long skuId);
}
