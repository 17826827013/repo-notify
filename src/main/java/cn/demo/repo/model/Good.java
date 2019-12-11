package cn.demo.repo.model;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @ Date  : Create in 14:31 2019/12/2
 */
@Data
public class Good implements Serializable {


    /**主键id*/
    @ExcelProperty(value = "SKU / Material Number" ,index = 0)
    private String skuId;
    /**商品名称*/
    @ExcelProperty(value = "Chinese name" , index = 1)
    private String goodsName;
    /**最小数量*/
    @ExcelProperty(value = "Mini Order" , index = 2)
    private String miniOrder;
    /**安全库存*/
    @ExcelProperty(value = "安全库存" , index = 3)
    private BigDecimal safeNum;
    /**消耗速率*/
    @ExcelProperty(value = "消耗速率" , index = 4)
    private BigDecimal consumeRatio;
    /**消耗周期(天)*/
    @ExcelProperty(value = "消耗周期" , index = 5)
    private BigDecimal consumeCycle;
    /**创建时间*/
    private String createTime;
    /**更新时间*/
    private String updateTime;
    /**是否需要补货*/
    private String isPadding;
    /**
     * 当前库存:
     * 值变动: Excel 上传解析
     * 值来源: 上次补货库存 - 消耗速率 * 消耗周期 / (当前时间 - 上次补货时间(updateTime))
     * 值用途: Excel 导出 ,页面List展示
     * */
    @ExcelProperty(value = "当前库存" , index = 6)
    private BigDecimal currentNum;
    /**
     * 上次补货库存:
     * 值来源:Excel 上传解析
     * 值变动:编辑手动补货或者 Excel 上传解析
     * */
    @ExcelProperty(value = "上次补货库存" , index = 7)
    private BigDecimal remnantNum;

}
