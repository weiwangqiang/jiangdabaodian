package juhe.jiangdajiuye.bean;

import java.util.HashMap;

import cn.bmob.v3.BmobObject;

/**
 * class description here
 *  用户收藏的书
 * @author wangqiang
 * @since 2017-12-31
 */

public class BookBean extends BmobObject {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId ; //用户ID
    private  String book ;
    private  String editor;
    private  String available;
    private  String number ;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    private int totalNum;//返回的搜索结果数量
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

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     * {@code x}, {@code x.equals(x)} should return
     * {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     * {@code x} and {@code y}, {@code x.equals(y)}
     * should return {@code true} if and only if
     * {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     * {@code x}, {@code y}, and {@code z}, if
     * {@code x.equals(y)} returns {@code true} and
     * {@code y.equals(z)} returns {@code true}, then
     * {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     * {@code x} and {@code y}, multiple invocations of
     * {@code x.equals(y)} consistently return {@code true}
     * or consistently return {@code false}, provided no
     * information used in {@code equals} comparisons on the
     * objects is modified.
     * <li>For any non-null reference value {@code x},
     * {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p>
     * The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        if((null == obj ) || (!(obj instanceof BookBean))){
            return false ;
        }
        BookBean other = (BookBean)obj ;
        return other.getUrl().equals(this.url) && other.getUserId().equals(this.userId);
    }
}
