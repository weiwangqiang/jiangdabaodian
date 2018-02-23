package juhe.jiangdajiuye.view.xuanJiang.control;


import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.view.xuanJiang.constant.XuanEntranceData;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanJiangMesHolder;
import juhe.jiangdajiuye.view.xuanJiang.parse.ParseHangZou;
import juhe.jiangdajiuye.view.xuanJiang.parse.ParseJiangSu;
import juhe.jiangdajiuye.view.xuanJiang.parse.ParseShangHai;

/**
 * class description here
 * 网页解析入口
 * @author wangqiang
 * @since 2017-09-30
 */

public class XuanParseControl {
    private String TAG = "XuanParseControl" ;
    public static XuanParseControl instance ;
    public static XuanParseControl getInStance(){
        if(instance == null)
            instance = new XuanParseControl();
        return instance ;
    }
    public List<MessageItem> parse(String result , XuanJiangMesHolder holder){
        switch (holder.getProvinceId()){
            case XuanEntranceData.JIANGSU:
                return ParseJiangSu.getInstance().parse(result,holder);
            case XuanEntranceData.SHANGHAI:
                return ParseShangHai.getParseShangHai().parse(result,holder);
            case XuanEntranceData.ZHEJIANG:
                return ParseHangZou.parse(result,holder) ;
        }
        return new ArrayList<>();
    }

}
