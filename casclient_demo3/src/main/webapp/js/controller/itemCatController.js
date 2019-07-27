//控制层
app.controller('itemCatController', function ($scope, $controller, itemCatService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        itemCatService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        itemCatService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //定义一个parentId
    $scope.parentId=0;

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = itemCatService.update($scope.entity); //修改
        } else {
            //将当前的parentId赋值给添加的实体的parentId保存
            $scope.entity.parentId=$scope.parentId;
            serviceObject = itemCatService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.findByParentId($scope.parentId);//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.deleIds = function () {
        //获取选中的复选框
        itemCatService.dele($scope.ids).success(
            function (response) {
                if (response.success) {
                    $scope.findByParentId($scope.parentId);//刷新列表
                }else{
                    alert($scope.message);
                }
            }
        );
    }

    //批量删除
    // $scope.deleIds = function () {
    //     for (var i = 0; i <$scope.ids.length; i++) {
    //         if($scope.findByParentId==null){
    //
    //         }else{
    //             $scope.deleIds()
    //         }
    //     }
    //     //获取选中的复选框
    //     itemCatService.dele($scope.ids).success(
    //         function (response) {
    //             if (response.success) {
    //                 $scope.findByParentId($scope.parentId);//刷新列表
    //             }else{
    //                 alert($scope.message);
    //             }
    //         }
    //     );
    // }


    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        itemCatService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }
    //根据本级id查询下一级
    $scope.findByParentId = function (parentId) {
        $scope.parentId=parentId;
        itemCatService.findByParentId(parentId).success(
            function (response) {
                $scope.list = response;
            });
    }
    //面包屑导航
    //首先我们定义一个级别
    $scope.grade = 1;
    //每次点击下一级，级别+1
    $scope.setGrade = function (value) {
        $scope.grade = value
    }

    //然后定义每个级别的面包屑显示规则
    $scope.selectList = function (p_entity) {
        if ($scope.grade == 1) {
            $scope.entity_1 = null;
            $scope.entity_2 = null;
        }
        if($scope.grade==2){
            $scope.entity_1=p_entity;
            $scope.entity_2=null;
        }
        if($scope.grade==3){
            $scope.entity_2=p_entity;
        }
        $scope.findByParentId(p_entity.id);
    }
});	
