package com.atguigu.gulimall.pms.dao;

import com.atguigu.gulimall.pms.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 属性&属性分组关联
 * 
 * @author Jesse
 * @email Jesse@atguigu.com
 * @date 2019-08-01 21:54:13
 */
@Mapper
@Repository
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {
	
}
