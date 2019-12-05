package cn.demo.repo.model;


import lombok.Data;


/**
 * @ Date  : Create in 14:31 2019/12/2
 */
@Data
public class Good {
    /**主键id*/
    private String skuId;
    /**商品名称*/
    private String goodsName;
    /**最小数量*/
    private Long miniOrder;
    /**安全库存*/
    private Long saveNum;
    /**剩余库存*/
    private Long remnantNum;
    /**消耗速率(1,2)  一天两个*/
    private String consumeRatio;
    /**创建时间*/
    private String createTime;
    /**更新时间*/
    private String updateTime;

}
