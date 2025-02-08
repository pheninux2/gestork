package pheninux.xdev.gestork.response;

public class CustomResponseBody<T> {
    private T data;
    private String message;
    private int status;
    private String alert;

    public CustomResponseBody() {
    }

    public CustomResponseBody(T data, String message, int status, String alert) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.alert = alert;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }
}
