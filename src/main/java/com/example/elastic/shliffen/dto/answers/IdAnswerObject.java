package com.example.elastic.shliffen.dto.answers;

/**
 * Class for simple answer with uploaded file ID
 */
public class IdAnswerObject {

    private String id;

    public IdAnswerObject(String id) {
        this.id = id;
    }

    public IdAnswerObject() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
