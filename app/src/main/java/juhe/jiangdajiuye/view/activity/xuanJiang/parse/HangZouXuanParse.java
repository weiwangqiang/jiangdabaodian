package juhe.jiangdajiuye.view.activity.xuanJiang.parse;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.db.repository.BrowseDepository;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.HangDianUniversity;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.MesItemHolder;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-10-15
 */

public class HangZouXuanParse {
    private static final String TAG = "parseHangZou";

    public static List<MessageBean> parse(String response, MesItemHolder holder) {
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
    private static List<MessageBean> hangZhouShiFan(String response) {
        Document doc = Jsoup.parse(response);
        List<MessageBean> list = new ArrayList<>();
        Elements elements = doc.getElementsByClass("post")
                .get(0)
                .getElementsByTag("tbody")
                .get(0).getElementsByTag("tr");
        for (Element element:elements) {
            Elements elements1 = element.getElementsByTag("td");
            MessageBean messageBean = new MessageBean();
            messageBean.setUrl(elements1.get(0)
                    .getElementsByTag("a")
                    .get(1).attr("href"));
            messageBean.setTitle(elements1.get(0)
                    .getElementsByTag("a")
                    .get(1).attr("title"));

            messageBean.setTheme(elements1.get(0)
                    .getElementsByTag("a")
                    .get(0).text());
            messageBean.setFrom("杭州师范大学");
            messageBean.setLocate(elements1.get(1).text());
//            messageBean.setTime(elements1.get(2).text());
            String time = elements1.get(2).text().replace(Jsoup.parse("&nbsp;").text(), " ") ;
            if('[' == time.charAt(0)){
                time = time.substring(1,time.length()-1);
            }
            messageBean.setTime(time);
            messageBean.setHasBrowse(BrowseDepository.getInstance().contain(messageBean.getUrl()));
            list.add(messageBean);
        }
        return list;
    }

    //浙江工业大学等通用解析
    private static List<MessageBean> parseCommon(String baseUrl, String response) {
        Document doc = Jsoup.parse(response);
        List<MessageBean> list = new ArrayList<>();
        Elements elements = doc.getElementsByClass("teachinList");
        for (int i = 1; i < elements.size(); i++) {
            Elements element = elements.get(i).getElementsByTag("li");
            MessageBean messageBean = new MessageBean();
            Element element1 = element.get(0).getElementsByTag("a").get(0);
            messageBean.setUrl(baseUrl + element1.attr("href"));
            messageBean.setTitle(element1.text());
            messageBean.setCity(element.get(1).text());
            messageBean.setFrom(element.get(2).text());
            messageBean.setLocate(element.get(3).text());
            messageBean.setTime(element.get(4).text());
            messageBean.setHasBrowse(BrowseDepository.getInstance().contain(messageBean.getUrl()));

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
            MessageBean messageBean = new MessageBean();
            messageBean.setTitle(elements.get(0).select("a").text());
            messageBean.setUrl("http://www.career.zju.edu.cn/ejob/"
                    + elements.select("a").attr("href"));
            messageBean.setLocate(elements.get(1).text());
            messageBean.setTime(elements.get(2).text());
            messageBean.setFrom("浙江大学");
            messageBean.setCity("杭州市");
            messageBean.setHasBrowse(BrowseDepository.getInstance().contain(messageBean.getUrl()));

            list.add(messageBean);
        }
        return list;
    }


    private static List<MessageBean> hangZhouDianZi(String response) {
        Gson gson = new Gson();
        HangDianUniversity university =
                gson.fromJson(response, HangDianUniversity.class);
        List<HangDianUniversity.DataBean> dataBeen = university.getData();
        List<MessageBean> res = new ArrayList<>();
        for (HangDianUniversity.DataBean bean : dataBeen) {
            MessageBean messageBean = new MessageBean();
            messageBean.setTitle(bean.getMeet_name());
            messageBean.setFrom(bean.getSchool_name());
            messageBean.setCompany(bean.getCompany_name());
            messageBean.setIndustry(bean.getIndustry_category());
            messageBean.setTime(bean.getMeet_day() + " " + bean.getMeet_time());
            messageBean.setCity(bean.getCity_name());
            messageBean.setLocate(bean.getAddress());
            messageBean.setUrl("http://career.hdu.edu.cn/detail/career?id="
                    + bean.getCareer_talk_id());
            messageBean.setHasBrowse(BrowseDepository.getInstance().contain(messageBean.getUrl()));

            res.add(messageBean);
        }
        return res;
    }
}
