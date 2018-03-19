package juhe.jiangdajiuye.bean;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-03-18
 */

public class UserCenterBean {
    private String leftText ;
    private String rightText ;
    private boolean hasNext ;

    public UserCenterBean(String leftText, String rightText, boolean hasNext) {
        this.leftText = leftText;
        this.rightText = rightText;
        this.hasNext = hasNext;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
