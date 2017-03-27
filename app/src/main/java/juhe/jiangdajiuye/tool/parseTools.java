package juhe.jiangdajiuye.tool;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 2016/9/27.
 */
public class parseTools {
    public static parseTools parse;
    public static parseTools getparseTool(){
        if(parse==null){
            parse = new parseTools();
        }
        return parse;
    }
    //宣讲会
     public ArrayList<HashMap<String,String>> parseXuanjiang(String response) {
         ArrayList<HashMap<String, String>> list = new ArrayList<>();
         if (response.length() <= 0) return null;
         Document doc = Jsoup.parse(response);
         Elements elements = doc.getElementsByClass("teachinList");
         for (int i = 1; i < elements.size(); i++) {
             HashMap<String, String> map = new HashMap<>();
             Elements e = elements.get(i).getElementsByClass("span1");
             map.put("url", "http://ujs.91job.gov.cn"+e.select("a").attr("href"));
             map.put("title", e.select("a").attr("title"));
             map.put("company", elements.get(i).getElementsByClass("span2").text());
             map.put("city", elements.get(i).getElementsByClass("span3").text());
             map.put("place", elements.get(i).getElementsByClass("span4").text());
             map.put("time", elements.get(i).getElementsByClass("span5").text());
             list.add(map);
         }
         return list;
     }
    //招聘
    public ArrayList<HashMap<String,String>> parseZhaopin(String response){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        if(response.length()<=0) return null;
        Document doc = Jsoup.parse(response);
        Elements elements =  doc.getElementsByClass("infoList");
        for(int i = 0;i<elements.size();i++){
            HashMap<String,String > map = new HashMap<>();
            Elements e = elements.get(i).getElementsByClass("span1");
            map.put("url","http://ujs.91job.gov.cn"+e.select("a").attr("href"));
            map.put("title",e.select("a").attr("title"));
            map.put("company",elements.get(i).getElementsByClass("span2").select("a").text());
            map.put("place",elements.get(i).getElementsByClass("span3").get(0).text());
            map.put("work",elements.get(i).getElementsByClass("span3").get(1).text());
            map.put("time",elements.get(i).getElementsByClass("span4").text());
            list.add(map);
        }
        Log.e("parse","return data");
        return list;
    }
    //信息速递
    public ArrayList<HashMap<String,String>> parseShudi(String response){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        if(response.length()<=0) return null;
        Document doc = Jsoup.parse(response);
        Elements elements =  doc.getElementsByClass("newsList");
        for(int i = 0;i<elements.size();i++){
            HashMap<String,String > map = new HashMap<>();
            Elements e = elements.get(i).select("li");
            map.put("time",e.get(0).text());
            map.put("title",e.get(1).select("b").text()+e.get(1).select("a").text());
            map.put("url","http://ujs.91job.gov.cn"+
                    e.get(1).select("a").attr("href"));
            list.add(map);
        }
        Log.e("parse","return data");
        return list;
    }
    public int parseSearchNumber(String response){
        Document doc = Jsoup.parse(response);
        Elements number =  doc.getElementsByClass("bulk-actions");
        String n = number.select("p").select("strong").text();
        int a = 0;
        try{
         a = Integer.parseInt(n);
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.e("parseLibrary","number is "+ a);
        return  a;
    }

    //解析图书馆搜索
    public ArrayList<Map<String,String>> parseSearch(String response){
        Log.e("parseLibrary","response is "+ response);
        ArrayList<Map<String,String>> list = new ArrayList<>();
        if(response.length()<=0) return null;
        Document doc = Jsoup.parse(response);
        Elements elements =  doc.getElementsByClass("book_list_info");
                Log.e("parse","elements size  is "+elements.size());
        for(int i = 0;i<elements.size();i++){
            HashMap<String,String > map = new HashMap<>();
            Element h = elements.get(i).select("h3").get(0);
            Elements p = elements.get(i).select("p");
            map.put("url","http://huiwen.ujs.edu.cn:8080/opac/"+ h.select("a").get(0).attr("href"));
            map.put("book", h.select("a").text());
            map.put("number",h.ownText());
            map.put("available",p.get(0).select("span").text());
            map.put("editor",p.get(0).ownText().replace("(0)",""));
            Log.e("parse"," url is "+map.get("url")+" name is "+map.get("book"));
            list.add(map);
        }
        Log.e("parse","return data"+"data size is "+list.size());
        return list;
    }
    //解析图书馆详情
    public static HashMap<String,String> parseBookMessage(String response){
        String book = "";
        String editer = "";
        String bookMessage = "";
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("booklist");

        book = elements.get(0).select("a").text();
        editer = elements.get(0).select("dd").get(0).ownText();
        String[] str = editer.split("/");
        book = book + str[0];
        if(str.length>1){
            editer  = str[1];
        }
        for(int i =1 ;i<elements.size()-2;i++){
            bookMessage = bookMessage+elements.get(i).select("dt").text()+"\n";
            bookMessage = bookMessage+elements.get(i).select("dd").text()+"\n";
        }
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("book",book);
        map.put("editer",editer);
        map.put("bookMessage",bookMessage);
        return map;
    }

    public List<List<String>> parseSearchBookAvailabale(String response){
        List<List<String>> data = new ArrayList<>();
        String searchnumber  = "";
        String number = "";
        String time = "";
        String locate = "";
        String state = "";
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("whitetext");
        for(int i = 0;i<elements.size();i++){
            Elements e = elements.get(i).select("td");
            searchnumber = e.get(0).text();
            number  = e.get(1).text();
            time  = e.get(2).text();
            locate = e.get(3).text();
            state = e.get(4).text();
            List<String> list = new ArrayList<String>();
            list.add(searchnumber);
            list.add(number);
            list.add(time);
            list.add(locate);
            list.add(state);
            data.add(list);
        }
        return data;
    }

}
