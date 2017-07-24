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

import juhe.jiangdajiuye.entity.MessageItem;

/**
 * Created by wangqiang on 2016/9/27.
 */
public class parseTools {
    private String TAG = "parseTools";
    public static parseTools parse;
    public static parseTools getparseTool(){
        if(parse==null){
            parse = new parseTools();
        }
        return parse;
    }
    //宣讲会
     public List<MessageItem> parseXuanjiang(String response) {
         List<MessageItem> list = new ArrayList<>();
         if (response.length() <= 0) return null;
         Document doc = Jsoup.parse(response);
         Elements elements = doc.getElementsByClass("teachinList");
         Log.i(TAG, "parseXuanjiang:  size ： "+elements.size());
         for (int i = 1; i < elements.size(); i++) {
             MessageItem mes = new MessageItem();
             Elements e = elements.get(i).getElementsByClass("span1");
             mes.setUrl("http://ujs.91job.gov.cn"+e.select("a").attr("href"));
             mes.setTitle(e.select("a").attr("title"));
             mes.setFrom(elements.get(i).getElementsByClass("span2").text());
             mes.setCity(elements.get(i).getElementsByClass("span3").text());
             mes.setLocate(elements.get(i).getElementsByClass("span4").text());
             mes.setTime(elements.get(i).getElementsByClass("span5").text());
             list.add(mes);
         }
         return list;
     }
    //招聘
    public List<MessageItem> parseZhaopin(String response){
        List<MessageItem> list = new ArrayList<>();
        if(response.length()<=0) return null;
        Document doc = Jsoup.parse(response);
        Elements elements =  doc.getElementsByClass("infoList");
        System.out.println("elements size : "+elements.size());
        for(int i = 0;i<elements.size();i++){
            MessageItem mes = new MessageItem();
            Elements e = elements.get(i).getElementsByClass("span1");
            mes.setUrl("http://ujs.91job.gov.cn"+e.select("a").attr("href"));
            mes.setTitle(e.select("a").attr("title"));
            mes.setFrom(elements.get(i).getElementsByClass("span2").select("a").text());
            mes.setLocate(elements.get(i).getElementsByClass("span3").get(0).text());
            mes.setIndustry(elements.get(i).getElementsByClass("span3").get(1).text());
            mes.setTime(elements.get(i).getElementsByClass("span4").text());
            list.add(mes);
        }
        return list;
    }
    //信息速递
    public List<MessageItem> parseShudi(String response){
        List<MessageItem> list = new ArrayList<>();
        if(response.length()<=0) return null;
        Document doc = Jsoup.parse(response);
        Elements elements =  doc.getElementsByClass("newsList");
        Log.i(TAG, "parseShudi: size  "+elements.size());
        for(int i = 0;i<elements.size();i++){
           MessageItem mes = new MessageItem();
            Elements e = elements.get(i).select("li");
            mes.setTime(e.get(0).text());
            mes.setTitle(e.get(1).select("b").text()+e.get(1).select("a").text());
            mes.setUrl("http://ujs.91job.gov.cn"+
                    e.get(1).select("a").attr("href"));
            list.add(mes);
        }
        return list;
    }
    public int parseSearchNumber(String response){
        Document doc = Jsoup.parse(response);
        Elements number =  doc.getElementsByClass("bulk-actions");
        String n = number.select("p").select("strong").text();
        int num = 0;
        try{
            num = Integer.parseInt(n);
        }catch(Exception e){
            e.printStackTrace();
        }
        return  num;
    }

    //解析图书馆搜索
    public List<Map<String,String>> parseSearch(String response){
        Log.e("parseLibrary","response is "+ response);
        List<Map<String,String>> list = new ArrayList<>();
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
