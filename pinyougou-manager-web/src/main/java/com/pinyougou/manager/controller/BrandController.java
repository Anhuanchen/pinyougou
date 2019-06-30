package com.pinyougou.manager.controller;


import PageResult.PageResult;
import PageResult.InsertResult;
import PageResult.DeleteResult;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * 查询所有品牌
     *
     * @return
     */
    @RequestMapping("/findAllBrands.do")
    public List<TbBrand> findAllBrands() {
        return brandService.findAllBrands();
    }

    /**
     * 分页查询所有品牌
     *
     * @param PageNum
     * @param PageSize
     * @return
     */
    @RequestMapping("/findByPages.do")
    public PageResult findByPages(@RequestParam(name = "page") int PageNum, @RequestParam(name = "size") int PageSize) {
        if (PageNum != 0 && PageSize != 0) {
            PageResult pageResult = brandService.findByPages(PageNum, PageSize);
            return pageResult;
        }
        return null;
    }

    /**
     * 添加一个品牌,或者修改一个品牌
     *
     * @param tbBrand
     * @return
     */
    @RequestMapping("/insertBrand.do")
    public InsertResult InsertBrand(@RequestBody TbBrand tbBrand) {
        if (tbBrand.getId() == null) {
            //id为空或者为0，进行插入
            InsertResult insertResult = brandService.insertBrand(tbBrand);
            return insertResult;
        }
        //id不为零，进行修改
        InsertResult insertResult = brandService.updateBrand(tbBrand);
        return insertResult;
    }

    /**
     * 根据id查询当前品牌
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public TbBrand findById(Long id) {
        TbBrand tbBrand = brandService.findOne(id);
        return tbBrand;
    }

    /**
     * 删除品牌们
     *
     * @param ids
     * @return
     */
    @RequestMapping("/deleResult.do")
    public DeleteResult deleResult(@RequestBody Long[] ids) {
        if (ids != null && ids.length > 0) {
            try {
                brandService.deleteBrands(ids);
                return new DeleteResult(true, "删除成功");
            } catch (Exception e) {
                return new DeleteResult(false, "删除失败");
            }
        }
        return null;
    }

    /**
     * 模糊查询
     * @param tbBrand
     * @param PageNum
     * @param PageSize
     * @return
     */
    @RequestMapping("/findIndistinct.do")
    public PageResult findIndistinct(@RequestBody TbBrand tbBrand,@RequestParam(name = "page") int PageNum, @RequestParam(name = "size") int PageSize){
        PageResult pageResult = brandService.findIndistinct(tbBrand, PageNum, PageSize);

        return pageResult;
    }
}
