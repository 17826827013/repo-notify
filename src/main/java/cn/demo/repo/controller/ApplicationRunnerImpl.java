package cn.demo.repo.controller;

import cn.demo.repo.frame.RepoData;
import cn.demo.repo.model.Good;
import cn.demo.repo.service.repo.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ Date  : Create in 10:53 2019/12/3
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    RepoService repoService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //初始化数据
        Map<String,Good> data =  repoService.getJSONData();
        if (data!=null){
            RepoData.CacheDataBase = data;
        }
   }

}
