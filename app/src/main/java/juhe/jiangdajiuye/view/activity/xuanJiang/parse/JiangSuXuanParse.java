package juhe.jiangdajiuye.view.activity.xuanJiang.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.db.repository.BrowseDepository;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.MesItemHolder;

/**
 * Created by wangqiang on 2016/10/16.
 * 解析江苏高校的宣讲会
 */
public class JiangSuXuanParse {
    private static final String TAG = "JiangSuJobParse";
    public static JiangSuXuanParse instance ;
    public static JiangSuXuanParse getInstance(){
        if(instance == null)
            instance = new JiangSuXuanParse() ;
        return instance ;
    }
    public JiangSuXuanParse(){}
    public List<MessageBean> parse(String response, MesItemHolder holder){
        switch(holder.getCollege()){
            case "南京大学":
                return nanJingDaXue(response);
            case "江苏科技大学":
                return JiangKeDa("http://just.91job.gov.cn",response);
            default:
                return commonParse(response,holder.getBaseUrl().substring(0,holder.getBaseUrl().length()-14));
        }
    }
    //江苏科技大学
    private List<MessageBean> JiangKeDa(String baseUrl , String response) {
        List<MessageBean> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("teachinList");
        for (int i = 1; i < elements.size(); i++) {
            MessageBean messageBean = new MessageBean();
            Elements elements1 = elements.get(i).getElementsByTag("li");
            if(elements1.get(0).children().size() >1){
                // 【置顶】那一行，与宣讲会无关，直接跳过
                continue;
            }
            messageBean.setUrl(baseUrl+elements1.get(0).select("a").attr("href"));
            messageBean.setTitle(elements1.get(0).select("a").attr("title"));
            messageBean.setCity(elements1.get(1).text());
            messageBean.setFrom(elements1.get(2).text());
            messageBean.setLocate(elements1.get(3).text());
            messageBean.setTime(elements1.get(4).text());
            messageBean.setHasBrowse(BrowseDepository.getInstance().contain(messageBean.getUrl()));
            list.add(messageBean);
        }
        return list;
    }

    /**
     * 江苏省大学通用的parse
     * @param response
     * @return
     */
    public List<MessageBean> commonParse(String response, String baseUrl){
        List<MessageBean> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("teachinList");
        for (int i = 1; i < elements.size(); i++) {
            MessageBean messageBean = new MessageBean();
            Elements e = elements.get(i).getElementsByClass("span1");
            Elements elements1 = elements.get(i).getElementsByTag("li");
            messageBean.setUrl(baseUrl+e.select("a").attr("href"));
            messageBean.setTitle(e.select("b").text() + e.select("a").attr("title"));
            messageBean.setCity(elements1.get(1).text());
            messageBean.setFrom(elements1.get(2).text());
            messageBean.setLocate(elements1.get(3).text());
            messageBean.setTime(elements1.get(4).text());
            messageBean.setHasBrowse(BrowseDepository.getInstance().contain(messageBean.getUrl()));
            list.add(messageBean);
        }
        return list;
    }

    /**
     * 南京大学
     * http://job.nju.edu.cn:9081/login/nju/home.jsp?type=zph&pageNow=1
     */
    public List<MessageBean> nanJingDaXue(String response){
        List<MessageBean> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("article-lists").select("li");
        for (int i = 0; i < elements.size(); i++) {
            MessageBean messageBean = new MessageBean();
            Elements elements1 = elements.get(i).select("span");
            if(elements1.size() <2) continue;
            messageBean.setUrl("http://job.nju.edu.cn:9081/login/nju/"+
                    elements1.get(0).select("a").attr("href"));
            messageBean.setTitle(elements1.get(0).select("a").text());
            String string[] = elements1.get(1).text().replaceAll("\u00A0", ":").split("::");
            messageBean.setLocate(string[0]);
            messageBean.setTime(string[1]+" "+string[2]);
            messageBean.setFrom("南京大学");
            messageBean.setCity("南京");
            messageBean.setHasBrowse(BrowseDepository.getInstance().contain(messageBean.getUrl()));
            list.add(messageBean);
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
