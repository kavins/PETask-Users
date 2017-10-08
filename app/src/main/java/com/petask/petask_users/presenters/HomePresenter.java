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

/**
 * Home page presenter which handles all the business logic which should happen in home page
 * It notifies home view using the interface methods to update UI
 * API call to load user data is implemented here
 */
public class HomePresenter {
    private final HomeView homeView;
    private final Context context;
    private final NetworkService networkService;

    /**
     *Constructor which takes home view interface and  context as its parameters
     *
     * @param homeView interface used to communicate between the presenter and corresponding view
     * @param context Context
     */
    public HomePresenter(HomeView homeView, Context context){
        this.homeView = homeView;
        this.context = context;
        this.networkService = new NetworkService();
    }

    /**
     * Method used to load user data
     * @param pageNumber current page number - used for pagination
     */
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

    /**
     * Notify view in case of any error in api. Based on the current page number two different methods will be called
     * If its the first page, we will show an error view and an option to retry for user
     * Otherwise we will notify the adapter to discard pagination for the current instance of recycler view
     *
     * @param isFirstPage whether its the first page in pagination
     * @param errorCode to identify the type of error
     */
    private void handleFailure(boolean isFirstPage, int errorCode) {
        if (isFirstPage) {
            homeView.showErrorView(errorCode);
        } else {
            homeView.stopPaginationOnError();
        }
    }
}
