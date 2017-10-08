package com.petask.petask_users.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.petask.petask_users.R;
import com.petask.petask_users.adapters.HomeAdapter;
import com.petask.petask_users.error.ErrorCodes;
import com.petask.petask_users.models.User;
import com.petask.petask_users.models.response.UserList;
import com.petask.petask_users.presenters.HomePresenter;
import com.petask.petask_users.viewinterfaces.HomeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * App's home activity. This class handles all the view related operations like showing or hiding views according to certain conditions.
 * It is mostly advised by the presenter to perform certain actions.
 * View initialization and set up (for eg : setting adapter to recycler view, setting up error view and its content, etc.,) also happens here
 */
public class HomeActivity extends AppCompatActivity implements HomeView {

    /**
     * ButterKnief is used for View Injection
     */

    // Recycler View
    @BindView(R.id.rvUserList)
    RecyclerView rvUserList;

    // Progress Bar
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    // Error View Section
    @BindView(R.id.layoutErrorView)
    LinearLayout layoutErrorView;
    @BindView(R.id.txtErrorTitle)
    TextView txtErrorTitle;
    @BindView(R.id.txtErrorMessage)
    TextView txtErrorMessage;

    private HomePresenter homePresenter;
    private HomeAdapter homeAdapter;

    public static final int FIRST_PAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initMembers();
        initRecyclerView();

        homePresenter.getUsers(FIRST_PAGE);
    }

    /**
     * Method used to initialize members like presenter, adapter, etc.,
     */
    private void initMembers() {
        homePresenter = new HomePresenter(this, this);
        homeAdapter = new HomeAdapter(this, new HomeAdapter.HomeAdapterListener() {
            @Override
            public void onClick(User user) {

            }

            @Override
            public void loadMoreEntries(int pageNumber) {
                homePresenter.getUsers(pageNumber);
            }
        });
    }

    /**
     * Method used to set up basic attributes to recycler view
     */
    private void initRecyclerView() {
        DividerItemDecoration divider = new DividerItemDecoration(rvUserList.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_transparent));
        rvUserList.addItemDecoration(divider);

        rvUserList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvUserList.setAdapter(homeAdapter);
    }

    /**
     * Method used to show progress bar and hide other components when api call is made for page 1
     */
    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvUserList.setVisibility(View.GONE);
        layoutErrorView.setVisibility(View.GONE);
    }

    /**
     * Method used to hide progress bar
     */
    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Method used to show error view in case of any failure in api for page 1
     * Also based on the error code, the respective title and message are set to the text views
     */
    @Override
    public void showErrorView(int errorCode) {
        rvUserList.setVisibility(View.GONE);
        layoutErrorView.setVisibility(View.VISIBLE);

        txtErrorTitle.setText(errorCode == ErrorCodes.CODE_UNKNOWN_HOST_EXCEPTION ? R.string.error_view_no_internet_title :
                errorCode == ErrorCodes.CODE_SOCKET_TIMEOUT ? R.string.error_view_slow_internet_title : R.string.error_view_unknown_error_title);
        txtErrorMessage.setText(errorCode == ErrorCodes.CODE_UNKNOWN_HOST_EXCEPTION ? R.string.error_view_no_internet_message :
                errorCode == ErrorCodes.CODE_SOCKET_TIMEOUT ? R.string.error_view_slow_internet_message : R.string.error_view_unknown_error_message);
    }

    /**
     * Click functionality for RETRY in error view
     */
    @OnClick(R.id.txtRetry)
    public void onRetryClicked() {
        homePresenter.getUsers(FIRST_PAGE);
    }

    /**
     * Method used to show recycler view (if hidden) and add the newly loaded users to the recycler view
     */
    @Override
    public void onUsersLoaded(UserList userList) {
        if(rvUserList.getVisibility() == View.GONE) {
            rvUserList.setVisibility(View.VISIBLE);
        }
        homeAdapter.addItems(userList.getData());
    }

    /**
     * Method used to set total pages to adapter
     */
    @Override
    public void setTotalPages(int totalPages) {
        homeAdapter.setTotalPages(totalPages);
    }

    /**
     * Method used to let adapter know to discard pagination on further scroll in case of any api error
     */
    @Override
    public void stopPaginationOnError() {
        homeAdapter.setHasPagination(false);
    }
}
