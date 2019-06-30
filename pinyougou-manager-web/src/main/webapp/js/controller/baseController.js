app.controller("baseController", function ($scope) {

    $scope.IndistinctEntity={};

    $scope.paginationConf = {
        currentPage: 1,  //当前页码

        totalItems: 10,  //总条数

        itemsPerPage: 10, //每页显示条数

        perPageOptions: [1, 2, 3, 4, 5],//页码选项
        //更改页面时触发事件
        onChange: function () {
            //首先查询
            $scope.reloadList();
        }
    };

    $scope.reloadList = function () {
        $scope.findIndistinct($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };

    //定义一个数组ids来接收传递的id
    $scope.ids=[];
    //将传递的id存储进ids
    $scope.pushId=function ($event,id) {
        //如果被选中，就添加
        if(event.target.checked){
            $scope.ids.push(id);
        }else{
            //如果没有被选中,找出他的角标，去除选中状态
            var number =$scope.ids.indexOf(id);
            $scope.ids.splice(number,1);
        }
    }
})