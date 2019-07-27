//创建控制中心
app.controller("brandController", function ($scope,$controller,brandService) {

    // 伪继承
    $controller('baseController',{$scope:$scope});

    // //定义方法品牌分页
    // $scope.findByPages = function (page, size) {
    //     $http.get('../brand/findByPages.do?page=' + page + '&size=' + size).success(
    //         function (response) {
    //             $scope.list = response.rows;
    //             $scope.paginationConf.totalItems = response.totalCount;
    //         });
    // }

    //定义方法新增品牌,修改品牌
    $scope.insertBrand = function () {
        //$http.post('../brand/insertBrand.do', $scope.entity)
        brandService.insertBrand($scope.entity).success(
            function (response) {
                if (response.success) {
                    //添加成功就刷新
                    $scope.reloadList();
                } else {
                    //添加不成功就弹出一个窗口
                    alert(response.message);
                }
            });
    }

    //定义方法查询品牌
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity=response;
            });
    };



    //删除选中的ids
    $scope.deleIds = function () {
        brandService.deleIds($scope.ids).success(
            function (response) {
                if(response.success){
                    $scope.reloadList();
                }else{
                    alert($scope.message);
                }
            });
    }



    //模糊查询
    $scope.findIndistinct = function (page,size) {
        brandService.findIndistinct(page,size,$scope.IndistinctEntity).success(
            function (response) {
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.totalCount;
            });
    }

});