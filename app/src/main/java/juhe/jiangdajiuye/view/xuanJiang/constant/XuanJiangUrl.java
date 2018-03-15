package juhe.jiangdajiuye.view.xuanJiang.constant;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wangqiang on 2017/6/6.
 */

public class XuanJiangUrl {
    private Map<String, String> shangHaiMap = new LinkedHashMap<>();//上海市的参数
    private Map<String, String> zheJiangMap = new LinkedHashMap<>();//浙江省的参数
    private Map<String, String> JiangSuMap = new LinkedHashMap<>();//江苏省的参数

    public static XuanJiangUrl getInstance() {
        if (instance == null)
            instance = new XuanJiangUrl();
        return instance;
    }

    public static XuanJiangUrl instance;

    private XuanJiangUrl() {
        JiangSuMap.put("南京邮电","http://njupt.91job.gov.cn/teachin/index");
        JiangSuMap.put("河海大学","http://hhu.91job.gov.cn/teachin/index");
        JiangSuMap.put("江南大学","http://jiangnan.91job.gov.cn/teachin/index");
        JiangSuMap.put("江苏科技大学","http://just.91job.gov.cn/teachin/index");
        JiangSuMap.put("南京理工","http://njust.91job.gov.cn/teachin/index");
        JiangSuMap.put("东南大学","http://seu.91job.gov.cn/teachin/index");
        JiangSuMap.put("南京大学","http://job.nju.edu.cn:9081/login/nju/home.jsp");
        JiangSuMap.put("南京航空航天大学","http://job.nuaa.edu.cn/teachin/index");
        JiangSuMap.put("苏州大学","http://suda.91job.gov.cn/teachin/index");
        JiangSuMap.put("南京农业大学","http://njau.91job.gov.cn/teachin/index");
        JiangSuMap.put("南京医科大学","http://njmu.91job.gov.cn/teachin/index");
        JiangSuMap.put("南京财经大学","http://njue.91job.gov.cn/teachin/index");
        JiangSuMap.put("南京信息工程大学","http://nuist.91job.gov.cn/teachin/index");
        JiangSuMap.put("南京工业大学","http://jiuye.njtech.edu.cn/teachin/index");
        JiangSuMap.put("南京林业大学","http://njfu.91job.gov.cn/teachin/index");
        JiangSuMap.put("南京师范大学","http://njnu.jysd.com/teachin/index");
        JiangSuMap.put("中国药科大学","http://cpu.91job.gov.cn/teachin/index");
        JiangSuMap.put("中国矿业大学","http://jyzd.cumt.edu.cn/teachin/index");

        shangHaiMap.put("上海交通大学", "http://www.job.sjtu.edu.cn/eweb/jygl/zpfw.so");
//        shangHaiMap.put("上海理工大学", "http://91.usst.edu.cn/InformationNAVList.aspx");
        shangHaiMap.put("复旦大学", "http://www.career.fudan.edu.cn/html/qzxx/xjh/1.html");
        shangHaiMap.put("上海财经大学", "http://careersys.sufe.edu.cn/pros_jiuye/s/zxh/owebsiteData/preaching");

        zheJiangMap.put("杭州电子科技大学", "http://career.hdu.edu.cn/module/getcareers");
        zheJiangMap.put("浙江大学", "http://www.career.zju.edu.cn/ejob/zczphxxmorelogin.do");
        zheJiangMap.put("浙江工业大学", "http://zjut.jysd.com/teachin/index");
        zheJiangMap.put("浙江师范大学", "http://zjnu.jysd.com/teachin/index");
        zheJiangMap.put("中国计量大学", "http://jyb.cjlu.edu.cn/teachin/index");
        zheJiangMap.put("杭州师范大学", "http://job.hznu.edu.cn/view/ind_applies.do");
    }

    public String[] getTitle(int provinceId) {
        switch (provinceId) {
            case XuanEntranceData.JIANGSU:
                return mapToTitle(JiangSuMap);
            case XuanEntranceData.SHANGHAI:
                return mapToTitle(shangHaiMap);
            case XuanEntranceData.ZHEJIANG:
                return mapToTitle(zheJiangMap);
        }
        return new String[]{};
    }

    public String[] getUrls(int provinceId) {
        switch (provinceId) {
            case XuanEntranceData.JIANGSU:
                return mapToUrl(JiangSuMap);
            case XuanEntranceData.SHANGHAI:
                return mapToUrl(shangHaiMap);
            case XuanEntranceData.ZHEJIANG:
                return mapToUrl(zheJiangMap);
        }
        return new String[0];
    }

    //获取map的key
    public String[] mapToTitle(Map<String, String> map) {
        Iterator<String> iterator = map.keySet().iterator();
        String[] college = new String[map.size()];
        int index = 0;
        while (iterator.hasNext()) {
            college[index++] = iterator.next();
        }
        return college;
    }

    //获取map的 url
    public String[] mapToUrl(Map<String, String> map) {
        Iterator<String> iterator = map.keySet().iterator();
        String[] url = new String[map.size()];
        int index = 0;
        while (iterator.hasNext()) {
            url[index++] = map.get(iterator.next());
        }
        return url;
    }
}
