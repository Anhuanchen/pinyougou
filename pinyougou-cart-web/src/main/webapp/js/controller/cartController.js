app.controller('cartController',function ($scope, cartService) {
   $scope.findCartList=function () {
       cartService.findCartList().success(
         function (response) {
             $scope.cartList=response;
             $scope.totalValue=cartService.sum($scope.cartList)
         }
       );
   }

   $scope.addGoodsToCartList=function (itemId, num) {
       cartService.addGoodsToCartList(itemId, num).success(
         function (response) {
             if(response.success){
                 $scope.findCartList();//刷新列表
             }else{
                 alert(response.message);//弹出错误提示
             }
         }
       );
   }

   //查询当前登陆人的地址列表
    $scope.findAddressList=function () {
       cartService.findAddressList().success(
         function (response) {
             $scope.addressList=response;
             for (var i = 0; i <$scope.addressList.length ; i++) {
                 if($scope.addressList[i].isDefault=='1'){
                     $scope.address=$scope.addressList[i];
                     break;
                 }
             }
         }
       );
    }

    //选择地址【当点击的时候，把address信息存入$scope域,这样就可以在全局使用被选中的address】
    $scope.selectAddress=function (address) {
        $scope.address=address;
    }

    //判断是否是当前选中的地址【循环遍历，看是不是刚才点击存储的address，是就选中，否则就不选中】
    $scope.isSelectedAddress=function (address) {
        if(address==$scope.address){
            return true;
        }else{
            return false;
        }
    }

    $scope.order={paymentType:'1'}
    //选择支付方式
    $scope.selectPayType=function (type) {
        $scope.order.paymentType=type;
    }

    //保存订单
    $scope.submitOrder=function () {
        $scope.order.receiverAreaName=$scope.address.address;//地址
        $scope.order.receiverMobile=$scope.address.mobile;//手机
        $scope.order.receiver=$scope.address.contact;//联系人
        cartService.submitOrder($scope.order).success(
            function (response) {
                if(response.success){
                    if($scope.order.paymentType=='1'){
                        location.href="pay.html";
                    }else{//如果是货到付款
                        location.href="paysuccess.html";
                    }
                }else{
                        alert(response.message);//也可以跳转到提示页面
                }
            }
        );
    }

});