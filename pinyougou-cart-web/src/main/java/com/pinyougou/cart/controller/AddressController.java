package com.pinyougou.cart.controller;
import java.util.List;

import PageResult.PageResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbAddress;
import com.pinyougou.user.service.AddressService;

import PageResult.InsertResult;



/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/address")
public class AddressController {

	@Reference
	private AddressService addressService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbAddress> findAll(){			
		return addressService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows){
		return addressService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param address
	 * @return
	 */
	@RequestMapping("/add")
	public InsertResult add(@RequestBody TbAddress address){
		try {
			addressService.add(address);
			return new InsertResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new InsertResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param address
	 * @return
	 */
	@RequestMapping("/update")
	public InsertResult update(@RequestBody TbAddress address){
		try {
			addressService.update(address);
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
	public TbAddress findOne(Long id){
		return addressService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public InsertResult delete(Long [] ids){
		try {
			addressService.delete(ids);
			return new InsertResult(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new InsertResult(false, "删除失败");
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
	public PageResult search(@RequestBody TbAddress address, int page, int rows  ){
		return addressService.findPage(address, page, rows);		
	}

	/**
	 * 根据用户名id查询用户信息
	 * @return
	 */
	@RequestMapping("/findListByLoginUser")
	public List<TbAddress> findListByLoginUser(){
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		List<TbAddress> listByUserId = addressService.findListByUserId(name);
		return listByUserId;
	}
	
}
