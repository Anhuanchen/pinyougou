package com.pinyougou.sellergoods.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import PageResult.PageResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellergoods.service.GoodsService;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbBrandMapper tbBrandMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private TbSellerMapper tbSellerMapper;
	@Autowired
	private TbItemMapper itemMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private TbGoodsDescMapper tbGoodsDescMapper;

	/**
	 * 增加，SKU表保存
	 */
	@Override
	public void add(Goods goods) {
		goods.getTbGoods().setAuditStatus("0");//设置为未申请状态
		goodsMapper.insert(goods.getTbGoods());
		//从Tbgoods中获取id，设置为GoodsDesc的goodsId
		goods.getGoodsDesc().setGoodsId(goods.getTbGoods().getId());
		tbGoodsDescMapper.insert(goods.getGoodsDesc());
		//标题 = goodsName+规格选项值
		if("1".equals(goods.getTbGoods().getIsEnableSpec())){
			for (TbItem item : goods.getItemList()) {
				//先获得goodsName
				String title=goods.getTbGoods().getGoodsName();
				//然后从item中获取规格选项值,得到JSON对象，并转换
				Map<String,Object> map = JSON.parseObject(item.getSpec());
				for (String key : map.keySet()) {
					title+=" "+map.get(key);
				}
				item.setTitle(title);
				//存储3级分类id
				setItemValues(item,goods);
				itemMapper.insert(item);
			}
		}else{
			//不循环，只存一条
			TbItem item = new TbItem();
			item.setTitle(goods.getTbGoods().getGoodsName());
			item.setPrice(goods.getTbGoods().getPrice());
			item.setNum(99999);
			item.setStatus("1");
			item.setIsDefault("1");
			item.setSpec("{}");
			setItemValues(item,goods);
			itemMapper.insert(item);
		}

	}


	private void setItemValues(TbItem item,Goods goods){
		item.setCategoryid(goods.getTbGoods().getCategory3Id());
		item.setCreateTime(new Date());//创建日期
		item.setUpdateTime(new Date());//更新日期
		item.setGoodsId(goods.getTbGoods().getId());//goodsId
		item.setSeller(goods.getTbGoods().getSellerId());//sellerId
		//第三级对应的分类
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getTbGoods().getCategory3Id());
		//得到分类名称
		item.setCategory(itemCat.getName());
		//设置对应的商品品牌
		TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(goods.getTbGoods().getBrandId());
		item.setBrand(tbBrand.getName());
		//设置对应的商家名称[店铺名称]
		TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(goods.getTbGoods().getSellerId());
		item.setSeller(tbSeller.getNickName());
		//存储图片
		List<Map> imageList=JSON.parseArray(goods.getGoodsDesc().getItemImages(),Map.class);
		if (imageList.size()>0){
			item.setImage((String)imageList.get(0).get("url"));
		}
	}

	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goods){
		goodsMapper.updateByPrimaryKey(goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){
		Goods goods = new Goods();
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setTbGoods(tbGoods);
		TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(id);
		goods.setGoodsDesc(tbGoodsDesc);

		return goods;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		if(goods!=null){			
						if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				//criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
							criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
