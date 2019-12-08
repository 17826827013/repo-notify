package cn.demo.repo.service.repo;

import cn.demo.repo.frame.AjaxResult;
import cn.demo.repo.frame.DataGrid;
import cn.demo.repo.frame.RepoData;
import cn.demo.repo.model.Good;
import cn.demo.repo.service.excel.BaseAnalysisExcel;
import cn.demo.repo.util.DateUtil;
import cn.demo.repo.util.ExcelUtil;
import cn.demo.repo.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.awt.geom.GeneralPath;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @ Date  : Create in 14:01 2019/12/2
 */
@Slf4j
@Service
public class RepoService implements BaseAnalysisExcel {

    @Value("${excel.filePath}")
    String filePath;

    public void executeData(List<Good> list) {
        for (Good good : list) {
            RepoData.CacheDataBase.put(good.getSkuId(), good);
        }
        storageJSONData(RepoData.CacheDataBase);
        log.info("装载excel数据完成并写入本地文件:{}",filePath);
    }

    public DataGrid<Good> search(Good good,int pageNum,int pageSize){
        DataGrid<Good> dataGrid = new DataGrid<>();
        List<Good> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(good.getSkuId())){
            //不知道为啥,但这么写不报错,所以舍近求远
            Object o = RepoData.CacheDataBase.get(good.getSkuId());
            if (o!=null){
                Good g = JSONObject.parseObject(o.toString(),Good.class);
                list.add(g);
            }
            dataGrid.setTotal((long)list.size());
        }else{
            Collection<Good> goods = RepoData.CacheDataBase.values();
            List<Good> gList = new ArrayList<>(goods);
            list = selectStorage(pageNum, pageSize, gList);
            dataGrid.setTotal((long) RepoData.CacheDataBase.size());
        }
        list = isPadding(list);
        dataGrid.setRows(list);
        return dataGrid;
    }

    public AjaxResult updateData(String skuId,Good good){
        good.setUpdateTime(DateUtil.currentDate4yyyyMMdd());
        RepoData.CacheDataBase.put(skuId,good);
        storageJSONData(RepoData.CacheDataBase);
        return AjaxResult.success();
    }

    public AjaxResult save(Good good){
        good.setUpdateTime(DateUtil.currentDate4yyyyMMdd());
        good.setCreateTime(DateUtil.currentDate4yyyyMMdd());
        if (RepoData.CacheDataBase.get(good.getSkuId())!=null){
           return AjaxResult.error("sku已存在,添加新的物品数据失败");
        }
        RepoData.CacheDataBase.put(good.getSkuId(),good);
        storageJSONData(RepoData.CacheDataBase);
        return AjaxResult.success();
    }

    public AjaxResult delete(String skuId){
        RepoData.CacheDataBase.remove(skuId);
        storageJSONData(RepoData.CacheDataBase);
        return AjaxResult.success();
    }

    /**内存分页*/
    public List<Good> selectStorage(Integer pageNum, Integer pageSize, List<Good> goodList) {
        if (pageNum != null && pageSize != null && pageSize > 0) {
            int start = (pageNum - 1) * pageSize;
            int end = pageNum * pageSize;
            if (end > goodList.size()) {
                end = goodList.size();
            }
            return goodList.subList(start, end);
        }
        else {
            return goodList;
        }
    }

    @Override
    public <T> T warpCell(List<Cell> list) {
        Good good = new Good();
        if (!CollectionUtils.isEmpty(list)) {
            if(list.get(0)!=null){
                good.setSkuId(list.get(0) == null ? null : ExcelUtil.getValue(list.get(0)));
                good.setGoodsName(list.get(1) == null ? null : ExcelUtil.getValue(list.get(1)));
//            good.setMiniOrder(list.get(2) == null?null:new Long(ExcelUtil.getValue(list.get(2))));
//            good.setSaveNum(list.get(3) == null?null:new Long(ExcelUtil.getValue(list.get(3))));
                good.setCreateTime(DateUtil.currentDate4yyyyMMdd());
                good.setUpdateTime(DateUtil.currentDate4yyyyMMdd());
            }
        }
        if (good.getSkuId()!=null){
            return (T)good;
        }else {
            return null;
        }
    }

    public void storageJSONData(Map<String,Good> dataMap){

        try {
            // 保证创建一个新文件
            long s = System.currentTimeMillis();
            File file = new File(filePath);
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();
            String jsonString = JSONObject.toJSONString(dataMap);
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
            long e = System.currentTimeMillis();
            log.info("数据json存储耗时:{}",e-s);
        } catch (IOException e) {
           log.error("json文件写失败:{}",e);
        }


    }

    public Map<String,Good> getJSONData(){
        log.info("读取数据json");
        String jsonStr = "";
        long s = System.currentTimeMillis();
        try {
            File file = new File(filePath);
            if (file.exists()){
                FileReader fileReader = new FileReader(file);
                Reader reader = new InputStreamReader(new FileInputStream(file),"utf-8");
                int ch = 0;
                StringBuffer sb = new StringBuffer();
                while ((ch = reader.read()) != -1) {
                    sb.append((char) ch);
                }
                fileReader.close();
                reader.close();
                jsonStr = sb.toString();
                Map<String,Good> map = (Map<String, Good>) JSONObject.parse(jsonStr);
                long e = System.currentTimeMillis();
                log.info("读取数据json耗时:{}",e-s);
                return map;
            }else{
                log.info("当前不存在json数据文件,需要手工上传excel");
            }
        }catch (Exception e){
            log.error("读取文件失败:{}",e);
        }
        return null;
    }

    /**
     * 对是否需要补货状态的判断
     */
    public List<Good> isPadding(List<Good> goods) {
        // Json 转List
        goods = JSON.parseObject(JSON.toJSONString(goods), new TypeReference<List<Good>>() {
        });
        for (Good g : goods) {
            if (g.getSafeNum() == null) {
                g.setIsPadding(Boolean.FALSE.toString());
            } else {
                BigDecimal days = null;
                try {
                    days = new BigDecimal(DateUtil.daysBetween(g.getUpdateTime(),DateUtil.currentDate4yyyyMMdd()));

                    BigDecimal[] results = days.divideAndRemainder(g.getConsumeCycle());
                    if (results.length > 0) {
                        //消耗量计算
                        BigDecimal cons = results[0].multiply(g.getConsumeRatio());
                        BigDecimal remnantNum = g.getRemnantNum();
                        //安全库存  和  (上次补货库存-消耗量) 进行比较
                        g.setIsPadding(g.getSafeNum().compareTo(remnantNum.subtract(cons)) > 0 ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
                    }
                } catch (Exception e) {
                    log.error("比较计算失败", e);

                }
            }
        }
        return goods;
    }


}
