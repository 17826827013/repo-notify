package cn.demo.repo.controller;

import cn.demo.repo.frame.AjaxResult;
import cn.demo.repo.frame.DataGrid;
import cn.demo.repo.frame.RepoData;
import cn.demo.repo.model.Good;
import cn.demo.repo.service.excel.ExcelListener;
import cn.demo.repo.service.repo.RepoService;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ Date  : Create in 11:34 2019/12/2
 */
@Controller
@Slf4j
public class RepoController {

    @Autowired
    RepoService repoService;
    @Value("${excel.filePath}")
    String filePath;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/toUpdate")
    public String toUpdatePage(String skuId,HttpServletRequest request){
        request.setAttribute("Good",RepoData.CacheDataBase.get(skuId));
        return "edit";
    }

    @RequestMapping("/toSave")
    public String toSavePage(){
        return "add";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public AjaxResult upload(HttpServletRequest request){
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        long s = System.currentTimeMillis();
        Map<String, MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();
        log.info("上传excel 解析");
        //初始化
        RepoData.CacheDataBase = new LinkedHashMap<>();
        fileMap.forEach((k,v)->{
            try {
                InputStream io = v.getInputStream();
                EasyExcel.read(io,Good.class,new ExcelListener()).doReadAll();
            } catch (Exception e) {
                log.error("解析错误Excel:{}",e);
            }
        });
        long e = System.currentTimeMillis();
        repoService.storageJSONData(RepoData.CacheDataBase);
        log.info("装载excel数据完成并写入本地文件:{}",filePath);
        log.info("excel数据解析耗时:{}",e-s);
        return AjaxResult.success();
    }

    @RequestMapping("/import")
    @ResponseBody
    public AjaxResult importData(){
        return AjaxResult.success();
    }

    @RequestMapping("/save")
    @ResponseBody
    public AjaxResult saveData(Good good){
        return repoService.save(good);
    }

    @RequestMapping("/update")
    @ResponseBody
    public AjaxResult updateData(String skuId,Good good){
        AjaxResult result =repoService.updateData(skuId,good);
        return result;
    }

    @RequestMapping("/search")
    @ResponseBody
    public DataGrid search(int pageNumber, int pageSize,Good good){
        return repoService.search(good,pageNumber,pageSize);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public AjaxResult deleteData(String skuId){
        return repoService.delete(skuId);
    }
}
