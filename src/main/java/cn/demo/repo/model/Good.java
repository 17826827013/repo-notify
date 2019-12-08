package cn.demo.repo.model;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @ Date  : Create in 14:31 2019/12/2
 */
@Data
public class Good implements Serializable {
    /**主键id*/
    private String skuId;
    /**商品名称*/
    private String goodsName;
    /**最小数量*/
    private BigDecimal miniOrder;
    /**安全库存*/
    private BigDecimal safeNum;
    /**库存量*/
    private BigDecimal remnantNum;
    /**消耗速率*/
    private BigDecimal consumeRatio;
    /**消耗周期(天)*/
    private BigDecimal consumeCycle;
    /**创建时间*/
    private String createTime;
    /**更新时间*/
    private String updateTime;
    /**是否需要补货*/
    private String isPadding;
}
