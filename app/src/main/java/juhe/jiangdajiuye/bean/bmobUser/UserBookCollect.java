package juhe.jiangdajiuye.bean.bmobUser;

import cn.bmob.v3.BmobObject;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-02-04
 */

public class UserBookCollect extends BmobObject {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private  String userId ;
    private  String book ;
    private  String editor;
    private  String available;
    private  String number ;

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public  String url ;

}
