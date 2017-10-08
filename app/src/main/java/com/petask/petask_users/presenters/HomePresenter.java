package com.petask.petask_users.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.petask.petask_users.activities.HomeActivity;
import com.petask.petask_users.error.ErrorCodes;
import com.petask.petask_users.models.response.UserList;
import com.petask.petask_users.networking.NetworkService;
import com.petask.petask_users.viewinterfaces.HomeView;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter {
    private final HomeView homeView;
    private final Context context;
    private final NetworkService networkService;

    public HomePresenter(HomeView homeView, Context context){
        this.homeView = homeView;
        this.context = context;
        this.networkService = new NetworkService();
    }

    public void getUsers(int pageNumber){
        final boolean isFirstPage = pageNumber == HomeActivity.FIRST_PAGE;
        if(isFirstPage) {
            homeView.showLoading();
        }

        networkService
                .getNetworkCalls()
                .getUsers(pageNumber)
                .enqueue(new Callback<UserList>() {
                    @Override
                    public void onResponse(@NonNull Call<UserList> call, @NonNull Response<UserList> response) {
                        UserList result = response.body();

                        homeView.hideLoading();

                        if(result != null && result.getData() != null && !result.getData().isEmpty()) {

                            if(isFirstPage) {
                                homeView.setTotalPages(result.getTotalPages());
                            }

                            homeView.onUsersLoaded(result);
                        } else {
                            handleFailure(isFirstPage, ErrorCodes.CODE_NO_DATA);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserList> call, @Nullable Throwable t) {
                        homeView.hideLoading();

                        int errorCode = ErrorCodes.CODE_UNKNOWN;
                        if (t != null) {
                            if (t instanceof UnknownHostException) {
                                errorCode = ErrorCodes.CODE_UNKNOWN_HOST_EXCEPTION;
                            } else if (t instanceof SocketTimeoutException) {
                                errorCode = ErrorCodes.CODE_SOCKET_TIMEOUT;
                            }
                        }

                        handleFailure(isFirstPage, errorCode);
                    }
                });
    }

    private void handleFailure(boolean isFirstPage, int errorCode) {
        if (isFirstPage) {
            homeView.showErrorView(errorCode);
        } else {
            homeView.stopPaginationOnError();
        }
    }
}
