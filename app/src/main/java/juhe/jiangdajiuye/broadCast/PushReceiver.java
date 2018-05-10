package juhe.jiangdajiuye.broadCast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cn.bmob.push.PushConstants;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.push.MessageType;
import juhe.jiangdajiuye.bean.push.XuanJiangPush;
import juhe.jiangdajiuye.view.activity.browse.PushBrowseActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * class description here
 *
 *  推送消息接收者
 *
 * @author wangqiang
 * @since 2017-11-05
 */

public class PushReceiver extends BroadcastReceiver {
    Gson gson = new Gson();
    Context context ;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context ;
        mNM = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            parseMes(intent.getStringExtra("msg"));
        }
    }
    //解析推送的消息
    public void parseMes(String string){
        JsonParser parser=new JsonParser();  //创建JSON解析器
        JsonObject object = (JsonObject) parser.parse(string);
        int type = object.get("type").getAsInt();
        switch (type){
            case MessageType.XuanJiang:
                parseXuanJiang(string);
                break;
        }
    }
    //推送的消息为宣讲会
    public void parseXuanJiang(String string){
        XuanJiangPush message = gson.
                fromJson(string,
                        XuanJiangPush.class);
        if(null != message)
            message.getTitle();
        sendNotify(message);
    }
    private Notification notification  ;
    private NotificationManager mNM;
    private int NOTIFICATION = 1;
    //弹出通知
    public void sendNotify(XuanJiangPush message){
        //需要传入PendingIntent.FLAG_UPDATE_CURRENT intent方能传入数据
        //FLAG_UPDATE_CURRENT：如果该PendingIntent已经存在，则用新传入的Intent更新当前的数据。
        PendingIntent  contentIntent = PendingIntent.getActivity(context, 0,
                PushBrowseActivity.getActivityInt(context,message),
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.logo)  // the status icon
//                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(message.getTitle())  // the label of the entry
                .setContentText(message.getContent())  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setAutoCancel(true)
                .setLights(Color.RED,1000,1000)
                .setVibrate( new long[]{0,1000,1000})//静止，振动时长。。。。
                .setStyle(new NotificationCompat.BigTextStyle().bigText("我大皮带机啊"))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNM.notify(NOTIFICATION, notification);

    }
}
