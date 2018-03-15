package juhe.jiangdajiuye.view.xuanJiang.control;

import juhe.jiangdajiuye.view.xuanJiang.constant.XuanEntranceData;
import juhe.jiangdajiuye.view.xuanJiang.entity.MesItemHolder;

/**
 * class description here
 *
 * 获取宣讲URL的信息
 *
 * @author wangqiang
 * @since 2017-09-30
 */

public class XuanUrlControl {
    private static final String TAG = "JobUrlControl";
    public static XuanUrlControl instance;
    public static XuanUrlControl getInStance(){
        if(instance == null)
            instance = new XuanUrlControl();
        return instance ;
    }
    public String getUrl(MesItemHolder holder){
        switch (holder.getProvinceId()){
            case XuanEntranceData.JIANGSU:
                return getJiangSuUrl(holder);
            case XuanEntranceData.SHANGHAI:
                return getShanghaiUrl(holder);
            case XuanEntranceData.ZHEJIANG:
                return getZheJiangUrl(holder);
        }
        return "";
    }
//----------------------------------------------------------------------
    private String getZheJiangUrl(MesItemHolder holder) {
        switch (holder.getCollege()){
            case "杭州电子科技大学":
                return getHangZhouDianZi(holder);
            case "浙江大学":
                return getZheJiangDaXue(holder);
            case "杭州师范大学":
                return getHangZhouShiFan(holder);
           default:
                return getZheJiangCommentUrl(holder);
        }
    }
    //获取杭州师范大学
    private String getHangZhouShiFan(MesItemHolder holder) {
        return holder.getBaseUrl()+"?pageNo="+holder.getPager();
    }

    //浙江工业大学等通用的
    private String getZheJiangCommentUrl(MesItemHolder holder) {
        return holder.getBaseUrl()+"?page="+holder.getPager();
    }
    //浙江大学
    private String getZheJiangDaXue(MesItemHolder holder) {
        StringBuilder sb = new StringBuilder();
        sb.append(holder.getBaseUrl());
        sb.append("?pages.currentPage="+holder.getPager());
        return sb.toString();
    }

    /**
     * 获取杭州电子科技大学的URL
     * @param holder holder
     * @return URL
     */
    private String getHangZhouDianZi(MesItemHolder holder) {
        StringBuilder sb = new StringBuilder(holder.getBaseUrl());
        sb.append("?start_page=1&keyword=&type=inner&day=&count=10&start="+holder.getPager());
        return sb.toString();
    }
//----------------------------------------------------------------------

    private String getShanghaiUrl(MesItemHolder holder) {
        switch (holder.getCollege()){
            case "上海交通大学":
                return getJiaoDaUrl(holder);
            case "上海理工大学":
                return getLiGongUrl(holder);
            case "复旦大学":
                return getFuDanUrl(holder) ;
            case "上海财经大学":
                return getShangCaiUrl(holder);
        }
        return "";
    }
    //上海财经大学
    private String getShangCaiUrl(MesItemHolder holder) {
        return holder.getBaseUrl()+"?eachPageRows=10&currentPageno="+holder.getPager();
    }

    private String getFuDanUrl(MesItemHolder holder) {
        return "http://www.career.fudan.edu.cn/jsp/career_talk_list.jsp?count=20&list=true&page="
                +holder.getPager();
    }

    private String getLiGongUrl(MesItemHolder holder) {
        return "";
    }

    /**
     * ?modcode=jygl_xjhxxck&subsyscode=zpfw&type=searchXjhxx&xjhType=all
     * first
     *
     JSESSIONID=59BD52D46984E9A980DB74AA28361E1C.tomcat107
     Cookie
     JSESSIONID=59BD52D46984E9A980DB74AA28361E1C.tomcat107
     *http://www.job.sjtu.edu.cn/eweb/wfc/app/pager.so?type=goPager&requestPager=pager&pageMethod=next&currentPage=1
     *http://www.job.sjtu.edu.cn/eweb/wfc/app/pager.so?type=goPager&requestPager=pager&pageMethod=next&currentPage=2
     * @param holder
     * @return
     */
    private String getJiaoDaUrl(MesItemHolder holder) {
        StringBuilder url = new StringBuilder(holder.getBaseUrl());
        if(holder.isPull()){
            url.append("?modcode=jygl_xjhxxck&subsyscode=zpfw&type=searchXjhxx&xjhType=all");
        }else{

            url.append("?ype=goPager&requestPager=pager&pageMethod=next&currentPage="+(holder.getPager()-1));
        }
        return url.toString();
    }
//----------------------------------------------------------------------
    private String getJiangSuUrl(MesItemHolder holder){
        String str = "";
        if(holder.getCollege().equals("南京大学")){
            return holder.getBaseUrl()+"?type=zph&pageNow="+holder.getPager();
        }else
            return  holder.getBaseUrl() +"?page="+holder.getPager();

    }
}
