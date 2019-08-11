package com.atguigu.gulimall.pms.vo;

import com.atguigu.gulimall.pms.entity.AttrEntity;
import lombok.Data;

@Data
public class AttrSaveVo extends AttrEntity {
    //额外多一个分组id
    private Long attrGroupId;

}
