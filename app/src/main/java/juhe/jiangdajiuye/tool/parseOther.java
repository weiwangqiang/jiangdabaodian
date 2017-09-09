package juhe.jiangdajiuye.tool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.entity.MessageItem;

/**
 * Created by wangqiang on 2016/10/16.
 */
public class parseOther {
    public parseOther(){}
    public List<MessageItem> parse(String response, int from, String url){
        switch(from){
            case 5:
                return nanjingdaxue(response);
            default:
                return commonParse(response,url.substring(0,url.length()-14));
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
    public List<MessageItem> nanjingdaxue(String response){
        List<MessageItem> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);

        Elements elements = doc.getElementsByClass("article-lists").select("li");
        for (int i = 0; i < elements.size(); i++) {
            MessageItem item = new MessageItem();
            Elements elements1 = elements.get(i).select("span");
            if(elements1.size() ==0) continue;
            item.setUrl("http://job.nju.edu.cn:9081/login/nju/"+elements1.get(1).select("a").attr("href"));
            item.setTitle(elements1.get(1).select("a").text());
            item.setTime(elements1.get(2).text());
            if(elements1.size()>3)
                item.setFrom(elements1.get(3).text());
            list.add(item);
        }
        return list;
    }


}
