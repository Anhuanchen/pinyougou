app.controller('searchController',function ($scope,searchService) {
    //添加搜索项
    $scope.searchMap={'keywords':'','category':'','brand':'',spec:{}}

    $scope.search=function () {
        searchService.search($scope.searchMap).success(
            function (response) {
            $scope.resultMap=response;
        });
    }

    //添加搜索项
    $scope.addSearchItem=function (key, value) {
        if(key=='category'||key=='brand'){
            //分类或者品牌
            $scope.searchMap[key]=value;
        }else{
            //规格
            $scope.searchMap.spec[key]=value;
        }
        $scope.search();
    }

    //移除复合搜索条件[根据key移除]
    $scope.removeSearchItem=function (key) {
        if(key=="category"||key=="brand"){
            //如果是分类或者品牌
            $scope.searchMap[key]='';
        }else{
            //移除此属性
            delete $scope.searchMap.spec[key];
        }
        $scope.search();
    }
})