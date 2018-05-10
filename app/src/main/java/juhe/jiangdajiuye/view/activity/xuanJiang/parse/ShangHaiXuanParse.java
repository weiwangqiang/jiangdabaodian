package juhe.jiangdajiuye.view.activity.xuanJiang.parse;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.db.repository.BrowseDepository;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.MesItemHolder;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.ShangHaiCaiJinBean;

/**
 * class description here
 *
 *  解析上海高校的宣讲会
 *
 * @author wangqiang
 * @since 2017-09-30
 */

public class ShangHaiXuanParse {
    private static final String TAG = "ShangHaiJobParse";

    public static ShangHaiXuanParse getShangHaiXuanParse() {
        if(shangHaiXuanParse == null)
            shangHaiXuanParse = new ShangHaiXuanParse() ;
        return shangHaiXuanParse;
    }

    public static ShangHaiXuanParse shangHaiXuanParse;
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
            case "华南理工大学":
                return parseHuaNan(response);
        }
        return new ArrayList<>() ;
    }
    //华南理工大学
    private List<MessageBean> parseHuaNan(String response) {
        List<MessageBean> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("list").select("li");
        for (int i = 0; i < elements.size(); i++) {
            MessageBean messageBean = new MessageBean();
            messageBean.setUrl("http://jyzx.6ihnep7.cas.scut.edu.cn"+
                    elements.get(i).select("a")
                            .get(0).attr("href"));
            messageBean.setTitle(elements.get(i).select("a").get(0).text());
            messageBean.setFrom("华南理工大学");
            messageBean.setCity("上海市");
            messageBean.setTime(elements.get(i).select("div").text());
            messageBean.setHasBrowse(BrowseDepository.getInstance().contain(messageBean.getUrl()));
            list.add(messageBean);
        }
        return list;
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
            messageBean.setHasBrowse(BrowseDepository.getInstance().contain(messageBean.getUrl()));
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
            MessageBean messageBean = new MessageBean();
            Elements elements1 = elements.get(i).getElementsByTag("div");
            String s = elements1.get(0).select("a").attr("onclick") ;
            messageBean.setUrl(stringJiaoda+s.substring(11,s.length() - 2));
            messageBean.setTitle(elements1.get(0).select("a").text());
            messageBean.setTheme(elements1.get(1).text());
            messageBean.setFrom("上海交通大学");
            messageBean.setCity("上海市");
            messageBean.setLocate(elements1.get(2).text());
            messageBean.setTime(elements1.get(3).text() + " "+elements1.get(4).text());
            messageBean.setHasBrowse(BrowseDepository.getInstance().contain(messageBean.getUrl()));

            list.add(messageBean);
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
            MessageBean messageBean = new MessageBean();
            messageBean.setUrl("http://www.career.fudan.edu.cn/html/xjh/1.html?view=true&key="
                    +elements.get(i).attr("key"));
            messageBean.setTitle(elements.get(i).getElementsByClass("tab1_bottom1").text());
            messageBean.setTheme(elements.get(i).getElementsByClass("tab1_bottom2").text());
            messageBean.setFrom("复旦大学");
            messageBean.setCity("上海市");
            messageBean.setLocate(elements.get(i).getElementsByClass("tab1_bottom5").text());
            messageBean.setTime(elements.get(i).getElementsByClass("tab1_bottom3").text()
                    + " "+elements.get(i).getElementsByClass("tab1_bottom4").text());
            messageBean.setHasBrowse(BrowseDepository.getInstance().contain(messageBean.getUrl()));

            list.add(messageBean);
        }
        return list;
    }
    }
