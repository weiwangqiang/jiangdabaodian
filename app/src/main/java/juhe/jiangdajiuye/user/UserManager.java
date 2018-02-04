package juhe.jiangdajiuye.user;

import juhe.jiangdajiuye.bean.bmobUser.UserBean;

/**
 * class description here
 *
 *  用户状态管理
 *
 * @author wangqiang
 * @since 2018-01-13
 */

public class UserManager {
    public UserBean getUserBean() {
        return userBean;
    }

    private UserBean userBean ;
    public static UserManager inStance = new UserManager();
    public static UserManager getInStance(){
        return inStance ;
    }
    public void setLogin(UserBean userBean){
        this.userBean = userBean ;
    }
    public void setLogout(){
        this.userBean = null ;
    }
    public boolean isLogin(){
        return userBean != null ;
    }

}
