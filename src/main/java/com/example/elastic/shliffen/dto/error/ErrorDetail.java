package com.example.elastic.shliffen.dto.error;


public class ErrorDetail {

    private boolean success;
    private String errorMessage;

    public ErrorDetail(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public ErrorDetail() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
