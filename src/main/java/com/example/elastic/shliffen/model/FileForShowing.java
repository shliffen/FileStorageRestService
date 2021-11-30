package com.example.elastic.shliffen.model;

import java.util.Set;

/**
 * Form of object inside the result list in Get Response for filtered files
 */
public class FileForShowing {

    private long id;
    private String name;
    private long size;
    private Set<String> tags;

    public FileForShowing(long id, String name, long size, Set<String> fileTags) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.tags = fileTags;
    }

    public FileForShowing() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
