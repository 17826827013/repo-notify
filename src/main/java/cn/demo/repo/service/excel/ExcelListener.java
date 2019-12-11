package cn.demo.repo.service.excel;

import cn.demo.repo.frame.RepoData;
import cn.demo.repo.model.Good;
import cn.demo.repo.util.DateUtil;
import cn.demo.repo.util.StringUtils;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

/**
 * @ Date  : Create in 14:54 2019/12/11
 */
public class ExcelListener extends AnalysisEventListener<Good> {

    @Override
    public void invoke(Good good, AnalysisContext analysisContext) {
        if (StringUtils.isNotEmpty(good.getSkuId())){
            good.setCreateTime(DateUtil.currentDate4yyyyMMdd());
            good.setUpdateTime(DateUtil.currentDate4yyyyMMdd());
            RepoData.CacheDataBase.put(good.getSkuId(),good);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
