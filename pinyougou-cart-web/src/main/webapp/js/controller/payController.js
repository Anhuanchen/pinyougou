app.controller('payController' ,function($scope ,$location,payService){
    //生成二维码
    $scope.createNative=function () {
        payService.createNative().success(
          function (response) {
              $scope.money=(response.total_fee/100).toFixed(2);//金额
              $scope.out_trade_no= response.out_trade_no;//订单号
            //二维码
              var qr = new QRious({
                 element:document.getElementById('qrious'),
                  size:250,
                  level:'H',
                  value:response.code_url
              });
              QueryPayStatus(response.out_trade_no);
          }
        );
    }


        var QueryPayStatus=function (out_trade_no) {
        payService.queryPayStatus(out_trade_no).success(
          function (response) {
              $scope.responseMessage=response;
              if(response.success){
                  location.href="paysuccess.html#?money="+$scope.money;
              }else{
                  if(response.message='二维码超时'){
                      $scope.createNative();//重新生成二维码
                  }else{
                      location.href="payfail.html";
                  }
              }
          }
        );
    }

    $scope.getMoney=function () {

        return $location.search()['money'];
    }

});