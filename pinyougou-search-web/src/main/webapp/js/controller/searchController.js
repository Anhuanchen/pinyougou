app.controller('searchController', function ($scope,$location, searchService) {
    //添加搜索项
    $scope.searchMap = {
        'keywords': '',
        'category': '',
        'brand': '',
        'spec': {},
        'price': '',
        'pageNo': 1,
        'pageSize': 20,
        'sortField':'',
        'sort':''
    }

    $scope.search = function () {
        //执行查询之前，转换类型
        $scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo);
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
                buildPageLabel();//查询得出结果，然后调用分页方法
            });
    }

    //添加搜索项
    $scope.addSearchItem = function (key, value) {
        if (key == 'category' || key == 'brand' || key == 'price') {
            //分类或者品牌
            $scope.searchMap[key] = value;
        } else {
            //规格
            $scope.searchMap.spec[key] = value;
        }
        $scope.search();
    }

    //移除复合搜索条件[根据key移除]
    $scope.removeSearchItem = function (key) {
        if (key == "category" || key == "brand" || key == 'price') {
            //如果是分类或者品牌
            $scope.searchMap[key] = '';
        } else {
            //移除此属性
            delete $scope.searchMap.spec[key];
        }
        $scope.search();
    }

    //构建分页标签
    buildPageLabel = function () {
        $scope.pageLabel = [];//新增分页栏属性
        var maxPageNo = $scope.resultMap.totalPages;//得到最后页码
        var firstPage = 1;//开始页码
        var lastPage = maxPageNo;//截至页码
        if ($scope.resultMap.totalPages > 5) {//总页数大于5，显示部分页数
            if ($scope.searchMap.pageNo <= 3) {//如果当前页小于3
                lastPage = 5;
            } else if ($scope.searchMap.pageNo >= lastPage - 2) {//如果当前页大于等于总页码-2
                firstPage = lastPage - 4;
            } else {//显示当前页为中心的5页
                firstPage = $scope.searchMap.pageNo - 2;
                lastPage = $scope.searchMap.pageNo + 2;
            }
        }
        for (var i = firstPage; i <= lastPage; i++) {
            $scope.pageLabel.push(i);
        }
    }

    $scope.queryByPage = function (pageNo) {
        //验证页码为正确页码
        if (pageNo < 1 || pageNo > $scope.totalPages) {
            return;
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search();
    }
    //是否是第一页
    $scope.isTopPage = function () {
        if ($scope.searchMap.pageNo == 1) {
            return true;
        } else {
            return false;
        }
    }
    //是否是最后一页
    $scope.isEndPage = function () {
        if ($scope.searchMap.pageNo == $scope.resultMap.totalPages) {
            return true;
        } else {
            return false;
        }
    }
    //是否显示第一个省略号
    $scope.isShowFirst = function () {
        if ($scope.resultMap.totalPages > 5) {
            if ($scope.searchMap.pageNo > 3) {
                return true
            } else {
                return false;
            }
        }
        return false;

    }
    //是否显示最后一个省略号
    $scope.isShowLast = function () {
        if ($scope.resultMap.totalPages > 5) {
            if ($scope.searchMap.pageNo < $scope.resultMap.totalPages - 2) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //设置排序规则
    $scope.sortSearch=function (sortField, sort) {
        $scope.searchMap.sortField=sortField;
        $scope.searchMap.sort=sort;
        $scope.search();
    }

    //判断关键字是不是品牌
    $scope.keywordsIsBrand=function () {
        for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
            if($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){
                //包含，就不显示，需要返回false
                return false;
            }
        }
        return true;
    }

    //加载字符串
    $scope.loadkeywords=function () {
        $scope.searchMap.keywords=$location.search()['keywords'];
        $scope.search();
    }

})