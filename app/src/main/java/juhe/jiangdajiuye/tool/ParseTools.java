package juhe.jiangdajiuye.tool;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.bean.BookMesBean;
import juhe.jiangdajiuye.bean.MessageItem;

/**
 * Created by wangqiang on 2016/9/27.
 */
public class ParseTools {
    private String TAG = "ParseTools";
    public static ParseTools parse;
    public static final int XUANJIANG = 1;
    public static final int ZHAOPIN = 2;
    public static final int XINXI = 3;
    public static ParseTools getparseTool(){
        if(parse==null){
            parse = new ParseTools();
        }
        return parse;
    }
    public List<MessageItem> parseMainMes(String response,int tab){
        switch (tab){
            case XUANJIANG:
                return parseXuanjiang(response);
            case ZHAOPIN:
                return parseZhaopin(response);
            case XINXI:
                return parseShudi(response);
        }
        return null ;
    }
    //宣讲会
     public List<MessageItem> parseXuanjiang(String response) {
         List<MessageItem> list = new ArrayList<>();
         if (response.length() <= 0) return null;
         Document doc = Jsoup.parse(response);
         Elements elements = doc.getElementsByClass("teachinList");
         for (int i = 1; i < elements.size(); i++) {
             MessageItem mes = new MessageItem();
             Elements e = elements.get(i).getElementsByClass("span1");
             Elements elements1 = elements.get(i).getElementsByTag("li");
             mes.setUrl("http://ujs.91job.gov.cn"+e.select("a").attr("href"));
             mes.setTitle(e.select("a").attr("title"));
             mes.setFrom(elements1.get(1).text());
             mes.setCity(elements1.get(2).text());
             mes.setLocate(elements1.get(3).text());
             mes.setTime(elements1.get(4).text());
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
    public List<BookBean> parseSearch(String response){
        List<BookBean> list = new ArrayList<>();
        if(response.length()<=0) return null;
        Document doc = Jsoup.parse(response);
        Elements elements =  doc.getElementsByClass("book_list_info");
        for(int i = 0;i<elements.size();i++){
           BookBean bookBean = new BookBean();
            Element h = elements.get(i).select("h3").get(0);
            Elements p = elements.get(i).select("p");
            bookBean.setUrl("http://huiwen.ujs.edu.cn:8080/opac/"+ h.select("a").get(0).attr("href"));
            String book = h.select("a").text() ;
            if(book.length() >2)
                bookBean.setBook(book.substring(2 ,book.length()));
            else
                bookBean.setBook(book);
            bookBean.setNumber(h.ownText());
            bookBean.setAvailable(p.get(0).select("span").text());
            bookBean.setEditor(p.get(0).ownText().replace("(0)",""));
            list.add(bookBean);
        }
        Log.e("parse","return data"+"data size is "+list.size());
        return list;
    }
    //解析图书馆详情
    public static BookMesBean parseBookMessage(String response){
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
        BookMesBean bookMesBean = new BookMesBean();
        bookMesBean.setBook(book);
        bookMesBean.setEditer(editer);
        bookMesBean.setBookMessage(bookMessage);
        return bookMesBean;
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
