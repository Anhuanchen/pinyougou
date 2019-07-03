package com.pinyougou.sellergoods.service;

import PageResult.PageResult;
import com.pinyougou.pojo.TbBrand;
import PageResult.InsertResult;
import java.util.List;
import java.util.Map;

public interface BrandService {
    /**
     * 查询所有品牌
     * @return
     */
    public List<TbBrand> findAllBrands();

    /**
     * 分页查询所有品牌
     * @param PageNum 当前页
     * @param PageSize 每页显示条数
     * @return
     */
    public PageResult findByPages(int PageNum,int PageSize);

    /**
     * 添加一个品牌
     * @param tbBrand
     * @return
     */
    public InsertResult insertBrand(TbBrand tbBrand);

    /**
     * 根据id查询当前品牌，进行回显
     * @param id
     * @return
     */
    public TbBrand findOne(Long id);

    /**
     * 修改当前品牌
     * @param tbBrand
     * @return
     */
    public InsertResult updateBrand(TbBrand tbBrand);

    /**
     * 根据ids数组，删除品牌们
     * @param ids
     * @return
     */
    public void deleteBrands(Long[] ids);

    /**
     * 模糊查询，并分页
     * @param tbBrand
     * @param PageNum
     * @param PageSize
     * @return
     */
    public PageResult findIndistinct(TbBrand tbBrand,int PageNum,int PageSize);

    /**
     * 品牌下拉列框数据
     * @return
     */
    public List<Map> selectOptionList();
}
