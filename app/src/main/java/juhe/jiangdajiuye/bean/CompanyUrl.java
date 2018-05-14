package juhe.jiangdajiuye.bean;

import cn.bmob.v3.BmobObject;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-05-10
 */
public class CompanyUrl extends BmobObject {
    public String url ;
    public int companyType ;
    public String companyName ;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCompanyType() {
        return companyType;
    }

    public void setCompanyType(int companyType) {
        this.companyType = companyType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
