package com.leyou.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
@Data
@Table(name = "tb_stock")
public class Stock {
            @Id
            private Long skuId;//` bigint(20) NOT NULL COMMENT '库存对应的商品sku id',
            private Long seckillStock;//` int(9) DEFAULT '0' COMMENT '可秒杀库存',
            private Long seckillTotal;//` int(9) DEFAULT '0' COMMENT '秒杀总数量',
            private Long stock;//` int(9) NOT NULL COMMENT '库存数量',
}
