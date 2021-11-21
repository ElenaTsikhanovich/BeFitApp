package it.academy.by.befitapp.exception;

public class ExceptionMessage {
    private Integer statusCode;
    private String message;
    private String exceptionTime;

    public ExceptionMessage(Integer statusCode, String message, String exceptionTime) {
        this.statusCode = statusCode;
        this.message = message;
        this.exceptionTime = exceptionTime;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExceptionTime() {
        return exceptionTime;
    }

    public void setExceptionTime(String exceptionTime) {
        this.exceptionTime = exceptionTime;
    }
}
