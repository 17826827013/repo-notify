package cn.demo.repo.service.repo;

import cn.demo.repo.frame.AjaxResult;
import cn.demo.repo.frame.DataGrid;
import cn.demo.repo.frame.RepoData;
import cn.demo.repo.model.Good;
import cn.demo.repo.util.DateUtil;
import cn.demo.repo.util.StringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ Date  : Create in 14:01 2019/12/2
 */
@Slf4j
@Service
public class RepoService  {

    @Value("${excel.filePath}")
    String filePath;

    @Value("${excel.fileParam}")
    String fileParam;

    public void executeData(List<Good> list) {
        RepoData.CacheDataBase = new LinkedHashMap<>();
        for (Good good : list) {
            RepoData.CacheDataBase.put(good.getSkuId(), good);
        }
        storageJSONData(RepoData.CacheDataBase);
        log.info("装载excel数据完成并写入本地文件:{}",filePath);
    }

    public DataGrid<Good> search(Good good,Integer pageNum,Integer pageSize){
        DataGrid<Good> dataGrid = new DataGrid<>();
        List<Good> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(good.getSkuId())){
            //不知道为啥,但这么写不报错,所以舍近求远
            Object o = RepoData.CacheDataBase.get(good.getSkuId());
            if (o != null) {
                Good g = JSON.parseObject(JSON.toJSONString(o), new TypeReference<Good>() {
                });
                list.add(g);
            }
            dataGrid.setTotal((long)list.size());
        }else{
            Collection<Good> goods = RepoData.CacheDataBase.values();
            List<Good> gList = new ArrayList<>(goods);
            list = selectStorage(pageNum, pageSize, gList);
            dataGrid.setTotal((long) RepoData.CacheDataBase.size());
        }
        list = dataHandler(list);
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
                //为了保证序列化时的 map 顺序 使用这种序列话的方式 会增加一倍左右的序列化时间
                LinkedHashMap<String, Good> json = JSON.parseObject(jsonStr,LinkedHashMap.class, Feature.OrderedField);
                JSONObject jsonObject=new JSONObject(true);
                jsonObject.putAll(json);

//                Map<String,Good> map = (Map<String, Good>) JSONObject.parse(jsonStr);
                long e = System.currentTimeMillis();
                log.info("读取数据json耗时:{}",e-s);
                return json;
            }else{
                log.info("当前不存在json数据文件,需要手工上传excel");
            }
        }catch (Exception e){
            log.error("读取文件失败:{}",e);
        }
        return null;
    }

    /**
     * 对数据进行处理
     * 1.判断是否需要补货
     * 2.计算当前库存
     */
    public List<Good> dataHandler(List<Good> goods) {
        // Json 转List
        goods = JSON.parseObject(JSON.toJSONString(goods), new TypeReference<List<Good>>() {
        });
        for (Good g : goods) {
            if (g.getSafeNum() == null) {
                g.setIsPadding(Boolean.FALSE.toString());
            } else {
                BigDecimal days ;
                BigDecimal safeNum = g.getSafeNum();
                try {
                    days = new BigDecimal(DateUtil.daysBetween(g.getUpdateTime(), DateUtil.currentDate4yyyyMMdd()));
                    days.add(new BigDecimal("7"));
                    BigDecimal[] results = days.divideAndRemainder(g.getConsumeCycle());
                    if (results.length > 0) {
                        //消耗量计算
                        BigDecimal cons = results[0].multiply(g.getConsumeRatio());
                        //上次补货库存
                        BigDecimal remnantNum = g.getRemnantNum() == null ? BigDecimal.ZERO : g.getRemnantNum();
                        //当前库存
                        BigDecimal currentNum = remnantNum.subtract(cons);
                        g.setCurrentNum(currentNum);
                        //安全库存  和  (上次补货库存-消耗量) 进行比较
                        g.setIsPadding(safeNum.compareTo(currentNum) > 0 ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
                    }
                } catch (Exception e) {
                    log.error("数据处理失败", e);
                }
            }
        }
        return goods;
    }

    public void excelImport(HttpServletResponse response, List<Good> data,String fileName) throws IOException {
        String sheetName = null;
        String[] sheetParam = fileParam.split("/");
        int sheetNum = sheetParam.length;
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), Good.class).build();

        WriteSheet writeSheet ;
        for (int i= 0;i<sheetNum;i++){
            String[] param = sheetParam[i].split(",");
            if (param.length==3){
                writeSheet = EasyExcel.writerSheet(i, param[0]).build();
                List<Good> dataList = data.subList(
                        new Integer(param[1]).compareTo(data.size())>1?data.size():new Integer(param[1])
                        ,new Integer(param[2]).compareTo(data.size()>1?data.size():new Integer(param[2]))
                );
                excelWriter.write(dataList,writeSheet);
            }
        }

        excelWriter.finish();

    }
}
