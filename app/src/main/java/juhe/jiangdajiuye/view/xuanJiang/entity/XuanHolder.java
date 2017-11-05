package juhe.jiangdajiuye.view.xuanJiang.entity;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-09-30
 */

public class XuanHolder {
    public int provinceId;

    public boolean isPull() {
        return isPull;
    }

    public void setPull(boolean pull) {
        isPull = pull;
    }

    public boolean isPull ;
    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    public int collegeId;
    public String college;

    public int getPager() {
        return pager;
    }

    public void setPager(int pager) {
        this.pager = pager;
    }

    public int pager;
    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String baseUrl;
}
