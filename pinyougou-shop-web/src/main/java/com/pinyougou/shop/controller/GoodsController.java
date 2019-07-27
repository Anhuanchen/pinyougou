package com.pinyougou.shop.controller;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import PageResult.PageResult;
import com.pinyougou.pojogroup.Goods;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.GoodsService;
import PageResult.InsertResult;
import PageResult.DeleteResult;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
//	@RequestMapping("/findPage")
//	public PageResult findPage(int page, int rows){
//		return goodsService.findPage(page, rows);
//	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public InsertResult add(@RequestBody Goods goods){
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.getTbGoods().setSellerId(sellerId);
		try {
			goodsService.add(goods);
			return new InsertResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new InsertResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public InsertResult update(@RequestBody Goods goods){
		//首先判断商品是否是该商家的商品
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();//当前商家id
		Goods goods2 = goodsService.findOne(goods.getTbGoods().getId());
		//判断当前的商家sellerid和传过来的商家的商家id
		if(!goods2.getTbGoods().getSellerId().equals(sellerId)||!goods.getTbGoods().getSellerId().equals(sellerId)){
			return new InsertResult(false,"非法操作");
		}


		try {
			goodsService.update(goods);
			return new InsertResult(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new InsertResult(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public DeleteResult delete(Long [] ids){
		try {
			goodsService.delete(ids);
			return new DeleteResult(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new DeleteResult(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		//添加查询条件
		goods.setSellerId(sellerId);
		return goodsService.findPage(goods, page, rows);
	}


	@RequestMapping("/changeIsMarketable")
	public InsertResult changeIsMarketable(Long []ids,String status){
		try{
			goodsService.changeIsMarketable(ids,status);
			return new InsertResult(true,"成功");
		}catch (Exception e){
			e.printStackTrace();
			return new InsertResult(false,"失败");
		}
	}
	
}
