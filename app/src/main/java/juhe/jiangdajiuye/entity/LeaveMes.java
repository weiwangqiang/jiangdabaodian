package juhe.jiangdajiuye.entity;

import cn.bmob.v3.BmobObject;

/**
 * class description here
 *
 * 用户留言
 *
 * @author wangqiang
 * @since 2017-07-31
 */

public class LeaveMes extends BmobObject{
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content ;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;
}
