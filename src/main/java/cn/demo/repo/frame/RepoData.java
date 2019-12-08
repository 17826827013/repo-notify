package cn.demo.repo.frame;

import cn.demo.repo.model.Good;
import lombok.Data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ Date  : Create in 13:55 2019/12/2
 */
public class RepoData {

    public static Map<String,Good> CacheDataBase = new LinkedHashMap<>();

    public static List<Ops> ops = new ArrayList<>();

    public static String Today = null;

    @Data
    class Ops{
        String text;
        String value;
    }
}
