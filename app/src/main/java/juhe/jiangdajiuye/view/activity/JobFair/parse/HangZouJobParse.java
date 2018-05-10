package juhe.jiangdajiuye.view.JobFair.parse;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.view.JobFair.bean.HaiDianbean;
import juhe.jiangdajiuye.view.xuanJiang.entity.MesItemHolder;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-10-15
 */

public class HangZouJobParse extends BaseParse {
    private static final String TAG = "parseHangZou";

    public static List<MessageBean> parse(String response, MesItemHolder holder) {
        switch (holder.getCollege()) {
            case "杭州电子科技大学":
                return hangZhouDianZi(response);
            case "浙江大学":
                return zheJinagDaXue(response);
            case "杭州师范大学":
                return hangZhouShiFan(response);
            case "浙江师范大学":
            case "中国计量大学":
                return zheJiangShiFan(getRootUrl(holder.getBaseUrl()), response);
            default:
                return parseCommon(getRootUrl(holder.getBaseUrl()), response);
        }
    }

    //浙江师范大学
    private static List<MessageBean> zheJiangShiFan(String baseUrl, String response) {
        Document doc = Jsoup.parse(response);
        List<MessageBean> list = new ArrayList<>();
        Elements elements = doc.getElementsByClass("jobfairList");
        for (int i = 1; i < elements.size(); i++) {
            Elements element = elements.get(i).getElementsByTag("li");
            MessageBean messageBean = new MessageBean();
            Element element1 = element.get(0).getElementsByTag("a").get(0);
            messageBean.setUrl(baseUrl + element1.attr("href"));
            messageBean.setTitle(element1.text());
            messageBean.setCity(element.get(1).text());
            messageBean.setLocate(element.get(2).text());
            messageBean.setTheme(element.get(3).text());
            messageBean.setTime(element.get(4).text());
            list.add(messageBean);
        }
        return list;

    }

    //杭州师范大学
    private static List<MessageBean> hangZhouShiFan(String response) {
        Document doc = Jsoup.parse(response);
        List<MessageBean> list = new ArrayList<>();
        Elements elements = doc.getElementsByClass("main_list")
                .get(0)
                .getElementsByTag("dl");
        for (Element element : elements) {
            Element elements1 = element.getElementsByTag("dt").get(0);
            MessageBean messageBean = new MessageBean();
            String time = elements1
                    .getElementsByTag("span")
                    .get(0).text()
                    .replace(Jsoup.parse("&nbsp;").text(),
                            " ");
            if('[' == time.charAt(0)){
                time = time.substring(2,time.length()-2);
            }
            messageBean.setTime(time);
            messageBean.setTheme(elements1
                    .getElementsByTag("a")
                    .get(0).text());
            messageBean.setUrl(elements1
                    .getElementsByTag("a")
                    .get(1).attr("href"));
            messageBean.setTitle(elements1
                    .getElementsByTag("a")
                    .get(1).attr("title"));

            messageBean.setFrom("杭州师范大学");
            list.add(messageBean);
        }
        return list;
    }

    //浙江工业大学等通用解析
    private static List<MessageBean> parseCommon(String baseUrl, String response) {
        Document doc = Jsoup.parse(response);
        List<MessageBean> list = new ArrayList<>();
        Elements elements = doc.getElementsByClass("newsList");
        for (int i = 0; i < elements.size(); i++) {
            Elements element = elements.get(i).getElementsByTag("li");
            MessageBean messageBean = new MessageBean();
            Element element1 = element.get(1).getElementsByTag("a").get(0);
            messageBean.setUrl(baseUrl + element1.attr("href"));
            messageBean.setTitle(element1.text());
//            messageBean.setCity(element.get(1).text());
//            messageBean.setFrom(element.get(2).text());
//            messageBean.setLocate(element.get(3).text());
            messageBean.setTime(element.get(0).text());
            list.add(messageBean);
        }
        return list;
    }

    private static List<MessageBean> zheJinagDaXue(String response) {
        Document doc = Jsoup.parse(response);
        List<MessageBean> list = new ArrayList<>();
        Elements element = doc.getElementsByClass("con");
        for (Element element1 : element) {
            Elements elements = element1.select("td");
            MessageBean item = new MessageBean();
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


    private static List<MessageBean> hangZhouDianZi(String response) {
        Gson gson = new Gson();
        HaiDianbean university =
                gson.fromJson(response, HaiDianbean.class);
        List<HaiDianbean.DataBean> dataBeen = university.getData();
        List<MessageBean> res = new ArrayList<>();
        for (HaiDianbean.DataBean bean : dataBeen) {
            MessageBean item = new MessageBean();
            item.setTitle(bean.getTitle());
            item.setFrom(bean.getOrganisers());
//            item.setCompany(bean.getC());
            item.setIndustry("大型");
            item.setTime(bean.getMeet_day() + " " + bean.getMeet_time());
//            item.setCity(bean.getAddress());
            item.setLocate(bean.getAddress());
            item.setUrl("http://career.hdu.edu.cn/detail/jobfair?id="
                    + bean.getFair_id());
            res.add(item);
        }
        return res;
    }
}
