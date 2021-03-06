package com.petask.petask_users.models.response;

import com.google.gson.annotations.SerializedName;
import com.petask.petask_users.models.User;

import java.util.ArrayList;

/**
 * POJO class used to parse the "users" api response
 * It contains data like current page number, total pages, number of user data per page, total number of users and user data for the particular page
 */
public class UserList {

    private int page;
    @SerializedName("per_page")
    private int perPage;
    private int total;
    @SerializedName("total_pages")
    private int totalPages;
    private ArrayList<User> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public ArrayList<User> getData() {
        return data;
    }

    public void setData(ArrayList<User> data) {
        this.data = data;
    }
}