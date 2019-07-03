//创建服务中心
app.service("brandService",function ($http) {
    //定义方法新增品牌,修改品牌
    this.insertBrand = function (entity) {
        return $http.post('../brand/insertBrand.do', entity);
    }

    //定义方法查询品牌
    this.findOne = function (id) {
        return $http.get('../brand/findOne.do?id='+ id);
    }

    //删除选中的ids
    this.deleIds = function (ids) {
        return $http.post("/brand/deleResult.do", ids)
    }

    //模糊查询
    this.findIndistinct = function (page,size,IndistinctEntity) {
        return $http.post('../brand/findIndistinct.do?page=' + page + '&size=' + size, IndistinctEntity);
    }

    //查询品牌下拉列表
    this.selectOptionList=function () {
        return $http.get('../brand/selectOptionList.do?');
    }

});