package juhe.jiangdajiuye.view.comment.bean;

import cn.bmob.v3.BmobObject;
import juhe.jiangdajiuye.bean.bmobUser.UserBean;

/**
 * class description here
 *
 *  评论内容
 *
 * @author wangqiang
 * @since 2018-03-15
 */

public class Comment extends BmobObject {
    private String content;//评论内容

    private String time ;
    private UserBean user;//评论的用户，Pointer类型，一对一关系

    private Post post; //所评论的帖子，这里体现的是一对多的关系，一个评论只能属于一个微博

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
