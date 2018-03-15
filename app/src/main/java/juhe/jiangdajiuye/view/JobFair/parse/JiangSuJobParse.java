package juhe.jiangdajiuye.view.JobFair.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.view.xuanJiang.entity.MesItemHolder;

/**
 * Created by wangqiang on 2016/10/16.
 * 解析江苏高校的宣讲会
 */
public class JiangSuJobParse extends BaseParse  {
    private static final String TAG = "JiangSuJobParse";
    public static JiangSuJobParse instance ;
    public static JiangSuJobParse getInstance(){
        if(instance == null)
            instance = new JiangSuJobParse() ;
        return instance ;
    }
    public JiangSuJobParse(){}
    public List<MessageItem> parse(String response,MesItemHolder holder){
        switch(holder.getCollege()){
            case "南京大学":
                return nanJingDaXue(response);
            default:
                return commonParse(response,getRootUrl(holder.getBaseUrl()));
        }
    }
    //江苏科技大学
    private List<MessageItem> JiangKeDa(String baseUrl , String response) {
        List<MessageItem> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("teachinList");
        for (int i = 1; i < elements.size(); i++) {
            MessageItem item = new MessageItem();
            Elements elements1 = elements.get(i).getElementsByTag("li");
            if(elements1.get(0).children().size() >1){
                // 【置顶】那一行，与宣讲会无关，直接跳过
                continue;
            }
            item.setUrl(baseUrl+elements1.get(0).select("a").attr("href"));
            item.setTitle(elements1.get(0).select("a").attr("title"));
            item.setCity(elements1.get(1).text());
            item.setFrom(elements1.get(2).text());
            item.setLocate(elements1.get(3).text());
            item.setTime(elements1.get(4).text());
            list.add(item);
        }
        return list;
    }

    /**
     * 江苏省大学通用的parse
     * @param response
     * @return
     */
    public List<MessageItem> commonParse(String response,String baseUrl){
        List<MessageItem> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("jobfairList");
        for (int i = 1; i < elements.size(); i++) {
            MessageItem item = new MessageItem();
            Elements elements1 = elements.get(i).getElementsByTag("li");
            item.setUrl(baseUrl+
                    elements1.get(0).select("a").attr("href"));
            item.setTitle(elements1.get(0).select("b").text() +
                    elements1.get(0).select("a").attr("title"));
            item.setCity(elements1.get(1).text());
            item.setLocate(elements1.get(2).text());
            item.setTheme(elements1.get(3).text());
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
            if(elements1.size() <2) {
                continue;
            }
            item.setUrl("http://job.nju.edu.cn:9081/login/nju/"+
                    elements1.get(0).select("a").attr("href"));
            item.setTitle(elements1.get(0).select("a").text());
            String string[] = elements1.get(1).text().replaceAll("\u00A0", ":").split("::");
            item.setLocate(string[0]);
            item.setTime(string[1]+" "+string[2]);
            item.setFrom("南京大学");
            item.setCity("南京");
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
