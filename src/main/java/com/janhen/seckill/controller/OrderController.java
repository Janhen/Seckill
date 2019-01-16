package com.janhen.seckill.controller;

import com.janhen.seckill.pojo.OrderInfo;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.result.CodeMsg;
import com.janhen.seckill.result.ResultVO;
import com.janhen.seckill.service.GoodsService;
import com.janhen.seckill.service.OrderService;
import com.janhen.seckill.vo.GoodsVo;
import com.janhen.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;
	
	@RequestMapping(value="/detail")
	@ResponseBody
	public ResultVO<OrderDetailVo> info(SeckillUser user,
                                        @RequestParam("orderId") Long orderId) {
		if (user == null) { return ResultVO.error(CodeMsg.SERVER_ERROR); }
		
		// get the object orderinfo
		OrderInfo order = orderService.selectOrderInfoById(orderId);
		if (order == null) {
			return ResultVO.error(CodeMsg.ORDER_NOT_EXIST);
		}
		
		// get the object goodsvo
		Long goodsId = order.getGoodsId();
		GoodsVo goodsVo = goodsService.selectGoodsVoByGoodsId(goodsId);
		
		
		OrderDetailVo orderDetailVo = new OrderDetailVo();
		orderDetailVo.setGoods(goodsVo);
		orderDetailVo.setOrder(order);
		
		return ResultVO.success(orderDetailVo);
	}
}




