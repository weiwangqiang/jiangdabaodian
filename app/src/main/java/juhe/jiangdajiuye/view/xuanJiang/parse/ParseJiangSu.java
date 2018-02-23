package juhe.jiangdajiuye.view.xuanJiang.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanJiangMesHolder;

/**
 * Created by wangqiang on 2016/10/16.
 * 解析江苏高校的宣讲会
 */
public class ParseJiangSu {
    private static final String TAG = "ParseJiangSu";
    public static ParseJiangSu instance ;
    public static ParseJiangSu getInstance(){
        if(instance == null)
            instance = new ParseJiangSu() ;
        return instance ;
    }
    public ParseJiangSu(){}
    public List<MessageItem> parse(String response,XuanJiangMesHolder holder){
        switch(holder.getCollege()){
            case "南京大学":
                return nanJingDaXue(response);
            default:
                return commonParse(response,holder.getBaseUrl().substring(0,holder.getBaseUrl().length()-14));
        }
    }

    /**
     * 江苏省大学通用的parse
     * @param response
     * @return
     */
    public List<MessageItem> commonParse(String response,String baseUrl){
        List<MessageItem> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("teachinList");
        for (int i = 1; i < elements.size(); i++) {
            MessageItem item = new MessageItem();
            Elements e = elements.get(i).getElementsByClass("span1");
            Elements elements1 = elements.get(i).getElementsByTag("li");
            item.setUrl(baseUrl+e.select("a").attr("href"));
            item.setTitle(e.select("b").text() + e.select("a").attr("title"));
            item.setFrom(elements1.get(1).text());
            item.setCity(elements1.get(2).text());
            item.setLocate(elements1.get(3).text());
            item.setTime(elements1.get(4).text());
            list.add(item);
        }
        return list;
    }

    /**
     * 南京大学
     * http://job.nju.edu.cn:9081/login/nju/home.jsp?type=zph&pageNow=1
     */
    public List<MessageItem> nanJingDaXue(String response){
        List<MessageItem> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("article-lists").select("li");
        for (int i = 0; i < elements.size(); i++) {
            MessageItem item = new MessageItem();
            Elements elements1 = elements.get(i).select("span");
            if(elements1.size() <2) continue;
            item.setUrl("http://job.nju.edu.cn:9081/login/nju/"+
                    elements1.get(0).select("a").attr("href"));
            item.setTitle(elements1.get(0).select("a").text());
            String string[] = elements1.get(1).text().replaceAll("\u00A0", ":").split("::");
            item.setLocate(string[0]);
            item.setTime(string[1]+"\n"+string[2]);
            item.setFrom("南京大学");
            list.add(item);
        }
        return list;
    }
    public static int getPager(String string){
        if(string == null) return  0 ;
        Document doc = Jsoup.parse(string);
        Elements elements = doc.getElementsByClass("artilce-nav").select("a");
        String str[] = elements.get(0).text().split("/");
        int total = Integer.parseInt(str[0]);
        int num = Integer.parseInt(str[1]);
        if(total % num == 0) return total / num ;
        else return (total / num )+ 1;
    }

}
