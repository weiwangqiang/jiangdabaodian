package juhe.jiangdajiuye.view.xuanJiang.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.entity.MessageItem;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanHolder;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-09-30
 */

public class ParseShangHai {
    private static final String TAG = "ParseShangHai";

    public static ParseShangHai getParseShangHai() {
        if(parseShangHai == null)
            parseShangHai = new ParseShangHai() ;
        return parseShangHai;
    }

    public static ParseShangHai parseShangHai ;
    public List<MessageItem> parse(String response,XuanHolder holder){
        switch (holder.getCollege()){
            case "上海交通大学":
                return parseJiaoDa(response);
            case "上海理工大学":
                return parseLiGong(response);
            case "复旦大学":
                return parseFuDan(response) ;
        }
        return new ArrayList<>() ;
    }

    /**
     * 上海交通大学
     * @param response
     * @return
     */
    String stringJiaoda = "http://www.job.sjtu.edu.cn/eweb/jygl/zpfw.so?modcode=jygl_xjhxxck&subsyscode=zpfw&type=viewXjhxx&id=";
    private List<MessageItem> parseJiaoDa(String response){
        List<MessageItem> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("z_newsl").select("li");
        for (int i = 1; i < elements.size(); i++) {
            MessageItem item = new MessageItem();
            Elements elements1 = elements.get(i).getElementsByTag("div");
            String s = elements1.get(0).select("a").attr("onclick") ;
            item.setUrl(stringJiaoda+s.substring(11,s.length() - 2));
            item.setTitle(elements1.get(0).select("a").text());
            item.setTheme(elements1.get(1).text());
            item.setFrom("上海交通大学");
            item.setLocate(elements1.get(2).text());
            item.setTime(elements1.get(3).text() + "\n"+elements1.get(4).text());
            list.add(item);
        }
        return list;
    }

    /**
     *
     * @param response
     * @return
     */
    private List<MessageItem> parseLiGong(String response) {
        List<MessageItem> list = new ArrayList<>();

        return list;
    }

    /**
     * 复旦大学
     * <div id="tab1_bottom" key="mbv2whlf-cui3-uz9y-csee-8fj2c6zq5aup">
     *     http://www.career.fudan.edu.cn/html/xjh/1.html?view=true&key=mbv2whlf-cui3-uz9y-csee-8fj2c6zq5aup
     * @param response
     * @return
     */
    private List<MessageItem> parseFuDan(String response) {
        List<MessageItem> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.select("div[id=tab1_bottom]");
        for (int i = 1; i < elements.size(); i++) {
            MessageItem item = new MessageItem();
            item.setUrl("http://www.career.fudan.edu.cn/html/xjh/1.html?view=true&key="
                    +elements.get(i).attr("key"));
            item.setTitle(elements.get(i).getElementsByClass("tab1_bottom1").text());
            item.setFrom(elements.get(i).getElementsByClass("tab1_bottom2").text());
            item.setLocate(elements.get(i).getElementsByClass("tab1_bottom5").text());
            item.setTime(elements.get(i).getElementsByClass("tab1_bottom3").text()
                    + "\n"+elements.get(i).getElementsByClass("tab1_bottom4").text());
            list.add(item);
        }
        return list;
    }
    }
