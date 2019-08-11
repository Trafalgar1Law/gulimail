package com.atguigu.gulimall.pms.feign;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.es.EsSkuVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author ：Jesse
 * @ProjectName: gulimall
 * @date ：Created in 2019/8/11
 * @description：远程调用search的
 */
@FeignClient("gulimall-search")
public interface EsFeignService {
    @PostMapping("/es/spu/up")
    public Resp<Object> saveUp(@RequestBody List<EsSkuVo> vo);
}
