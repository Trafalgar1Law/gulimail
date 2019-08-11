package com.atguigu.gulimall.pms.service.impl;


import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuSaleInfoTo;
import com.atguigu.gulimall.commons.to.SkuStockVo;
import com.atguigu.gulimall.commons.to.es.EsSkuAttributeValue;
import com.atguigu.gulimall.commons.to.es.EsSkuVo;
import com.atguigu.gulimall.commons.utils.AppUtils;
import com.atguigu.gulimall.pms.dao.*;
import com.atguigu.gulimall.pms.entity.*;

import com.atguigu.gulimall.pms.feign.EsFeignService;
import com.atguigu.gulimall.pms.feign.SmsSkuSaleInfoFService;
import com.atguigu.gulimall.pms.feign.WmsFeignService;
import com.atguigu.gulimall.pms.vo.InnerVo.BaseAttrVo;
import com.atguigu.gulimall.pms.vo.InnerVo.SaleAttrVo;
import com.atguigu.gulimall.pms.vo.InnerVo.SkuVo;
import com.atguigu.gulimall.pms.vo.SpuAllSaveVo;


import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import com.atguigu.gulimall.pms.service.SpuInfoService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescDao spuInfoDescDao;
    @Autowired
    private ProductAttrValueDao productAttrValueDao;
    @Autowired
    private SkuInfoDao skuInfoDao;
    @Autowired
    private SkuImagesDao imagesDao;
    @Autowired
    private AttrDao attrDao;
    @Autowired
    private SkuSaleAttrValueDao skuSaleAttrValueDao;
    @Autowired
    private SmsSkuSaleInfoFService smsSkuSaleInfoFService;
    @Autowired
    private EsFeignService esFeignService;
    @Autowired
    BrandDao brandDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    WmsFeignService wmsFeignService;


    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryPageByCatId(QueryCondition queryCondition, Integer catId) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        if (catId != 0) {
            queryWrapper.eq("catalog_id", catId);
            if (!StringUtils.isEmpty(queryCondition.getKey())) {
                queryWrapper.and((obj) -> {
                    return obj.like("id", queryCondition.getKey())
                            .or().like("spu_name", queryCondition.getKey());
                });
            }

        }


        IPage<SpuInfoEntity> page = new Query<SpuInfoEntity>().getPage(queryCondition);

        IPage<SpuInfoEntity> selectPage = baseMapper.selectPage(page, queryWrapper);

        PageVo pageVo = new PageVo(selectPage);

        return pageVo;
    }

    /**
     * description: 大保存
     *
     * @return void
     * <p>
     * this不是代理对象，就相当于代码粘到了大方法里面
     * 解决方法：放到别人的service
     * 如果能获取到苯类的代理对象，直接调方法
     * @Param [spuAllSaveVo]
     */
    @GlobalTransactional
    @Override
    public void spuBigSaveAll(SpuAllSaveVo spuAllSaveVo) {
        SpuInfoService spuProxy = (SpuInfoService) AopContext.currentProxy();

        //1 基本信息保存
        Long spuId = this.saveSpuBaseInfo(spuAllSaveVo);
        //2 保存spu的详情图
        spuProxy.saveSpuInfoImages(spuId, spuAllSaveVo.getSpuImages());
        //3 保存spu的基本属性信息
        List<BaseAttrVo> baseAttrs = spuAllSaveVo.getBaseAttrs();
        spuProxy.saveSpuBaseAttrs(spuId, baseAttrs);
        //保存sku以及sku营销的相关信息
        spuProxy.saveSkuInfos(spuId, spuAllSaveVo.getSkus());

        //4 提取出所有优惠信息
        //int i=10/0;

    }

    /**
     * description: 保存sku的所有详情
     *
     * @return void
     * @Param [spuId, skus]
     */
    @Transactional
    @Override
    public void saveSkuInfos(Long spuId, List<SkuVo> skus) {
        SpuInfoEntity spu = baseMapper.selectById(spuId);
        //1 保存sku的info信息
        List<SkuSaleInfoTo> list = list = new ArrayList<>();
        for (SkuVo skuVo : skus) {
            String[] images = skuVo.getImages();
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            skuInfoEntity.setBrandId(spu.getBrandId());
            skuInfoEntity.setCatalogId(spu.getCatalogId());
            //
            skuInfoEntity.setPrice(skuVo.getPrice());
            skuInfoEntity.setSkuCode(UUID.randomUUID().toString().substring(0, 5).toUpperCase());
            //判断一下
            if (images != null && images.length > 0) {
                skuInfoEntity.setSkuDefaultImg(images[0]);
            }

            skuInfoEntity.setSkuDesc(skuVo.getSkuDesc());

            skuInfoEntity.setSkuName(skuVo.getSkuName());
            skuInfoEntity.setSkuTitle(skuVo.getSkuSubtitle());
            skuInfoEntity.setSpuId(spuId);
            skuInfoEntity.setWeight(skuVo.getWeight());
            skuInfoEntity.setSkuTitle(skuVo.getSkuSubtitle());
            //落盘
            skuInfoDao.insert(skuInfoEntity);

            Long skuId = skuInfoEntity.getSkuId();
            //2 保存sku所有的对应图片
            for (int i = 0; i < images.length; i++) {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                skuImagesEntity.setDefaultImg(i == 0 ? 1 : 0);
                skuImagesEntity.setSkuId(skuId);
                skuImagesEntity.setImgUrl(images[i]);
                skuImagesEntity.setImgSort(0);

                imagesDao.insert(skuImagesEntity);
            }

            //3 当前sku的所有销售属性
            List<SaleAttrVo> saleAttrs = skuVo.getSaleAttrs();
            for (SaleAttrVo saleAttr : saleAttrs) {
                SkuSaleAttrValueEntity entity = new SkuSaleAttrValueEntity();
                entity.setAttrId(saleAttr.getAttrId());
                //查询当前属性的信息
                AttrEntity attrEntity = attrDao.selectById(saleAttr.getAttrId());
                entity.setAttrName(attrEntity.getAttrName());
                entity.setAttrSort(0);
                entity.setAttrValue(saleAttr.getAttrValue());
                entity.setSkuId(skuId);

                //sku与销售属性的关联关系也保存了
                skuSaleAttrValueDao.insert(entity);
            }
//--------------------------------以上是pms系统干的活----------------------------------------

//--------------------------------以下需要sms系统完成----------------------------------------
            SkuSaleInfoTo skuSaleInfoTo = new SkuSaleInfoTo();
            BeanUtils.copyProperties(skuVo, skuSaleInfoTo);
            skuSaleInfoTo.setSkuId(skuId);

            list.add(skuSaleInfoTo);
        }
        //发给sms，让他去处理
        log.info("pms向sms发送了请求。。。{}", list);
        smsSkuSaleInfoFService.saveSkuSaleInfos(list);
        log.info("请求发送成功了");
    }

    @Transactional
    @Override
    public void saveSpuBaseAttrs(Long spuId, List<BaseAttrVo> baseAttrs) {
        ArrayList<ProductAttrValueEntity> list = new ArrayList<>();

        if (baseAttrs != null && baseAttrs.size() > 0) {
            for (BaseAttrVo baseAttr : baseAttrs) {
                ProductAttrValueEntity attrValueEntity = new ProductAttrValueEntity();
                attrValueEntity.setAttrId(baseAttr.getAttrId());
                attrValueEntity.setAttrName(baseAttr.getAttrName());

                attrValueEntity.setAttrValue(
                        AppUtils.arrayToStringWithSeparater(baseAttr.getValueSelected(), ","));
                attrValueEntity.setAttrSort(0);
                attrValueEntity.setSpuId(spuId);
                attrValueEntity.setQuickShow(1);

                list.add(attrValueEntity);
            }
        }

        productAttrValueDao.insertBatch(list);

    }

    /**
     * description: 商品上下架
     *
     * @return void
     * @Param [spuId, status]
     */
    @Override
    public void updateSpuStatus(Long spuId, Integer status) {
        //1 修改数据库上下架的状态
        //将商品需要检索的信息放在es中
        if (status == 1) {
            spuUp(spuId, status);
        } else {//下架就是删除在es中的信息
            spuDown(spuId, status);
        }


    }

    private void spuUp(Long spuId, Integer status) {
        List<EsSkuVo> esSkuVoList = new ArrayList<>();
        //查出所有需要上架的spu的所有sku
        List<SkuInfoEntity> skuList = skuInfoDao.selectList(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        List<ProductAttrValueEntity> valueEntities = productAttrValueDao.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        //过滤出可以被检索的
        List<Object> attrList = new ArrayList<>();
        for (ProductAttrValueEntity valueEntity : valueEntities) {
            attrList.add(valueEntity.getAttrId());
        }
        List<AttrEntity> attrEntities = attrDao.selectList(new QueryWrapper<AttrEntity>().in("attr_id", attrList).eq("search_type", 1));
        List<EsSkuAttributeValue> list = new ArrayList<>();

        List<EsSkuAttributeValue> esSkuAttr = new ArrayList<>();
        for (AttrEntity attrEntity : attrEntities) {
            Long attrId = attrEntity.getAttrId();
            //拿到真实的值
            valueEntities.forEach((value) -> {
                if (attrId == value.getAttrId()) {
                   // list.add(value);
                    EsSkuAttributeValue esSkuValue = new EsSkuAttributeValue();
                    esSkuValue.setId(value.getId());
                    esSkuValue.setName(value.getAttrName());
                    esSkuValue.setProductAttributeId(value.getAttrId());
                    esSkuValue.setSpuId(spuId);
                    esSkuValue.setValue(value.getAttrValue());
                    list.add(esSkuValue);
                }
            });
        }


        if (skuList != null && skuList.size() > 0) {
            //构造所有需要保存在es的sku信
            skuList.forEach((sku) -> {
                EsSkuVo esSkuVo = skuInfoToEsSkuVo(sku, list);
                esSkuVoList.add(esSkuVo);

            });

            //远程调用将物品上架
            Resp<Object> resp = esFeignService.saveUp(esSkuVoList);

            if (resp.getCode() == 0) {
                //远程调用成功
                //本地修改数据库
                SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
                spuInfoEntity.setId(spuId);
                spuInfoEntity.setPublishStatus(1);
                spuInfoEntity.setUodateTime(new Date());
                //按照id更新字段
                baseMapper.updateById(spuInfoEntity);
            }
        } else {
            return;
        }

    }

    private void spuDown(Long spuId, Integer status) {

    }

    private EsSkuVo skuInfoToEsSkuVo(SkuInfoEntity skuInfoEntity, List<EsSkuAttributeValue> list) {
        EsSkuVo esSkuVo = new EsSkuVo();
        esSkuVo.setId(skuInfoEntity.getSkuId());
        esSkuVo.setBrandId(skuInfoEntity.getBrandId());
        CategoryEntity categoryEntity = categoryDao.selectById(skuInfoEntity.getCatalogId());
        BrandEntity brandEntity = brandDao.selectById(skuInfoEntity.getBrandId());

        if (brandEntity != null) {

            esSkuVo.setBrandName(brandEntity.getName());
        }

        esSkuVo.setName(skuInfoEntity.getSkuTitle());
        esSkuVo.setPic(skuInfoEntity.getSkuDefaultImg());
        esSkuVo.setPrice(skuInfoEntity.getPrice());
        esSkuVo.setProductCategoryId(skuInfoEntity.getCatalogId());

        if (categoryEntity != null) {
            esSkuVo.setProductCategoryName(categoryEntity.getName());
        }
        esSkuVo.setSale(0);
        esSkuVo.setSort(0);

        //查出sku的所有库存信息
        Resp<List<SkuStockVo>> listResp = wmsFeignService.skuInfoById(skuInfoEntity.getSkuId());
        List<SkuStockVo> stocks = listResp.getData();

        stocks.forEach((item) -> {

            esSkuVo.setStock(item.getStock());
        });


        esSkuVo.setAttributeValues(null);


        return esSkuVo;
    }

    @Transactional
    @Override
    public void saveSpuInfoImages(Long spuId, String[] spuImages) {

        StringBuffer urls = new StringBuffer();
        String url = null;
        if (spuImages != null && spuImages.length > 0) {
            for (String spuImage : spuImages) {
                urls.append(spuImage);
                urls.append(",");
            }
            url = urls.toString().substring(0, urls.length() - 1);
        }


        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setDecript(url);
        spuInfoDescEntity.setSpuId(spuId);

        spuInfoDescDao.insertInfo(spuInfoDescEntity);

    }

    @Transactional
    @Override
    public Long saveSpuBaseInfo(SpuAllSaveVo spuAllSaveVo) {

        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();

        BeanUtils.copyProperties(spuAllSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUodateTime(new Date());

        baseMapper.insert(spuInfoEntity);


        return spuInfoEntity.getId();
    }


}