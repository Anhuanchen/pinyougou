package com.pinyougou.order.service.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import PageResult.PageResult;
import com.pinyougou.common.utils.IdWorker;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.mapper.TbPayLogMapper;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderExample;
import com.pinyougou.pojo.TbOrderExample.Criteria;
import com.pinyougou.order.service.OrderService;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbPayLogMapper payLogMapper;
	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	@Autowired
	private IdWorker idWorker;
	/**
	 * 增加
	 */
	@Override
	public void add(TbOrder order) {
		//得到购物车数据
		List<Cart> itemList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());
		//订单ID列表
		List<String> orderList = new ArrayList<>();
		double total_money=0;//总金额
		for (Cart cart : itemList) {
			long orderId = idWorker.nextId();
			System.out.println("sellerId:"+cart.getSellerId());
			TbOrder tbOrder = new TbOrder();//新建订单对象
			tbOrder.setOrderId(orderId);//订单ID
			tbOrder.setUserId(order.getUserId());//用户名
			tbOrder.setPaymentType(order.getPaymentType());//支付类型
			tbOrder.setStatus("1");//状态，未付款
			tbOrder.setCreateTime(new Date());//订单创建日期
			tbOrder.setUpdateTime(new Date());//订单更新日期
			tbOrder.setReceiverAreaName(order.getReceiverAreaName());//地址
			tbOrder.setReceiver(order.getReceiver());//收货人
			tbOrder.setSourceType(order.getSourceType());//订单来源
			tbOrder.setSellerId(cart.getSellerId());

			//循环购物车明细
			double money = 0;
			for (TbOrderItem tbOrderItem : cart.getOrderItemList()) {
				tbOrderItem.setId(idWorker.nextId());
				tbOrderItem.setOrderId(orderId);//订单ID
				tbOrderItem.setSellerId(cart.getSellerId());
				money +=tbOrderItem.getTotalFee().doubleValue();//金额累加
				orderItemMapper.insert(tbOrderItem);
			}
			tbOrder.setPayment(new BigDecimal(money));

			orderList.add(orderId+"");//添加到订单列表
			total_money+=money;//累加到总金额
			orderMapper.insert(tbOrder);
		}
		if("1".equals(order.getPaymentType())){//如果是微信支付
			TbPayLog payLog = new TbPayLog();
			String outTradeNo = idWorker.nextId()+"";//支付订单号
			payLog.setOutTradeNo(outTradeNo);//支付订单号
			payLog.setCreateTime(new Date());//创建时间
			//订单号列表，逗号分割
			String ids = orderList.toString().replace("[", "").replace("]", "").replace(" ","");
			payLog.setOrderList(ids);
			payLog.setPayType("1");//支付类型
			payLog.setTotalFee((long)(total_money*100));//总金额/分
			payLog.setTradeState("0");//支付状态
			payLog.setUserId(order.getUserId());
			payLogMapper.insert(payLog);//插入到支付日志表
			redisTemplate.boundHashOps("payLog").put(order.getUserId(),payLog);//放入缓存
		}
		//删除redis缓存
		redisTemplate.boundHashOps("cartList").delete(order.getUserId());
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 根据用户id查询payLog
	 * @param userId
	 * @return
	 */
	@Override
	public TbPayLog searchPayLogFromRedis(String userId) {
		return (TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);
	}

	/**
	 * 修改订单状态
	 * @param out_trade_no 支付订单号
	 * @param transaction_id  微信返回的交易流水号
	 */
	@Override
	public void updateOrderStatus(String out_trade_no, String transaction_id) {
		//修改日志状态
		TbPayLog payLog = payLogMapper.selectByPrimaryKey(out_trade_no);
		payLog.setPayTime(new Date());
		payLog.setTradeState("1");//已支付
		payLog.setTransactionId(transaction_id);//交易号
		payLogMapper.updateByPrimaryKey(payLog);
		//修改订单状态
		String orderList = payLog.getOrderList();//获取订单号列表
		String[] orderIds = orderList.split(",");//获取订单号数组
		for (String orderId : orderIds) {
			TbOrder order = orderMapper.selectByPrimaryKey(Long.parseLong(orderId));
			if (order!=null){
				order.setStatus("2");//已付款
				orderMapper.updateByPrimaryKey(order);
			}
		}
		//清除redis缓存
		redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
	}

}
