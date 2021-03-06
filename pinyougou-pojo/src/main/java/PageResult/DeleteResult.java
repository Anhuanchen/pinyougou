package PageResult;

import java.io.Serializable;

public class DeleteResult implements Serializable {
    public boolean success;
    public String message;

    public DeleteResult() {
    }

    public DeleteResult(boolean success, String message) {
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
