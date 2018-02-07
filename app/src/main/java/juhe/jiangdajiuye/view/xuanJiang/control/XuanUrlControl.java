package juhe.jiangdajiuye.view.xuanJiang.control;

import android.util.Log;

import juhe.jiangdajiuye.view.xuanJiang.constant.XuanEntranceData;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanJiangMesHolder;

/**
 * class description here
 *
 * 获取宣讲URL的信息
 *
 * @author wangqiang
 * @since 2017-09-30
 */

public class XuanUrlControl {
    private static final String TAG = "XuanUrlControl";
    public static XuanUrlControl instance;
    public static XuanUrlControl getInStance(){
        if(instance == null)
            instance = new XuanUrlControl();
        return instance ;
    }
    public String getUrl(XuanJiangMesHolder holder){
        Log.i(TAG, "getUrl: "+holder.getProvinceId());
        switch (holder.getProvinceId()){
            case XuanEntranceData.JIANGSU:
                return getJiangSu(holder);
            case XuanEntranceData.SHANGHAI:
                return getShanghai(holder);
            case XuanEntranceData.ZHEJIANG:
                return getHangZhou(holder);
        }
        return "";
    }
//----------------------------------------------------------------------
    private String getHangZhou(XuanJiangMesHolder holder) {
        switch (holder.getCollege()){
            case "杭州电子科技大学":
                return getHangZhouDianZi(holder);
            case "浙江大学":
                return getZheJiangDaXue(holder);
        }
        return "";
    }

    private String getZheJiangDaXue(XuanJiangMesHolder holder) {
        StringBuilder sb = new StringBuilder();
        sb.append(holder.baseUrl);
        sb.append("?pages.currentPage="+holder.pager);
        return sb.toString();
    }

    /**
     * 获取杭州电子科技大学的URL
     * @param holder holder
     * @return URL
     */
    private String getHangZhouDianZi(XuanJiangMesHolder holder) {
        StringBuilder sb = new StringBuilder(holder.baseUrl);
        sb.append("?start_page=1&keyword=&type=inner&day=&count=10&start="+holder.pager);
        return sb.toString();
    }
//----------------------------------------------------------------------

    private String getShanghai(XuanJiangMesHolder holder) {
        Log.i(TAG, "getShanghai: ");
        switch (holder.getCollege()){
            case "上海交通大学":
                return getJiaoDaUrl(holder);
            case "上海理工大学":
                return getLiGongUrl(holder);
            case "复旦大学":
                return getFuDanUrl(holder) ;
        }
        return "";
    }

    private String getFuDanUrl(XuanJiangMesHolder holder) {
        return "http://www.career.fudan.edu.cn/jsp/career_talk_list.jsp?count=20&list=true&page="
                +holder.getPager();
    }

    private String getLiGongUrl(XuanJiangMesHolder holder) {
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
    private String getJiaoDaUrl(XuanJiangMesHolder holder) {
        StringBuilder url = new StringBuilder(holder.baseUrl);
        if(holder.isPull){
            url.append("?modcode=jygl_xjhxxck&subsyscode=zpfw&type=searchXjhxx&xjhType=all");
        }else{
            url.append("?ype=goPager&requestPager=pager&pageMethod=next&currentPage="+(holder.getPager()-1));
        }
        return url.toString();
    }
//----------------------------------------------------------------------
    private String getJiangSu(XuanJiangMesHolder holder){
        String str = "";
        if(holder.getCollege().equals("南京大学")){
            return holder.getBaseUrl()+"?type=zph&pageNow="+holder.getPager();
        }else
            return  holder.getBaseUrl() +"?page="+holder.getPager();

    }
}
