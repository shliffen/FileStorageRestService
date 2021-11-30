package com.example.elastic.shliffen.dto;

public class FileDataFromJson {

    private String name;
    private long size;

    public FileDataFromJson(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public FileDataFromJson() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
