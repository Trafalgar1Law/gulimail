package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.pms.vo.AttrSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import com.atguigu.gulimall.pms.dao.AttrDao;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryAllBaseAttrByCatId(QueryCondition queryCondition, Integer catId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id", catId)
                .eq("attr_type", 1);

        IPage<AttrEntity> page = new Query<AttrEntity>().getPage(queryCondition);

        IPage<AttrEntity> attrEntityIPage = baseMapper.selectPage(page, queryWrapper);
        PageVo pageVo = new PageVo(attrEntityIPage);
        return pageVo;
    }

    @Override
    public PageVo queryAllSaleAttrByCatId(QueryCondition queryCondition, Integer catId) {
        IPage<AttrEntity> attrEntityIPage = baseMapper.selectPage(
                new Query<AttrEntity>().getPage(queryCondition),
                new QueryWrapper<AttrEntity>().eq("catelog_id", catId)
                        .eq("attr_type", 0)
        );
        return new PageVo(attrEntityIPage);
    }

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Transactional
    @Override
    public void saveAttrAndRelation(AttrSaveVo attr) {
        //保存属性分为两步
        //1 保存在属性表里
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        baseMapper.insert(attrEntity);

        //2 建立关联关系
        Long attrId = attrEntity.getAttrId();
        Long attrGroupId = attr.getAttrGroupId();

        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
        attrAttrgroupRelationEntity.setAttrId(attrId);

        attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        //测试回滚
        //int i=10/0;
    }

}