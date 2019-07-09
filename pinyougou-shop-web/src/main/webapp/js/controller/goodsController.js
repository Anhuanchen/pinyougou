//控制层
app.controller('goodsController', function ($scope, $controller, $location, goodsService, uploadService, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }


    //查询实体
    $scope.findOne = function () {
        var id = $location.search()['id'];//获取参数
        if (id == null) {
            //说明是新增，返回
            return;
        }

        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
                //向富文本编辑器里添加商品介绍
                editor.html($scope.entity.goodsDesc.introduction);
                //显示图片列表
                $scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);
                //显示扩展属性【这里会冲突】
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                //规格
                $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);
            }
        );
    }

    //根据规格名称和选项名称返回是否被勾选
    $scope.checkAttributeValue = function (specName,optionName) {
        var item = $scope.entity.goodsDesc.specificationItems;
        var object = $scope.searchObjectBykey(item, 'attributeName', specName);
        if (object == null) {
            return false;
        } else {
            if (object.attributeValue.indexOf(optionName) >= 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }

    //增加
    $scope.add = function () {
        $scope.entity.goodsDesc.introduction = editor.html();
        goodsService.add($scope.entity).success(
            function (response) {
                if (response.success) {
                    alert('保存成功');
                    $scope.entity = {};
                    editor.html(""); //清空富文本编辑器
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.totalCount;//更新总记录数
            }
        );
    }

    //分页
    // $scope.findPage = function (page, rows) {
    //     goodsService.findPage(page, rows).success(
    //         function (response) {
    //             $scope.list = response.rows;
    //             $scope.paginationConf.totalItems = response.total;//更新总记录数
    //         }
    //     );
    // }
    //文件上传
    $scope.uploadFile = function () {

        uploadService.uploadFile().success(
            function (response) {
                if (response.success) {
                    $scope.image_entity.url = response.message;
                    ;
                } else {
                    alert(response.message);
                }
            }).error(function () {
            alert("上传发生错误");
        })
    }
    //添加图片列表
    $scope.entity = {goods: {}, goodsDesc: {itemImages: [], specificationItems: []}}; //定义页面实体结构

    //添加图片列表
    $scope.add_image_entity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    //列表中移除图片
    $scope.remove_image_entity = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index, 1);
    }

    //读取一级分类
    $scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(
            function (response) {
                $scope.itemCat1List = response;
            });
    }
    //读取二级分类【当category1Id发生变化的时候就执行该方法】
    $scope.$watch('entity.tbGoods.category1Id', function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat2List = response;
        });
    });
    //读取三级分类【当category2Id发生变化的时候就执行该方法】
    $scope.$watch('entity.tbGoods.category2Id', function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat3List = response;
        });
    });
    //读取typeid【当第三级分类被确定以后】
    $scope.$watch('entity.tbGoods.category3Id', function (newValue, oldValue) {
        itemCatService.findOne(newValue).success(function (response) {
            $scope.entity.tbGoods.typeTemplateId = response.typeId;
        })
    });
    //实现扩展属性【当模板id发生变化的时候】
    //实现品牌下拉列表
    //实现规格
    $scope.$watch('entity.tbGoods.typeTemplateId', function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(function (response) {
            $scope.typeTemplate = response;
            $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds); //品牌列表，将其转换为json
            if($location.search()['id']==null){
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
            }
        });
        typeTemplateService.findSpecList(newValue).success(function (response) {
            $scope.SpecList = response;
        });
    });
    //保存选中规格选项，即对应的name和value
    $scope.updateSpecAttribute = function ($event, name, value) {
        var object = $scope.searchObjectBykey($scope.entity.goodsDesc.specificationItems, 'attributeName', name);
        if (object != null) {
            //说明已经goodsDesc中已经存在这个attributeName，也就是attributeName已经被选中了
            if ($event.target.checked) {
                //勾选attributevalue
                object.attributeValue.push(value);
            } else {
                //取消勾选
                object.attributeValue.splice(object.attributeValue.indexOf(value), 1);
                //判断这个选项中是否全部被删除，如果是，就将此选项也删除
                if (object.attributeValue.length == 0) {
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object), 1);
                }

            }

        } else {
            //说明goodsDesc中不存在这个attributeName，没有被选中，需要添加整体的specificaitonItems
            $scope.entity.goodsDesc.specificationItems.push({"attributeName": name, "attributeValue": [value]});
        }
    }

    //创建SKU列表
    $scope.createItemList = function () {
        $scope.entity.itemList = [{spec: {}, price: 0, num: 99999, status: '0', isDefault: '0'}]
        var items = $scope.entity.goodsDesc.specificationItems;//得到specificationItems对象
        for (var i = 0; i < items.length; i++) {//得到单个对象
            $scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue)
        }
    }

    //定义方法添加列值
    addColumn = function (list, columnName, columnValues) {
        var newList = [];//新的集合
        for (var i = 0; i < list.length; i++) {
            var oldRow = list[i];
            for (var j = 0; j < columnValues.length; j++) {
                //深克隆
                var newRow = JSON.parse(JSON.stringify(oldRow));
                newRow.spec[columnName] = columnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    }


    $scope.status = ['未审核', '已审核', '审核未通过', '关闭'];//商品状态

    $scope.itemCatList = [];

    //加载商品分类列表,根据id，设置name
    $scope.findItemCatList = function () {
        itemCatService.findAll().success(
            function (response) {
                for (var i = 0; i < response.length; i++) {
                    //根据id获取name
                    $scope.itemCatList[response[i].id] = response[i].name
                }
            });
    }
});	
