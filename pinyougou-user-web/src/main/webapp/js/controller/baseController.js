//品牌控制层
app.controller('baseController', function ($scope) {

    //重新加载列表 数据
    $scope.reloadList = function () {
        //切换页码
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage:5,
        perPageOptions: [1, 2, 3, 4, 5],
        onChange:function (){
            $scope.reloadList();//重新加载
        }
    };

    $scope.selectIds = [];//选中的ID集合

    //更新复选
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {//如果是被选中,则增加到数组
            $scope.selectIds.push(id);
        } else {
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);//删除 
        }
    }
    //从集合中按key查找对象
    $scope.searchObjectBykey = function (list, key, keyValue) {
        //遍历结合，查看是否已经存在对应的keyvalue
        for (var i = 0; i < list.length; i++) {
            //已经存在，我们返回对应的集合
            if (list[i][key] == keyValue) {
                return list[i];
            }
        }
        //不存在，我们返回null
        return null;
    }
});