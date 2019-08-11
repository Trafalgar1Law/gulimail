package com.atguigu.gulimall.pms.feign;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuSaleInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author ：Jesse
 * @ProjectName: gulimall-pms
 * @date ：Created in 2019/8/6
 * @description：远程调用sms的feign
 */
@FeignClient("gulimall-sms")
public interface SmsSkuSaleInfoFService {

    @PostMapping("sms/skubounds/saleInfo/save")
    public Resp<Object> saveSkuSaleInfos(@RequestBody List<SkuSaleInfoTo> skuSaleInfoTo);

}
