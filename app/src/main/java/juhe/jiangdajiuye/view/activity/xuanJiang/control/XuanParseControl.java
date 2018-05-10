package juhe.jiangdajiuye.view.activity.xuanJiang.control;


import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.view.activity.xuanJiang.constant.XuanEntranceData;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.MesItemHolder;
import juhe.jiangdajiuye.view.activity.xuanJiang.parse.HangZouXuanParse;
import juhe.jiangdajiuye.view.activity.xuanJiang.parse.ShangHaiXuanParse;
import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.view.activity.xuanJiang.parse.JiangSuXuanParse;

/**
 * class description here
 * 网页解析入口
 * @author wangqiang
 * @since 2017-09-30
 */

public class XuanParseControl {
    private String TAG = "JobParseControl" ;
    public static XuanParseControl instance ;
    public static XuanParseControl getInStance(){
        if(instance == null)
            instance = new XuanParseControl();
        return instance ;
    }
    public List<MessageBean> parse(String result , MesItemHolder holder){
        switch (holder.getProvinceId()){
            case XuanEntranceData.JIANGSU:
                return JiangSuXuanParse.getInstance().parse(result,holder);
            case XuanEntranceData.SHANGHAI:
                return ShangHaiXuanParse.getShangHaiXuanParse().parse(result,holder);
            case XuanEntranceData.ZHEJIANG:
                return HangZouXuanParse.parse(result,holder) ;
        }
        return new ArrayList<>();
    }

}
