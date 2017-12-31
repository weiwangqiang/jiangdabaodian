package juhe.jiangdajiuye.view.xuanJiang.parse;

import android.util.Log;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanMesBean;
import juhe.jiangdajiuye.view.xuanJiang.entity.HanZhouDianZiKeJiUniversity;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-10-15
 */

public class ParseHangZou {
    private static final String TAG = "parseHangZou";

    public static List<MessageItem> parse(String response, XuanMesBean holder) {
        switch(holder.getCollege()) {
            case "杭州电子科技大学":
                return hangZhouDianZi(response);
            case "浙江大学":
                return zheJinagDaXue(response);
        }
        return new ArrayList<>();
    }

    private static List<MessageItem> zheJinagDaXue(String response) {
        System.out.println(response);
        Document doc = Jsoup.parse(response);
        List<MessageItem> list = new ArrayList<>();
        Elements element = doc.getElementsByClass("con");
//                .select("table").get(0).getElementsByTag("tbody").get(0).select("tr");
        for (Element element1 : element){
            Elements elements = element1.select("td");
            MessageItem item = new MessageItem();
            item.setTitle(elements.get(0).select("a").text());
            item.setUrl("http://www.career.zju.edu.cn/ejob/"
                    +elements.select("a").attr("href"));
            item.setLocate(elements.get(1).text());
            item.setTime(elements.get(2).text());
            item.setFrom("浙江大学");
            Log.i(TAG, "zheJinagDaXue: "+item.getTitle());
            list.add(item);
        }
        return list ;
    }


    private static List<MessageItem> hangZhouDianZi(String response) {
        Gson gson = new Gson();
        HanZhouDianZiKeJiUniversity university =
                gson.fromJson(response,HanZhouDianZiKeJiUniversity.class)   ;
        List<HanZhouDianZiKeJiUniversity.DataBean> dataBeen = university.getData() ;
        List<MessageItem> res = new ArrayList<>();
        for(HanZhouDianZiKeJiUniversity.DataBean bean:dataBeen){
            MessageItem item = new MessageItem();
            item.setTitle(bean.getMeet_name());
            item.setFrom(bean.getSchool_name());
            item.setTime(bean.getMeet_day()+"\n"+bean.getMeet_time());
            item.setLocate(bean.getAddress());
            item.setUrl("http://career.hdu.edu.cn/detail/career?id="
                    +bean.getCareer_talk_id());
            res.add(item);
        }
        return res ;
    }
}
