package com.atguigu.gulimall.pms.vo;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

@Data
public class AttrRelationDeleteVo {
    private Long attrGroupId;
    private Long attrId;

}
