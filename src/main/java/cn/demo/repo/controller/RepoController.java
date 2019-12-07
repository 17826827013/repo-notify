package cn.demo.repo.controller;

import cn.demo.repo.frame.AjaxResult;
import cn.demo.repo.frame.DataGrid;
import cn.demo.repo.frame.RepoData;
import cn.demo.repo.model.Good;
import cn.demo.repo.service.excel.BaseAnalysisExcel;
import cn.demo.repo.service.excel.ExcelService;
import cn.demo.repo.service.repo.RepoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ Date  : Create in 11:34 2019/12/2
 */
@Controller
@Slf4j
public class RepoController {

    @Autowired
    ExcelService excelService;

    @Autowired
    RepoService repoService;


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

        return "edit";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public AjaxResult upload(HttpServletRequest request){
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();
        log.info("上传excel 解析");
        fileMap.forEach((k,v)->{
            long s = System.currentTimeMillis();
            List<Good> list ;
            Workbook wb = excelService.fileTransToWorkbook(v);
            long e = System.currentTimeMillis();
            log.info("excel数据解析耗时:{}",e-s);
            list = excelService.analysiscWorkbook(wb,repoService);
            repoService.executeData(list);
        });
        return AjaxResult.success();
    }

    @RequestMapping("/import")
    @ResponseBody
    public AjaxResult importData(){
        return AjaxResult.success();
    }

    @RequestMapping("/save")
    @ResponseBody
    public AjaxResult saveData(){
        return AjaxResult.success();
    }

    @RequestMapping("/update")
    @ResponseBody
    public AjaxResult updateData(String skuId,Good good){
        return repoService.updateData(skuId,good);
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
