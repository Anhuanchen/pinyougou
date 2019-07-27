app.service('cartService',function ($http) {
    this.findCartList=function () {
        return $http.get('../cart/findCartList.do');
    }

    this.addGoodsToCartList=function (itemId, num) {
        return $http.get('../cart/addGoodsToCartList.do?itemId='+itemId+'&num='+num);
    }

    this.sum=function (cartList) {
        var totalValue={totalNum:0,totalMoney:0};//合计实体
        for(var i = 0;i<cartList.length;i++){
            for(var j=0;j<cartList[i].orderItemList.length;j++){
                var orderItem = cartList[i].orderItem[j];//购物车明细
                totalValue.totalNum+=orderItem.num;
                totalValue.totalMoney+=orderItem.totalFee;
            }
        }
    }
})