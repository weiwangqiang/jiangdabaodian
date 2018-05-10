package juhe.jiangdajiuye.view.activity.JobFair.bean;

import java.util.List;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-03-13
 */

public class HaiDianbean {

    /**
     * code : 1
     * msg :
     * data : [{"overdue":"0","sort_time":"-1513216800","fair_id":"1255","inner_school":"0","type":"2","is_online":"0","title":"鹏城专列\u201c职\u201d为你来 \u2014\u20142017年\u201c深圳起点号\u201d校招专列","organisers":"杭州电子科技大学","school_name":"杭州电子科技大学","address":"文化活动中心","fact_c_count":"0","plan_c_count":"40","view_count":"4011","meet_time":"10:00","is_recommend":"0","recommend_time":"0","is_inner":"1","is_over":true,"meet_day":"2017-12-14","school_cnt":"","internet_cnt":"","total":""},{"overdue":"0","sort_time":"-1511658000","fair_id":"1192","inner_school":"0","type":"2","is_online":"0","title":"杭电、理工、计量、科技四校联合招聘会（2017年在理工大学举办）","organisers":"杭电、理工、计量、科技","school_name":"杭州电子科技大学","address":"杭州市下沙高教园区浙江理工大学体育馆","fact_c_count":"0","plan_c_count":"40","view_count":"1628","meet_time":"09:00","is_recommend":"0","recommend_time":"0","is_inner":"1","is_over":true,"meet_day":"2017-11-26","school_cnt":"","internet_cnt":"","total":""}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * overdue : 0
         * sort_time : -1513216800
         * fair_id : 1255
         * inner_school : 0
         * type : 2
         * is_online : 0
         * title : 鹏城专列“职”为你来 ——2017年“深圳起点号”校招专列
         * organisers : 杭州电子科技大学
         * school_name : 杭州电子科技大学
         * address : 文化活动中心
         * fact_c_count : 0
         * plan_c_count : 40
         * view_count : 4011
         * meet_time : 10:00
         * is_recommend : 0
         * recommend_time : 0
         * is_inner : 1
         * is_over : true
         * meet_day : 2017-12-14
         * school_cnt :
         * internet_cnt :
         * total :
         */

        private String overdue;
        private String sort_time;
        private String fair_id;
        private String inner_school;
        private String type;
        private String is_online;
        private String title;
        private String organisers;
        private String school_name;
        private String address;
        private String fact_c_count;
        private String plan_c_count;
        private String view_count;
        private String meet_time;
        private String is_recommend;
        private String recommend_time;
        private String is_inner;
        private boolean is_over;
        private String meet_day;
        private String school_cnt;
        private String internet_cnt;
        private String total;

        public String getOverdue() {
            return overdue;
        }

        public void setOverdue(String overdue) {
            this.overdue = overdue;
        }

        public String getSort_time() {
            return sort_time;
        }

        public void setSort_time(String sort_time) {
            this.sort_time = sort_time;
        }

        public String getFair_id() {
            return fair_id;
        }

        public void setFair_id(String fair_id) {
            this.fair_id = fair_id;
        }

        public String getInner_school() {
            return inner_school;
        }

        public void setInner_school(String inner_school) {
            this.inner_school = inner_school;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIs_online() {
            return is_online;
        }

        public void setIs_online(String is_online) {
            this.is_online = is_online;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOrganisers() {
            return organisers;
        }

        public void setOrganisers(String organisers) {
            this.organisers = organisers;
        }

        public String getSchool_name() {
            return school_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getFact_c_count() {
            return fact_c_count;
        }

        public void setFact_c_count(String fact_c_count) {
            this.fact_c_count = fact_c_count;
        }

        public String getPlan_c_count() {
            return plan_c_count;
        }

        public void setPlan_c_count(String plan_c_count) {
            this.plan_c_count = plan_c_count;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getMeet_time() {
            return meet_time;
        }

        public void setMeet_time(String meet_time) {
            this.meet_time = meet_time;
        }

        public String getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(String is_recommend) {
            this.is_recommend = is_recommend;
        }

        public String getRecommend_time() {
            return recommend_time;
        }

        public void setRecommend_time(String recommend_time) {
            this.recommend_time = recommend_time;
        }

        public String getIs_inner() {
            return is_inner;
        }

        public void setIs_inner(String is_inner) {
            this.is_inner = is_inner;
        }

        public boolean isIs_over() {
            return is_over;
        }

        public void setIs_over(boolean is_over) {
            this.is_over = is_over;
        }

        public String getMeet_day() {
            return meet_day;
        }

        public void setMeet_day(String meet_day) {
            this.meet_day = meet_day;
        }

        public String getSchool_cnt() {
            return school_cnt;
        }

        public void setSchool_cnt(String school_cnt) {
            this.school_cnt = school_cnt;
        }

        public String getInternet_cnt() {
            return internet_cnt;
        }

        public void setInternet_cnt(String internet_cnt) {
            this.internet_cnt = internet_cnt;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
}
