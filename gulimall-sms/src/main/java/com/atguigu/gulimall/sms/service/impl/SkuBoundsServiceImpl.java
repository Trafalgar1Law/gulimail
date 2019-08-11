package com.atguigu.gulimall.sms.service.impl;

import com.atguigu.gulimall.commons.to.SkuSaleInfoTo;
import com.atguigu.gulimall.sms.dao.SkuFullReductionDao;
import com.atguigu.gulimall.sms.dao.SkuLadderDao;
import com.atguigu.gulimall.sms.entity.SkuFullReductionEntity;
import com.atguigu.gulimall.sms.entity.SkuLadderEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import com.atguigu.gulimall.sms.dao.SkuBoundsDao;
import com.atguigu.gulimall.sms.entity.SkuBoundsEntity;
import com.atguigu.gulimall.sms.service.SkuBoundsService;


@Service("skuBoundsService")
public class SkuBoundsServiceImpl extends ServiceImpl<SkuBoundsDao, SkuBoundsEntity> implements SkuBoundsService {
    @Autowired
    private SkuBoundsDao skuBoundsDao;
    @Autowired
    private SkuLadderDao skuLadderDao;
    @Autowired
    private SkuFullReductionDao skuFullReductionDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuBoundsEntity> page = this.page(
                new Query<SkuBoundsEntity>().getPage(params),
                new QueryWrapper<SkuBoundsEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public void saveSkuSaleInfoTo(List<SkuSaleInfoTo> skuSaleInfoTo) {
        if(skuSaleInfoTo!=null&&skuSaleInfoTo.size()>0){
            for (SkuSaleInfoTo saleInfoTo : skuSaleInfoTo) {
                //1 sku的积分信息的保存
                SkuBoundsEntity skuBoundsEntity = new SkuBoundsEntity();
                //因为是数组就没法拷贝
                Integer[] work = saleInfoTo.getWork();
                int total=work[3]*1+work[2]*2+work[1]*4+work[0]*8;
                skuBoundsEntity.setWork(total);
                //就都自己设置吧
                skuBoundsEntity.setBuyBounds(saleInfoTo.getBuyBounds());
                skuBoundsEntity.setGrowBounds(saleInfoTo.getGrowBounds());
                skuBoundsEntity.setSkuId(saleInfoTo.getSkuId());


                skuBoundsDao.insert(skuBoundsEntity);
                //2 sku阶梯价格的保存
                SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
                BeanUtils.copyProperties(skuSaleInfoTo,skuBoundsEntity);
                skuLadderEntity.setAddOther(saleInfoTo.getLadderAddOther());
                skuLadderDao.insert(skuLadderEntity);

                //3 满减信息的保存
                SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
                BeanUtils.copyProperties(skuSaleInfoTo,skuFullReductionEntity);
                skuFullReductionEntity.setAddOther(saleInfoTo.getFullAddOther());
                skuFullReductionDao.insert(skuFullReductionEntity);
            }
        }
    }

}