package juhe.jiangdajiuye.bean;

/**
 * class description here
 * 书详情
 * @author wangqiang
 * @since 2017-12-31
 */

public class BookMesBean {
   public String book ;//书名

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String author ;//作者
    public String bookMessage;//数详情


    public String getBookMessage() {
        return bookMessage;
    }

    public void setBookMessage(String bookMessage) {
        this.bookMessage = bookMessage;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }
}
