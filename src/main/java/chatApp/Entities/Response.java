package chatApp.Entities;

public class Response {
    private String message;
    private int status;

    public Response(int status, String message) {
        this.message = message;
        this.status = status;
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

    public void setObj(int status) {
        this.status = status;
    }
}
