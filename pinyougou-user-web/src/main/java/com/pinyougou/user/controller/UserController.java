package com.pinyougou.user.controller;
import java.util.List;

import PageResult.PageResult;
import com.pinyougou.common.utils.PhoneFormatCheckUtils;
import com.pinyougou.user.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbUser;
import PageResult.InsertResult;


/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference
	private UserService userService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbUser> findAll(){			
		return userService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows){
		return userService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	public InsertResult add(@RequestBody TbUser user,String smscode){
		boolean checkSmsCode = userService.checkSmsCode(user.getPhone(),smscode);
		if (checkSmsCode==false){
			return new InsertResult(false,"验证码输入错误");
		}
		try {
			userService.add(user);
			return new InsertResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new InsertResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public InsertResult update(@RequestBody TbUser user){
		try {
			userService.update(user);
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
	public TbUser findOne(Long id){
		return userService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public InsertResult delete(Long [] ids){
		try {
			userService.delete(ids);
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
	public PageResult search(@RequestBody TbUser user, int page, int rows  ){
		return userService.findPage(user, page, rows);		
	}

	/**
	 * 发送短信验证码
	 * @param phone
	 * @return
	 */
	@RequestMapping("/sendCode")
	public InsertResult sendCode(String phone){
		if(!PhoneFormatCheckUtils.isPhoneLegal(phone)){
		return new InsertResult(false,"手机格式不正确 ");
		}
		try{
			userService.createSmsCode(phone);//生成随机的6位验证码
			return new InsertResult(true,"验证码发送成功 ");
		}catch (Exception e){
			e.printStackTrace();
			return new InsertResult(false,"验证码发送失败");
		}
	}


}
