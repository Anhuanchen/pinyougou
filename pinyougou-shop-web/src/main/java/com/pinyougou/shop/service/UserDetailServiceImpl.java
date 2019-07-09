package com.pinyougou.shop.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证类
 */

public class UserDetailServiceImpl implements UserDetailsService {

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //构建角色列表
        List<GrantedAuthority> grantedAuths= new ArrayList<GrantedAuthority>();

        grantedAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        //得到商家对象
        TbSeller tbSeller = sellerService.findOne(username);

        if(tbSeller!=null){
            if (tbSeller.getStatus().equals("1")){
                //如果该商家审核通过，就给他添加Role_Seller的角色
                return new User(username,tbSeller.getPassword(),grantedAuths);
            }else {
                return null;
            }
        }
            return null;
    }
}
