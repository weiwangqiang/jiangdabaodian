package juhe.jiangdajiuye.bean.bmobUser;

import cn.bmob.v3.BmobObject;

/**
 * class description here
 * <p>
 * 用户对象
 *
 * @author wangqiang
 * @since 2018-01-13
 */

public class UserBean extends BmobObject {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    private String name;
    private String passWord;
    private Integer state;
    private String school = "无";
    private String company = "无";
    private String work = "无";

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (obj instanceof UserBean) {
            UserBean bean = (UserBean) obj;
            return this.name.equals(bean.getName())
                    && this.passWord.equals(bean.getPassWord());
        }
        return false;
    }
}
