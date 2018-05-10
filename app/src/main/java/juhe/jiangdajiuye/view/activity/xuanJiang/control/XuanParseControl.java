package juhe.jiangdajiuye.view.xuanJiang.control;


import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.view.xuanJiang.constant.XuanEntranceData;
import juhe.jiangdajiuye.view.xuanJiang.entity.MesItemHolder;
import juhe.jiangdajiuye.view.xuanJiang.parse.HangZouXuanParse;
import juhe.jiangdajiuye.view.xuanJiang.parse.JiangSuXuanParse;
import juhe.jiangdajiuye.view.xuanJiang.parse.ShangHaiXuanParse;

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
