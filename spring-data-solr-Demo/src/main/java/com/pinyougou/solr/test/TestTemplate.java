package com.pinyougou.solr.test;

import com.pinyougou.solr.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-solr.xml")
public class TestTemplate {

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testAdd(){
        TbItem tbItem = new TbItem();
        tbItem.setId(1L);
        tbItem.setBrand("小米");
        tbItem.setCategory("手机");
        tbItem.setGoodsId(1L);
        tbItem.setTitle("小米手机1号专卖店");
        tbItem.setSeller("红米RedeMe");
        tbItem.setPrice(new BigDecimal(2600));
        solrTemplate.saveBean(tbItem);
        solrTemplate.commit();
    }

    /**
     * 根据主键查询
     */
    @Test
    public void findByPrimaryKey(){
        TbItem tbItem = solrTemplate.getById(1, TbItem.class);
        System.out.println(tbItem.getTitle());
    }

    /**
     * 根据主键删除
     */
    @Test
    public void deleteByPrimaryKey(){
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    /**
     * 批量插入
     */
    @Test
    public void saveBeans(){
        List list = new ArrayList();
        for (int i = 0; i <100 ; i++) {
            TbItem tbItem = new TbItem();
            tbItem.setId(i+1L);
            tbItem.setBrand("小米");
            tbItem.setCategory("手机");
            tbItem.setGoodsId(1L);
            tbItem.setTitle("小米手机"+i+"号专卖店");
            tbItem.setSeller("红米RedeMe");
            tbItem.setPrice(new BigDecimal(2600+i));
            list.add(tbItem);
        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    /**
     * 分页查询
     */
    @Test
    public void findByPage(){
        Query query = new SimpleQuery("*:*");
        query.setOffset(10);//开始索引（默认是0）
        query.setRows(20);//每页记录数
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);//分页page对象
        //根据page对象获取list
        List<TbItem> list = page.getContent();
        showList(list);
        System.out.println("总记录数"+page.getTotalElements());
    }

    private void showList(List<TbItem> list){
        for (TbItem tbItem : list) {
            System.out.println(tbItem.getTitle()+","+tbItem.getPrice());
        }

    }

    /**
     * 条件查询
     */
    @Test
    public void testPageQuery(){
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_title").contains("2");
        criteria=criteria.and("item_title").contains("3");
        query.addCriteria(criteria);
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        List<TbItem> list = page.getContent();
        showList(list);
    }

    /**
     * 删除全部数据
     */
    @Test
    public void deleteAll(){
        Query query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

}
