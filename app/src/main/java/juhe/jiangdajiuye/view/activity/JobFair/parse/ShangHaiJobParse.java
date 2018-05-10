package juhe.jiangdajiuye.view.activity.JobFair.parse;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.ShangHaiCaiJinBean;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.MesItemHolder;

/**
 * class description here
 *
 *  解析上海高校的宣讲会
 *
 * @author wangqiang
 * @since 2017-09-30
 */

public class ShangHaiJobParse extends BaseParse{
    private static final String TAG = "ShangHaiJobParse";

    public static ShangHaiJobParse getShangHaiJobParse() {
        if(shangHaiJobParse == null)
            shangHaiJobParse = new ShangHaiJobParse() ;
        return shangHaiJobParse;
    }

    public static ShangHaiJobParse shangHaiJobParse;
    public List<MessageBean> parse(String response, MesItemHolder holder){
        switch (holder.getCollege()){
            case "上海交通大学":
                return parseJiaoDa(response);
            case "上海理工大学":
                return parseLiGong(response);
            case "复旦大学":
                return parseFuDan(response) ;
            case "上海财经大学":
                return parseShangCai(response);
        }
        return new ArrayList<>() ;
    }
    //上海财经大学
    private List<MessageBean> parseShangCai(String response) {
        Gson gson = new Gson();
        //需要获取null(".....")中的"...."值
        ShangHaiCaiJinBean bean = gson.fromJson(response.substring(5,response.length()-1) ,ShangHaiCaiJinBean.class);
        List<MessageBean> list = new ArrayList<>();
        for(ShangHaiCaiJinBean.ListDataBean listDataBean : bean.getListData()){
            MessageBean messageBean = new MessageBean();
            messageBean.setUrl("http://career.sufe.edu.cn/preaching.html?wid="+listDataBean.getWid());
            messageBean.setTitle(listDataBean.getZt());
            messageBean.setFrom("上海财经大学");
            messageBean.setLocate(listDataBean.getJbdd());
            messageBean.setCity("上海市");
            messageBean.setTime(listDataBean.getApkssj());
            messageBean.setTheme(listDataBean.getUsertype());
            list.add(messageBean);
        }
        return list;
    }

    /**
     * 上海交通大学
     * @param response
     * @return
     */
    String stringJiaoda = "http://www.job.sjtu.edu.cn/eweb/jygl/zpfw.so?modcode=jygl_xjhxxck&subsyscode=zpfw&type=viewXjhxx&id=";
    private List<MessageBean> parseJiaoDa(String response){
        List<MessageBean> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("z_newsl").select("li");
        for (int i = 1; i < elements.size(); i++) {
            MessageBean item = new MessageBean();
            Elements elements1 = elements.get(i).getElementsByTag("div");
            String s = elements1.get(0).select("a").attr("onclick") ;
            item.setUrl(stringJiaoda+s.substring(11,s.length() - 2));
            item.setTitle(elements1.get(0).select("a").text());
            item.setTheme(elements1.get(1).text());
            item.setFrom("上海交通大学");
            item.setCity("上海市");
            item.setLocate(elements1.get(2).text());
            item.setTime(elements1.get(3).text() + " "+elements1.get(4).text());
            list.add(item);
        }
        return list;
    }

    /**
     *
     * @param response
     * @return
     */
    private List<MessageBean> parseLiGong(String response) {
        List<MessageBean> list = new ArrayList<>();

        return list;
    }

    /**
     * 复旦大学
     * <div id="tab1_bottom" key="mbv2whlf-cui3-uz9y-csee-8fj2c6zq5aup">
     *     http://www.career.fudan.edu.cn/html/xjh/1.html?view=true&key=mbv2whlf-cui3-uz9y-csee-8fj2c6zq5aup
     * @param response
     * @return
     */
    private List<MessageBean> parseFuDan(String response) {
        List<MessageBean> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.select("div[id=tab1_bottom]");
        for (int i = 1; i < elements.size(); i++) {
            MessageBean item = new MessageBean();
            item.setUrl("http://www.career.fudan.edu.cn/html/xjh/1.html?view=true&key="
                    +elements.get(i).attr("key"));
            item.setTitle(elements.get(i).getElementsByClass("tab1_bottom1").text());
            item.setTheme(elements.get(i).getElementsByClass("tab1_bottom2").text());
            item.setFrom("复旦大学");
            item.setCity("上海市");
            item.setLocate(elements.get(i).getElementsByClass("tab1_bottom5").text());
            item.setTime(elements.get(i).getElementsByClass("tab1_bottom3").text()
                    + " "+elements.get(i).getElementsByClass("tab1_bottom4").text());
            list.add(item);
        }
        return list;
    }
    }
