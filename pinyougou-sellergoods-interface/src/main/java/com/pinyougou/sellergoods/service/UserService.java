//package com.pinyougou.sellergoods.service;
//import java.util.List;
//
//import PageResult.PageResult;
//import com.pinyougou.pojo.TbUser;
////import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.security.core.userdetails.UserDetailsService;
////import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
///**
// * 服务层接口
// * @author Administrator
// *
// */
//// extends UserDetailsService
//public interface UserService{
//
//
////	@Override
////	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
//
//	/**
//	 * 返回全部列表
//	 * @return
//	 */
//	public List<TbUser> findAll();
//
//
//	/**
//	 * 返回分页列表
//	 * @return
//	 */
//	public PageResult findPage(int pageNum, int pageSize);
//
//
//	/**
//	 * 增加
//	*/
//	public void add(TbUser user);
//
//
//	/**
//	 * 修改
//	 */
//	public void update(TbUser user);
//
//
//	/**
//	 * 根据ID获取实体
//	 * @param id
//	 * @return
//	 */
//	public TbUser findOne(Long id);
//
//
//	/**
//	 * 批量删除
//	 * @param ids
//	 */
//	public void delete(Long[] ids);
//
//	/**
//	 * 分页
//	 * @param pageNum 当前页 码
//	 * @param pageSize 每页记录数
//	 * @return
//	 */
//	public PageResult findPage(TbUser user, int pageNum, int pageSize);
//
//}
