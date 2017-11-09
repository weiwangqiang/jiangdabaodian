package juhe.jiangdajiuye.entity.bombPush;

import com.google.gson.Gson;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-11-05
 */

public class Main {
       public static void main(String arg0[]){
           XuanJiangPush message = new XuanJiangPush();
           message.setTitle("haoahoda");
           message.setUrl("hasdhdqdada");
           message.setContent("你好发达");
           Gson gson  = new Gson();
           System.out.println(gson.toJson(message));
      }
}
