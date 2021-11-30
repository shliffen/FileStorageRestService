package com.example.elastic.shliffen.model.base;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName = "filetag_index")
public class FileTag {

    @Id
    private long key;

    @Field(name = "tagName", type = FieldType.Keyword)
    private String nameOfTag = "";

    public FileTag(String nameOfTag) {
        this.nameOfTag = nameOfTag;
    }

    public FileTag() {

    }

    public Long getKey() {
        return this.key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getNameOfTag() {
        return nameOfTag;
    }

    public void setNameOfTag(String nameOfTag) {
        this.nameOfTag = nameOfTag;
    }
}
