app.controller('itemController',function ($scope) {
    //数量操作
    $scope.addNum=function (x) {
        $scope.num=$scope.num+x;
        if ($scope.num<1){
            $scope.num=1;
        }
    }
    //记录用户选择的规格
    $scope.specificationItems={};
    //用户选择规格
    $scope.selectSpecification=function (name, value) {
        $scope.specificationItems[name]=value;
        searchSku();//读取Sku
    }
    //判断某规格是否被用户选中
    $scope.isSelected=function (name, value) {
        if($scope.specificationItems[name]==value){
            return true;
        }else{
            return false;
        }
    }
    //加载默认sku
    $scope.loadSku=function () {
        $scope.sku=skuList[0];
        //深克隆
        $scope.specificationItems=JSON.parse(JSON.stringify($scope.sku.spec));
    }
    //选择规格更新SKU
    matchObject=function (map1,map2) {
        for(var k in map1){
            if(map1[k]!=map2[k]){
                return false;
            }
        }

        for (var k in map2){
            if(map2[k]!=map1[k]){
                return false;
            }
        }
        return true;
    }
    //查询SKU
    searchSku=function () {
        for (var i=0;i<skuList.length;i++){
            if(matchObject(skuList[i].spec,$scope.specificationItems)){
                $scope.sku=skuList[i];
                return;
            }
        }
        //如果没有匹配的
        $scope.sku={id:0,title:'--------------',price:0};
    }

    //添加购物车
    $scope.addcar=function () {
        alert($scope.sku.id);
    }

});