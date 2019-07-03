package com.pinyougou.sellergoods.service.impl;


import PageResult.PageResult;
import PageResult.InsertResult;
import PageResult.DeleteResult;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.pojo.TbBrandExample.Criteria;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper tbBrandMapper;

    /**
     * 查询所有品牌
     *
     * @return
     */
    @Override
    public List<TbBrand> findAllBrands() {
        return tbBrandMapper.selectByExample(null);
    }

    /**
     * 分页查询所有品牌
     *
     * @param PageNum  当前页
     * @param PageSize 每页显示条数
     * @return
     */
    @Override
    public PageResult findByPages(int PageNum, int PageSize) {
        PageHelper.startPage(PageNum, PageSize);
        //强转为Page对象
        Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 添加一个品牌
     *
     * @param tbBrand
     * @return
     */
    @Override
    public InsertResult insertBrand(TbBrand tbBrand) {

            try {
                tbBrandMapper.insert(tbBrand);
                return new InsertResult(true, "添加成功");
            } catch (Exception e) {
                e.printStackTrace();
                return new InsertResult(false, "添加失败");
            }

    }

    /**
     * 根据主键id查询当前品牌
     *
     * @param id
     * @return
     */
    @Override
    public TbBrand findOne(Long id) {
        TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(id);
        return tbBrand;
    }

    /**
     * 修改当前品牌
     *
     * @param tbBrand
     * @return
     */
    @Override
    public InsertResult updateBrand(TbBrand tbBrand) {

            try {
                tbBrandMapper.updateByPrimaryKey(tbBrand);
                return new InsertResult(true, "修改成功");
            } catch (Exception e) {
                return new InsertResult(false, "修改失败");
            }

    }

    /**
     * 删除选中的品牌们
     * @param ids
     */
    @Override
    public void deleteBrands(Long[] ids) {
        if(ids!=null&&ids.length>0){
            for (Long id : ids) {
                tbBrandMapper.deleteByPrimaryKey(id);
            }
        }
    }

    /**
     * 模糊查询，并分页
     * @param tbBrand
     * @param PageNum
     * @param PageSize
     * @return
     */
    @Override
    public PageResult findIndistinct(TbBrand tbBrand, int PageNum, int PageSize) {
        PageHelper.startPage(PageNum,PageSize);
        //创建条件类
        TbBrandExample tbBrandExample = new TbBrandExample();
        //构建条件
        Criteria criteria = tbBrandExample.createCriteria();
        //设置条件
        if(tbBrand.getName()!=null&&tbBrand.getName().length()>0){
            criteria.andNameLike("%"+tbBrand.getName()+"%");
        }
        if(tbBrand.getFirstChar()!=null&&tbBrand.getFirstChar().length()>0){
            criteria.andFirstCharLike("%"+tbBrand.getFirstChar()+"%");
        }
        //得到查询对象，转为page对象
        Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(tbBrandExample);
        //返回PageResult对象【总条数，当前页对象】
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 品牌下拉框列表数据
     * @return
     */
    @Override
    public List<Map> selectOptionList() {
        List<Map> maps = tbBrandMapper.selectOptionList();
        return maps;
    }
}
