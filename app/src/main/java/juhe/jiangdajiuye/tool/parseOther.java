package juhe.jiangdajiuye.tool;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wangqiang on 2016/10/16.
 */
public class parseOther {
    public parseOther(){}
    public ArrayList<HashMap<String,String>> parse(String response,int from){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        switch(from){
            case 1:
                return nanyoudaxue(response);

            case 2:
                return  hehaidaxue(response);
            case 3:
                return jiangnandaxue(response);
            case 4:
                return nanjingligong(response);
            case 5:
                 return dongnandaxue(response);
            case 6:
                return nanjingdaxue(response);
            default:
        }
        return list;
    }

    /**
     * 南京邮电大学
     * @param response
     * @return
     */
    public ArrayList<HashMap<String,String>> nanyoudaxue(String response){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("teachinList");
        for (int i = 1; i < elements.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            Elements e = elements.get(i).getElementsByClass("span1");
            map.put("url", "http://njupt.91job.gov.cn"+e.select("a").attr("href"));
            map.put("title",e.select("b").text() + e.select("a").attr("title"));
            map.put("company", elements.get(i).getElementsByClass("span2").text());
            map.put("city", elements.get(i).getElementsByClass("span3").text());
            map.put("place", elements.get(i).getElementsByClass("span4").text());
            map.put("time", elements.get(i).getElementsByClass("span5").text());
            list.add(map);
            Log.e("南京邮电","title is "+map.get("title")+"company is "+map.get("company")
            +"time is "+map.get("time"));
        }
        Log.e("南京邮电","return data"+"data size is "+list.size());
        return list;
    }

    /**
     * 何海大学
     * @param response
     * @return
     */
    public ArrayList<HashMap<String,String>> hehaidaxue(String response){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("teachinList");
        for (int i = 1; i < elements.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            Elements e = elements.get(i).getElementsByClass("span1");
            map.put("url", "http://hhu.91job.gov.cn"+e.select("a").attr("href"));
            map.put("title",e.select("b").text() + e.select("a").attr("title"));
            map.put("company", elements.get(i).getElementsByClass("span2").text());
            map.put("city", elements.get(i).getElementsByClass("span3").text());
            map.put("place", elements.get(i).getElementsByClass("span4").text());
            map.put("time", elements.get(i).getElementsByClass("span5").text());
            list.add(map);
            Log.e("何海大学","title is "+map.get("title")+"company is "+map.get("company")
                    +"time is "+map.get("time"));
        }
        return list;
    }

    /**
     * 江南大学
     * @param response
     * @return
     */
    public ArrayList<HashMap<String,String>> jiangnandaxue(String response){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("teachinList");
        for (int i = 1; i < elements.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            Elements e = elements.get(i).getElementsByClass("span1");
            map.put("url", "http://jiangnan.91job.gov.cn"+e.select("a").attr("href"));
            map.put("title",e.select("b").text() + e.select("a").attr("title"));
            map.put("company", elements.get(i).getElementsByClass("span2").text());
            map.put("city", elements.get(i).getElementsByClass("span3").text());
            map.put("place", elements.get(i).getElementsByClass("span4").text());
            map.put("time", elements.get(i).getElementsByClass("span5").text());
            list.add(map);
            Log.e("江南大学","title is "+map.get("title")+"company is "+map.get("company")
                    +"time is "+map.get("time"));
        }
        return list;
    }
    /**
     * 南京理工大学
     * http://njust.91job.gov.cn/teachin/index?page=4
     */
    public ArrayList<HashMap<String,String>> nanjingligong(String response){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("teachinList");
        for (int i = 1; i < elements.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            Elements e = elements.get(i).getElementsByClass("span1");
            map.put("url", "http://njust.91job.gov.cn"+e.select("a").attr("href"));
            map.put("title",e.select("b").text() + e.select("a").attr("title"));
            map.put("company", elements.get(i).getElementsByClass("span2").text());
            map.put("city", elements.get(i).getElementsByClass("span3").text());
            map.put("place", elements.get(i).getElementsByClass("span4").text());
            map.put("time", elements.get(i).getElementsByClass("span5").text());
            list.add(map);
            Log.e("南京理工大学","title is "+map.get("title")+"company is "+map.get("company")
                    +"time is "+map.get("time"));
        }
        return list;
    }
    /**
     * 东南大学
     * http://seu.91job.gov.cn/teachin/index?page=2
     */
    public ArrayList<HashMap<String,String>> dongnandaxue(String response){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("teachinList");
        for (int i = 1; i < elements.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            Elements e = elements.get(i).getElementsByClass("span1");
            map.put("url", "http://seu.91job.gov.cn"+e.select("a").attr("href"));
            map.put("title",e.select("b").text() + e.select("a").attr("title"));
            map.put("company", elements.get(i).getElementsByClass("span2").text());
            map.put("city", elements.get(i).getElementsByClass("span3").text());
            map.put("place", elements.get(i).getElementsByClass("span4").text());
            map.put("time", elements.get(i).getElementsByClass("span5").text());
            list.add(map);
            Log.e("东南大学","title is "+map.get("title")+"company is "+map.get("company")
                    +"time is "+map.get("time"));
        }
        return list;
    }
    /**
     * 南京大学
     * http://job.nju.edu.cn:9081/login/nju/home.jsp?type=zph&pageNow=1
     */
    public ArrayList<HashMap<String,String>> nanjingdaxue(String response){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        Document doc = Jsoup.parse(response);

        Elements elements = doc.getElementsByClass("article-lists").select("li");
        for (int i = 0; i < elements.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            Elements elements1 = elements.get(i).select("span");
            map.put("url", "http://job.nju.edu.cn:9081/login/nju/"+elements1.get(0).select("a").attr("href"));
            map.put("title",elements1.get(0).select("a").text());
            map.put("time", elements.get(1).text());

            list.add(map);
            Log.e("南京大学","title is "+map.get("title")
                    +"time is "+map.get("time")+"url is "+map.get("url"));
        }
        Log.e("南京大学","list size is "+list.size());
        return list;
    }
}
