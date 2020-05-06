package com.leyou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
@Data
@Table(name = "tb_sku")
public class Sku {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;//` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'sku id',
            private Long spuId;//` bigint(20) NOT NULL COMMENT 'spu id',
            private String title;//` varchar(256) NOT NULL COMMENT '商品标题',
            private String images;//` varchar(1024) DEFAULT '' COMMENT '商品的图片，多个图片以‘,’分割',
            private Long price;//` bigint(15) NOT NULL DEFAULT '0' COMMENT '销售价格，单位为分',
            private String indexes;//` varchar(32) DEFAULT '' COMMENT '特有规格属性在spu属性模板中的对应下标组合',
            private String ownSpec;//` varchar(1024) DEFAULT '' COMMENT 'sku的特有规格参数键值对，json格式，反序列化时请使用linkedHashMap，保证有序',
            private Boolean enable;//` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效，0无效，1有效',
            private Date createTime;//` datetime NOT NULL COMMENT '添加时间',
            private Date lastUpdateTime;//` datetime NOT NULL COMMENT '最后修改时间',
             @Transient
            private Long stock;
}
