package juhe.jiangdajiuye.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.bean.BookMesBean;
import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.view.fragment.IndexFragment;

/**
 * Created by wangqiang on 2016/9/27.
 */
public class ParseUtils {
    private String TAG = "ParseUtils";
    public static ParseUtils parse;

    public static ParseUtils getInstance() {
        if (parse == null) {
            parse = new ParseUtils();
        }
        return parse;
    }

    public List<MessageBean> parseMainMes(String response, int tab) {
        switch (tab) {
            case IndexFragment.XUANJIANG:
                return parseXuanJiang(response);
            case IndexFragment.ZHAOPINZHIWEI:
                return parseZhaoPinZhiWei(response);
            case IndexFragment.XINXI:
                return parseShuDi(response);
            case IndexFragment.ZHAOPINGONGGAO:
                return parseZhaoPinGongGao(response);
            case IndexFragment.ZHAOPINHUI:
                return parseZhaoPinHui(response);
            default:
                return new ArrayList<>();
        }
    }

    //解析招聘会
    private List<MessageBean> parseZhaoPinHui(String response) {
        List<MessageBean> data = new ArrayList<>();
        Document document = Jsoup.parse(response);
        Elements elements = document.getElementsByClass("jobfairList");
        for (int i = 1;i < elements.size();i++) {
            MessageBean messageBean = new MessageBean();
            Elements elements1 = elements.get(i).getElementsByTag("li");
            messageBean.setTitle(elements1.get(0).select("a").text());
            messageBean.setUrl("http://ujs.91job.gov.cn" + elements1.get((0)).select("a").attr("href"));
            messageBean.setCity("镇江市");
            messageBean.setLocate(elements1.get(2).text());
            messageBean.setFrom("江苏大学");
            messageBean.setTheme(elements1.get(3).text());
            messageBean.setTime(elements1.get(4).text());
            data.add(messageBean);
        }
        return data;
    }

    //招聘公告
    private List<MessageBean> parseZhaoPinGongGao(String response) {
        List<MessageBean> data = new ArrayList<>();
        Document document = Jsoup.parse(response);
        Elements elements = document.getElementsByClass("infoList");
        for (Element element : elements) {
            MessageBean messageBean = new MessageBean();
            Elements elements1 = element.getElementsByTag("li");
            messageBean.setTitle(elements1.get(0).select("a").text());
            messageBean.setUrl("http://ujs.91job.gov.cn" + elements1.get((0)).select("a").attr("href"));
            messageBean.setCity(elements1.get(1).text());
            messageBean.setTime(elements1.get(2).text());
            data.add(messageBean);
        }
        return data;
    }

    //宣讲会
    private List<MessageBean> parseXuanJiang(String response) {
        List<MessageBean> list = new ArrayList<>();
        if (response.length() <= 0) return null;
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("teachinList");
        for (int i = 1; i < elements.size(); i++) {
            MessageBean mes = new MessageBean();
            Elements e = elements.get(i).getElementsByClass("span1");
            Elements elements1 = elements.get(i).getElementsByTag("li");
            mes.setUrl("http://ujs.91job.gov.cn" + e.select("a").attr("href"));
            mes.setTitle(e.select("a").attr("title"));
            mes.setCity(elements1.get(1).text());
            mes.setFrom(elements1.get(2).text());
            mes.setLocate(elements1.get(3).text());
            mes.setTime(elements1.get(4).text());
            String str = mes.getTime() ;
            String realTime;
            if(str.indexOf("(") >0){
                realTime = str.substring(0,str.indexOf("("));
            }else{
                realTime = mes.getTime() ;
            }

            list.add(mes);
        }
        return list;
    }

    //招聘职位
    private List<MessageBean> parseZhaoPinZhiWei(String response) {
        List<MessageBean> list = new ArrayList<>();
        if (response.length() <= 0) return null;
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("infoList");
        for (int i = 0; i < elements.size(); i++) {
            MessageBean mes = new MessageBean();
            Elements e = elements.get(i).getElementsByClass("span1");
            mes.setUrl("http://ujs.91job.gov.cn" + e.select("a").attr("href"));
            mes.setTitle(e.select("a").attr("title"));
            mes.setFrom(elements.get(i).getElementsByClass("span2").select("a").text());
            mes.setCity(elements.get(i).getElementsByClass("span3").get(0).text());
            mes.setIndustry(elements.get(i).getElementsByClass("span3").get(1).text());
            mes.setTime(elements.get(i).getElementsByClass("span4").text());
            list.add(mes);
        }
        return list;
    }

    //信息速递
    private List<MessageBean> parseShuDi(String response) {
        List<MessageBean> list = new ArrayList<>();
        if (response.length() <= 0) return null;
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("newsList");
        for (int i = 0; i < elements.size(); i++) {
            MessageBean mes = new MessageBean();
            Elements e = elements.get(i).select("li");
            mes.setTime(e.get(0).text());
            mes.setTitle(e.get(1).select("b").text() + e.get(1).select("a").text());
            mes.setUrl("http://ujs.91job.gov.cn" +
                    e.get(1).select("a").attr("href"));
            list.add(mes);
        }
        return list;
    }

    //解析搜索图书的结果数量
    public int parseSearchNumber(String response) {
        Document doc = Jsoup.parse(response);
        Elements number = doc.getElementsByClass("bulk-actions");
        String n = number.select("p").select("strong").text();
        int num = 0;
        try {
            num = Integer.parseInt(n);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    //解析图书馆搜索
    public List<BookBean> parseSearch(String response, int totalNum) {
        List<BookBean> list = new ArrayList<>();
        if (response.length() <= 0) return null;
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("book_list_info");
        for (int i = 0; i < elements.size(); i++) {
            BookBean bookBean = new BookBean();
            Element h = elements.get(i).select("h3").get(0);
            Elements p = elements.get(i).select("p");
            bookBean.setUrl("http://huiwen.ujs.edu.cn:8080/opac/" + h.select("a").get(0).attr("href"));
            String book = h.select("a").text();
            if (book.length() > 2) {
                bookBean.setBook(parseBook(book));
            } else
                bookBean.setBook(book);
            bookBean.setNumber(h.ownText());
            bookBean.setAvailable(p.get(0).select("span").text());
            bookBean.setEditor(p.get(0).ownText().replace("(0)", ""));
            bookBean.setTotalNum(totalNum);
            list.add(bookBean);
        }
        return list;
    }

    //解析书名，把抓取到的书名去掉前面的序列号
    private String parseBook(String book) {
        int index;
        for (index = 0; index < book.length(); index++) {
            if (book.charAt(index) == '.')
                break;
        }
        if (index < book.length() - 1)
            return book.substring(index + 1, book.length());
        return "";
    }

    //解析图书馆详情
    public static BookMesBean parseBookMessage(String response) {
        String book = "";
        String editer = "";
        String bookMessage = "";
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("booklist");

        book = elements.get(0).select("a").text();
        editer = elements.get(0).select("dd").get(0).ownText();
        String[] str = editer.split("/");
        book = book + str[0];
        if (str.length > 1) {
            editer = str[1];
        }
        for (int i = 1; i < elements.size() - 2; i++) {
            bookMessage = bookMessage + elements.get(i).select("dt").text() + "\n";
            bookMessage = bookMessage + elements.get(i).select("dd").text() + "\n";
        }
        BookMesBean bookMesBean = new BookMesBean();
        bookMesBean.setBook(book);
        bookMesBean.setAuthor(editer);
        bookMesBean.setBookMessage(bookMessage);
        return bookMesBean;
    }

    //    解析图书可以利用数量
    public List<List<String>> parseSearchBookAvailable(String response) {
        List<List<String>> data = new ArrayList<>();
        String searchNumber = "";
        String number = "";
        String time = "";
        String locate = "";
        String state = "";
        Document doc = Jsoup.parse(response);
        Elements elements = doc.getElementsByClass("whitetext");
        for (int i = 0; i < elements.size(); i++) {
            Elements e = elements.get(i).select("td");
            if(e.size()<5){
                continue;
            }
            searchNumber = e.get(0).text();
            number = e.get(1).text();
            time = e.get(2).text();
            locate = e.get(3).text();
            state = e.get(4).text();
            List<String> list = new ArrayList<String>();
            list.add(searchNumber);
            list.add(number);
            list.add(time);
            list.add(locate);
            list.add(state);
            data.add(list);
        }
        return data;
    }

}
