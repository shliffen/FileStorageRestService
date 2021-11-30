package com.example.elastic.shliffen.dto.answers;

/**
 * Simple class for simplest form of answer
 */
public class SuccessAnswerObject {

    private String success;

    public SuccessAnswerObject(String success) {
        this.success = success;
    }

    public SuccessAnswerObject() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
