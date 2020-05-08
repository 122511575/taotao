package com.taotao.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{
    private List<SearchItem> itemList;
    private long totalPages;
    private long totalcount;

    @Override
    public String toString() {
        return "SearchResult{" +
                "itemList=" + itemList +
                ", totalPages=" + totalPages +
                ", totalcount=" + totalcount +
                '}';
    }

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(long totalcount) {
        this.totalcount = totalcount;
    }
}
