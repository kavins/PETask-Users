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

public class HomeActivity extends AppCompatActivity implements HomeView {

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

    private void initRecyclerView() {
        DividerItemDecoration divider = new DividerItemDecoration(rvUserList.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_transparent));
        rvUserList.addItemDecoration(divider);

        rvUserList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvUserList.setAdapter(homeAdapter);
    }

    @OnClick(R.id.txtRetry)
    public void onRetryClicked() {
        homePresenter.getUsers(FIRST_PAGE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvUserList.setVisibility(View.GONE);
        layoutErrorView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView(int errorCode) {
        rvUserList.setVisibility(View.GONE);
        layoutErrorView.setVisibility(View.VISIBLE);

        txtErrorTitle.setText(errorCode == ErrorCodes.CODE_UNKNOWN_HOST_EXCEPTION ? R.string.error_view_no_internet_title :
                errorCode == ErrorCodes.CODE_SOCKET_TIMEOUT ? R.string.error_view_slow_internet_title : R.string.error_view_unknown_error_title);
        txtErrorMessage.setText(errorCode == ErrorCodes.CODE_UNKNOWN_HOST_EXCEPTION ? R.string.error_view_no_internet_message :
                errorCode == ErrorCodes.CODE_SOCKET_TIMEOUT ? R.string.error_view_slow_internet_message : R.string.error_view_unknown_error_message);
    }

    @Override
    public void onUsersLoaded(UserList userList) {
        if(rvUserList.getVisibility() == View.GONE) {
            rvUserList.setVisibility(View.VISIBLE);
        }
        homeAdapter.addItems(userList.getData());
    }

    @Override
    public void setTotalPages(int totalPages) {
        homeAdapter.setTotalPages(totalPages);
    }

    @Override
    public void stopPaginationOnError() {
        homeAdapter.setHasPagination(false);
    }
}
