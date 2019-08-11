package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.pms.dao.AttrAttrgroupRelationDao;

import com.atguigu.gulimall.pms.dao.AttrDao;
import com.atguigu.gulimall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.vo.AttrGroupWithAttrsVo;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import com.atguigu.gulimall.pms.dao.AttrGroupDao;
import com.atguigu.gulimall.pms.entity.AttrGroupEntity;
import com.atguigu.gulimall.pms.service.AttrGroupService;

import java.util.ArrayList;
import java.util.List;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryPageAttrGroupsByCatId(QueryCondition queryCondition, Integer catId) {
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id",catId);

        IPage<AttrGroupEntity> page = new Query<AttrGroupEntity>().getPage(queryCondition);
        IPage<AttrGroupEntity> entityIPage = baseMapper.selectPage(page, queryWrapper);
        //所有分组
        List<AttrGroupEntity> groupEntities = entityIPage.getRecords();
        //查出每一个分组的所有属性
        List<AttrGroupWithAttrsVo> list = new ArrayList<>(groupEntities.size());

        for (AttrGroupEntity groupEntity : groupEntities) {
            Long attrGroupId = groupEntity.getAttrGroupId();
            //获取当前分组的所有属性
            AttrGroupWithAttrsVo attrEntities = this.queryAttrGroupWithAttrs(attrGroupId);


            list.add(attrEntities);
        }

        PageVo pageVo = new PageVo(list,entityIPage.getTotal(),entityIPage.getSize(),entityIPage.getCurrent());
        return pageVo;
    }

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    private AttrDao attrDao;
    @Override
    public AttrGroupWithAttrsVo queryAttrGroupWithAttrs(Long attrGroupId) {
        AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
        //查出当前分组的信息
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_group_id",attrGroupId);
        AttrGroupEntity attrGroupEntity = baseMapper.selectOne(queryWrapper);
        BeanUtils.copyProperties(attrGroupEntity,attrGroupWithAttrsVo);

        //查出当前分组的关联关系
        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("attr_group_id",attrGroupId);
        List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationDao.selectList(queryWrapper1);

        attrGroupWithAttrsVo.setRelations(relationEntities);

        //查出当前分组所有属性
        ArrayList<Object> attrIdList = new ArrayList<>();

        if (relationEntities!=null&&relationEntities.size()>0){
            relationEntities.forEach(relationEntity -> {
                Long attrId=relationEntity.getAttrId();
                attrIdList.add(attrId);
            });

            List<AttrEntity> attrEntities = attrDao.selectList(new QueryWrapper<AttrEntity>().in("attr_id", attrIdList));
            attrGroupWithAttrsVo.setAttrEntities(attrEntities);
        }


        return attrGroupWithAttrsVo;
    }

}