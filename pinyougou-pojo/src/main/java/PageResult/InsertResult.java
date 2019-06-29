package PageResult;

import java.io.Serializable;

public class InsertResult implements Serializable {
    public boolean success;
    public String message;

    public InsertResult() {
    }

    public InsertResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "InsertResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
