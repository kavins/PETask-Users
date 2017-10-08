package com.petask.petask_users.viewinterfaces;

import com.petask.petask_users.models.response.UserList;

/**
 * Home View interface which is implemented in home view to communicate between home presenter and home view
 */
public interface HomeView {

    void showLoading();

    void hideLoading();

    void stopPaginationOnError();

    void showErrorView(int errorCode);

    void onUsersLoaded(UserList userList);

    void setTotalPages(int totalPages);
}
