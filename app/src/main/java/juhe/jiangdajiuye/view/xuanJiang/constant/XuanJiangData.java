package juhe.jiangdajiuye.view.xuanJiang.constant;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wangqiang on 2017/6/6.
 */

public class XuanJiangData {
    public String[]   jiangSuCollege = {"南京邮电","何海大学","江南大学", "南京理工","东南大学"
                            ,"南京大学","南京航空航天大学","苏州大学","南京医科大学","南京财经大学"
                            ,"南京信息工程大学"};

    public String[] jiangSuUrls = {"http://njupt.91job.gov.cn/teachin/index",
                            "http://hhu.91job.gov.cn/teachin/index"
                            ,"http://jiangnan.91job.gov.cn/teachin/index"
                            ,"http://njust.91job.gov.cn/teachin/index"
                            ,"http://seu.91job.gov.cn/teachin/index"
                            ,"http://job.nju.edu.cn:9081/login/nju/home.jsp"
                            ,"http://job.nuaa.edu.cn/teachin/index"
                            ,"http://suda.91job.gov.cn/teachin/index"
                            ,"http://njmu.91job.gov.cn/teachin/index"
                            ,"http://njue.91job.gov.cn/teachin/index"
                            ,"http://nuist.91job.gov.cn/teachin/index"};
    public Map<String,String> shangHaiMap = new LinkedHashMap<>();
    public Map<String,String> zheJiangMap = new LinkedHashMap<>();

    public static XuanJiangData getInstance() {
        if(instance == null)
            instance = new XuanJiangData()  ;
        return instance;
    }

    public static XuanJiangData instance;

    private XuanJiangData(){
        shangHaiMap.put("上海交通大学","http://www.job.sjtu.edu.cn/eweb/jygl/zpfw.so");
        shangHaiMap.put("上海理工大学","http://91.usst.edu.cn/InformationNAVList.aspx");
        shangHaiMap.put("复旦大学","http://www.career.fudan.edu.cn/html/qzxx/xjh/1.html");
        zheJiangMap.put("杭州电子科技大学","http://career.hdu.edu.cn/module/getcareers");
        zheJiangMap.put("浙江大学","http://www.career.zju.edu.cn/ejob/zczphxxmorelogin.do");
    }
    public String[] getTitle(int provinceId){
        switch (provinceId){
            case XuanEntranceData.JIANGSU:
                return jiangSuCollege;
            case XuanEntranceData.SHANGHAI:
                return mapToTitle(shangHaiMap);
            case XuanEntranceData.ZHEJIANG:
                return mapToTitle(zheJiangMap);
        }
        return new String[]{};
    }
    public String[] getUrls(int provinceId){
        switch (provinceId){
            case XuanEntranceData.JIANGSU:
                return jiangSuUrls;
            case XuanEntranceData.SHANGHAI:
                return mapToUrl(shangHaiMap);
            case XuanEntranceData.ZHEJIANG:
                return mapToUrl(zheJiangMap);
        }
        return new String[0];
    }
    //获取map的key
    public String[] mapToTitle(Map<String,String> map){
        Iterator<String> iterator = map.keySet().iterator();
        String[] college = new String[map.size()];
        int index =0 ;
        while (iterator.hasNext()){
            college[index++] = iterator.next();
        }
        return college ;
    }
    //获取map的 url
    public String[] mapToUrl(Map<String,String> map){
        Iterator<String> iterator = map.keySet().iterator();
        String[] url = new String[map.size()];
        int index =0 ;
        while (iterator.hasNext()){
            url[index++] = map.get(iterator.next());
        }
        return url ;
    }
}
