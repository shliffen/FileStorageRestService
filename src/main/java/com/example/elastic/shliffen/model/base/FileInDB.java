package com.example.elastic.shliffen.model.base;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.HashSet;
import java.util.Set;

@Document(indexName = "files_index")
public class FileInDB {

    @Id
    private long id;
    private String name;
    private long size;
    private Set<FileTag> fileTags = new HashSet<>();

    public FileInDB(Set<FileTag> fileTags, String name, long size) {
        this.fileTags = fileTags;
        this.name = name;
        this.size = size;
    }

    public FileInDB() {

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

    public Set<FileTag> getFileTags() {
        return fileTags;
    }

    public void setFileTags(Set<FileTag> fileTags) {
        this.fileTags = fileTags;
    }
}
