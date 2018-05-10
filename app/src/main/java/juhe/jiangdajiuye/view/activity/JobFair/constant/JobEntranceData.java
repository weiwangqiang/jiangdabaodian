package juhe.jiangdajiuye.view.activity.JobFair.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class description here
 *  宣讲会入口数据
 * @author wangqiang
 * @since 2017-09-30
 */

public class JobEntranceData {
    public String[] provinces = {"江苏","浙江"};
    public static final int JIANGSU = 0 ;
//    public static final int SHANGHAI = 1; //上海的无法抓到
    public static final int ZHEJIANG = 1;
    public static String province = "province";
    public static String provinceId = "provinceId";
    public List<Map<String,Object>> getData() {
        return data;
    }
   static enum provinceID{
       江大学大,
       打回去爱欧迪去
    }
    List<Map<String,Object>> data = new ArrayList<>();
    public JobEntranceData(){
        for(int i =0 ;i < provinces.length;i++){
            Map<String ,Object> map = new HashMap<>();
            map.put("province",provinces[i]);
            map.put("provinceId",i);
            data.add(map);
        }
    }

}
