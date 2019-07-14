package com.pinyougou.search.service;

import com.pinyougou.pojo.TbItemCat;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {
    /**
     * 搜索
     * @param searchMap
     * @return
     */
    public Map<String,Object> search(Map searchMap);


}
