package com.janhen.seckill.controller;

import com.janhen.seckill.pojo.OrderInfo;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.common.ResultVO;
import com.janhen.seckill.service.IGoodsService;
import com.janhen.seckill.service.IOrderService;
import com.janhen.seckill.vo.SeckillGoodsVO;
import com.janhen.seckill.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order/")
public class OrderController {
	
	@Autowired
	IOrderService iOrderService;
	
	@Autowired
	IGoodsService iGoodsService;
	
	@RequestMapping(value="detail")
	@ResponseBody
	public ResultVO<OrderDetailVO> info(SeckillUser user, @RequestParam("orderId") Long orderId) {
		if (user == null) {
			return ResultVO.error(ResultEnum.SERVER_ERROR);
		}
		OrderInfo order = iOrderService.selectOrderInfoById(orderId);
		// assemble orderDetailVO
		Long goodsId = order.getGoodsId();
		SeckillGoodsVO seckillGoodsVO = iGoodsService.selectGoodsVoByGoodsId(goodsId);

		OrderDetailVO orderDetailVO = new OrderDetailVO();
		orderDetailVO.setGoods(seckillGoodsVO);
		orderDetailVO.setOrder(order);
		return ResultVO.success(orderDetailVO);
	}
}




