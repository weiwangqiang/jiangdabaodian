package juhe.jiangdajiuye.view.activity.xuanJiang.entity;

/**
 * class description here
 *
 * 宣讲省份，学校等信息的 holder
 *
 * @author wangqiang
 * @since 2017-09-30
 */

public class MesItemHolder {
    public static final int mes_jiangda = 0 ;
    public static final int mes_xuan_jiang= 1 ;
    public static final int mes_job_fair = 2 ;
    private int provinceId;
    private int collegeId;
    private int messKind ;//信息类型：宣讲会、招聘会
    private String college;
    private int tab =  -1 ; //只有江大的界面才需要设置，默认为 -1 ，即其他学校
    private boolean isPull ;
    private int pager;
    private String baseUrl;

    public int getMessKind() {
        return messKind;
    }

    public void setMessKind(int messKind) {
        this.messKind = messKind;
    }
    public int getTab() {
        return tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    public boolean isPull() {
        return isPull;
    }

    public void setPull(boolean pull) {
        isPull = pull;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }



    public int getPager() {
        return pager;
    }

    public void setPager(int pager) {
        this.pager = pager;
    }

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

}
