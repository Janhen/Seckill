package com.janhen.seckill.vo;

import com.janhen.seckill.common.Const;
import com.janhen.seckill.pojo.SeckillUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDetailVO {

	private int         seckillStatus	= Const.SeckillStatusEnum.NOT_BEGIN.getCode();
	private int         remainSeconds	= 0;
	private SeckillUser user;
	private GoodsVO     goods;
}
