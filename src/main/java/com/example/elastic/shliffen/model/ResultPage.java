package com.example.elastic.shliffen.model;

import java.util.List;

/**
 * Result page with list of files after filtering and total count of 'suitable' objects
 */
public class ResultPage {

    private int total;
    private List<FileForShowing> page;

    public ResultPage() {
    }

    public ResultPage(int total, List<FileForShowing> page) {
        this.total = total;
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<FileForShowing> getPage() {
        return page;
    }

    public void setPage(List<FileForShowing> page) {
        this.page = page;
    }
}
