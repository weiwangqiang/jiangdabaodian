package juhe.jiangdajiuye.bean.school;

import java.util.List;


public class School {
    private boolean success;
    private int error_code;
    private String message;
    private List<SchoolList> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<SchoolList> getData() {
        return data;
    }

    public void setData(List<SchoolList> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}
