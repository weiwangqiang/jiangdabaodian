package juhe.jiangdajiuye.view.xuanJiang.parse;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.view.xuanJiang.entity.HanZhouDianZiKeJiUniversity;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanJiangMesHolder;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-10-15
 */

public class ParseHangZou {
    private static final String TAG = "parseHangZou";

    public static List<MessageItem> parse(String response, XuanJiangMesHolder holder) {
        switch (holder.getCollege()) {
            case "杭州电子科技大学":
                return hangZhouDianZi(response);
            case "浙江大学":
                return zheJinagDaXue(response);
            case "杭州师范大学":
                return hangZhouShiFan(response);
            default:
                return parseCommon(holder.getBaseUrl().substring(0,
                        holder.getBaseUrl().length() - 14), response);
        }
    }

    //杭州师范大学
    private static List<MessageItem> hangZhouShiFan(String response) {
        Document doc = Jsoup.parse(response);
        List<MessageItem> list = new ArrayList<>();
        Elements elements = doc.getElementsByClass("post")
                .get(0)
                .getElementsByTag("tbody")
                .get(0).getElementsByTag("tr");
        for (Element element:elements) {
            Elements elements1 = element.getElementsByTag("td");
            MessageItem messageItem = new MessageItem();
            messageItem.setUrl(elements1.get(0)
                    .getElementsByTag("a")
                    .get(1).attr("href"));
            messageItem.setTitle(elements1.get(0)
                    .getElementsByTag("a")
                    .get(1).attr("title"));

            messageItem.setTheme(elements1.get(0)
                    .getElementsByTag("a")
                    .get(0).text());
            messageItem.setFrom("杭州师范大学");
            messageItem.setLocate(elements1.get(1).text());
            messageItem.setTime(elements1.get(2).text());

            list.add(messageItem);
        }
        return list;
    }

    //浙江工业大学等通用解析
    private static List<MessageItem> parseCommon(String baseUrl, String response) {
        Document doc = Jsoup.parse(response);
        List<MessageItem> list = new ArrayList<>();
        Elements elements = doc.getElementsByClass("teachinList");
        for (int i = 1; i < elements.size(); i++) {
            Elements element = elements.get(i).getElementsByTag("li");
            MessageItem messageItem = new MessageItem();
            Element element1 = element.get(0).getElementsByTag("a").get(0);
            messageItem.setUrl(baseUrl + element1.attr("href"));
            messageItem.setTitle(element1.text());
            messageItem.setCity(element.get(1).text());
            messageItem.setFrom(element.get(2).text());
            messageItem.setLocate(element.get(3).text());
            messageItem.setTime(element.get(4).text());
            list.add(messageItem);
        }
        return list;
    }

    private static List<MessageItem> zheJinagDaXue(String response) {
        Document doc = Jsoup.parse(response);
        List<MessageItem> list = new ArrayList<>();
        Elements element = doc.getElementsByClass("con");
        for (Element element1 : element) {
            Elements elements = element1.select("td");
            MessageItem item = new MessageItem();
            item.setTitle(elements.get(0).select("a").text());
            item.setUrl("http://www.career.zju.edu.cn/ejob/"
                    + elements.select("a").attr("href"));
            item.setLocate(elements.get(1).text());
            item.setTime(elements.get(2).text());
            item.setFrom("浙江大学");
            item.setCity("杭州市");
            list.add(item);
        }
        return list;
    }


    private static List<MessageItem> hangZhouDianZi(String response) {
        Gson gson = new Gson();
        HanZhouDianZiKeJiUniversity university =
                gson.fromJson(response, HanZhouDianZiKeJiUniversity.class);
        List<HanZhouDianZiKeJiUniversity.DataBean> dataBeen = university.getData();
        List<MessageItem> res = new ArrayList<>();
        for (HanZhouDianZiKeJiUniversity.DataBean bean : dataBeen) {
            MessageItem item = new MessageItem();
            item.setTitle(bean.getMeet_name());
            item.setFrom(bean.getSchool_name());
            item.setCompany(bean.getCompany_name());
            item.setIndustry(bean.getIndustry_category());
            item.setTime(bean.getMeet_day() + " " + bean.getMeet_time());
            item.setCity(bean.getCity_name());
            item.setLocate(bean.getAddress());
            item.setUrl("http://career.hdu.edu.cn/detail/career?id="
                    + bean.getCareer_talk_id());
            res.add(item);
        }
        return res;
    }
}
