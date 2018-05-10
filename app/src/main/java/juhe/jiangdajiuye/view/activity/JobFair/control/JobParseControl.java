package juhe.jiangdajiuye.view.activity.JobFair.control;


import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.view.activity.JobFair.constant.JobEntranceData;
import juhe.jiangdajiuye.view.activity.JobFair.parse.HangZouJobParse;
import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.view.activity.JobFair.parse.JiangSuJobParse;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.MesItemHolder;

/**
 * class description here
 * 网页解析入口
 * @author wangqiang
 * @since 2017-09-30
 */

public class JobParseControl {
    private String TAG = "JobParseControl" ;
    public static JobParseControl instance ;
    public static JobParseControl getInStance(){
        if(instance == null)
            instance = new JobParseControl();
        return instance ;
    }
    public List<MessageBean> parse(String result , MesItemHolder holder){
        switch (holder.getProvinceId()){
            case JobEntranceData.JIANGSU:
                return JiangSuJobParse.getInstance().parse(result,holder);
//            case JobEntranceData.SHANGHAI:
//                return ShangHaiJobParse.getShangHaiJobParse().parse(result,holder);
            case JobEntranceData.ZHEJIANG:
                return HangZouJobParse.parse(result,holder) ;
        }
        return new ArrayList<>();
    }

}
