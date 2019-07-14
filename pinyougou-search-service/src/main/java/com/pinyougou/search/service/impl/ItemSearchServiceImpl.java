package com.pinyougou.search.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemCatExample;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(timeout = 5000) //超时时间
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @Override
    public Map<String, Object> search(Map searchMap) {
//        Map<String,Object> map = new HashMap<>();
//        //创建查询对象
//        Query  query = new SimpleQuery("*:*");
//        //构建查询条件
//        Criteria criteria = new Criteria("keywords");
//        query.addCriteria(criteria);
//        //获取page对象
//        ScoredPage<Map> page = solrTemplate.queryForPage(query, Map.class);
//        //获取查询对象，并封装
//        map.put("rows",page.getContent());
//        return map;
        //1.按照关键字查询（高亮显示关键字）
        Map<String,Object> map = new HashMap<>();
        map.putAll(searchList(searchMap));
        //2.根据关键字查询商品分类
        List<String> categorylist = searchCategoryList(searchMap);
        //3.查询品牌和规格列表
        String category = (String) searchMap.get("category");
        if(!"".equals(category)){
            //如果有分类名称，按照分类名称查询
            map.putAll(searchBrandAndSpec(category));
        }else{
            //如果没有分类名称
            if(categorylist.size()>0){
                map.putAll(searchBrandAndSpec(categorylist.get(0)));
            }
        }

        map.put("categoryList",categorylist);
        return map;
    }

    /**
     * 根据关键字搜索列表
     * @param searchMap
     * @return
     */
    private Map searchList(Map searchMap){
        Map map = new HashMap<>();
        //创建高亮显示查询对象
        HighlightQuery query = new SimpleHighlightQuery();
        //设置高亮域【可以设置多个add】
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
        //高亮前缀
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        //高亮后缀
        highlightOptions.setSimplePostfix("</em>");
        //设置高亮选项
        query.setHighlightOptions(highlightOptions);

        //1.1关键字查询【solrhome中字段，构建字段】
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        //1.2按照商品分类过滤
        if(!"".equals(searchMap.get("category"))){
            Criteria criteria1 = new Criteria("item_category").is(searchMap.get("category"));
            FilterQuery filterQuery = new SimpleFilterQuery(criteria1);
            filterQuery.addCriteria(criteria);
            query.addFilterQuery(filterQuery);
        }
        //1.3按照品牌分类过滤
        if(!"".equals(searchMap.get("brand"))){

            Criteria criteria1 = new Criteria("item_brand").is(searchMap.get("brand"));
            FilterQuery filterQuery = new SimpleFilterQuery(criteria1);
            query.addFilterQuery(filterQuery);
        }
        //1.4按照规格分类过滤
        if(searchMap.get("spec")!=null){
            Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
            for (String key : specMap.keySet()) {
                Criteria criteria1 = new Criteria("item_spec_"+key).is(specMap.get(key));
                FilterQuery filterQuery = new SimpleFilterQuery(criteria1);
                query.addFilterQuery(filterQuery);
            }
        }

        //获取高亮页page对象
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //循环遍历高亮页page对象，得到每一个add
        for (HighlightEntry<TbItem> h : page.getHighlighted()) {//循环高亮入口
            //获取原实体类
            TbItem item = h.getEntity();
            if(h.getHighlights().size()>0&&h.getHighlights().get(0).getSnipplets().size()>0){
                //设置高亮结果
                item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));
            }
        }

        //获取查询对象，并封装
        map.put("rows",page.getContent());
        return map;
    }

    /**
     * 查询分类列表
     * 根据模板查出品牌列表
     * 根据模板查出规格列表
     * @param searchMap
     * @return
     */
    private List searchCategoryList(Map searchMap){
        List<String> list = new ArrayList<String>();
        Query query = new SimpleQuery();
        //按照关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        //设置分组选项
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        //得到分组页
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query,TbItem.class);
        //根据列得到分组结果集
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        //得到分组结果入口页
        Page<GroupEntry<TbItem>> entryPage = groupResult.getGroupEntries();
        //得到分组入口集合
        List<GroupEntry<TbItem>> content = entryPage.getContent();
        for (GroupEntry<TbItem> entry : content) {
            //将分组结果的名称封装到返回值当中
            list.add(entry.getGroupValue());
        }
            return list;
    }

    /**
     * 根据模板名查询id，进而查询品牌和规格列表
     * @param category
     * @return
     */
    private Map searchBrandAndSpec(String category){
        Map map = new HashMap();
        //根据模板名称查询模板id
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if(typeId!=null){
            List brandList =(List) redisTemplate.boundHashOps("brandList").get(typeId);
            //返回值添加到品牌列表
            map.put("brandList",brandList);
            List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            //返回值添加到规格列表
            map.put("specList",specList);
        }
        return map;
    }


}
