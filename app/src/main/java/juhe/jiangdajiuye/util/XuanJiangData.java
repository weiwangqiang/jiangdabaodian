package juhe.jiangdajiuye.util;

/**
 * Created by wangqiang on 2017/6/6.
 */

public class XuanJiangData {
    public String[] college = {"南京邮电","何海大学","江南大学", "南京理工","东南大学"
                            ,"南京大学","南京航空航天大学","苏州大学","南京医科大学","南京财经大学"
                            ,"南京信息工程大学"};

    public String[] urls = {"http://njupt.91job.gov.cn/teachin/index",
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
    public String[] getTitle(){
        return college;
    }
    public String[] getUrls(){
        return urls;
    }
}
