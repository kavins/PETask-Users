package com.petask.petask_users.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.petask.petask_users.R;
import com.petask.petask_users.activities.HomeActivity;
import com.petask.petask_users.models.User;
import com.petask.petask_users.utils.SimpleRVHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter {

    private final static int TYPE_NORMAL = 0, TYPE_LOADING = 1;

    private Context context;
    private List<User> users;
    private final HomeAdapterListener listener;

    private int totalPages;
    private int pageNumber = HomeActivity.FIRST_PAGE;
    private boolean hasPagination;

    public HomeAdapter(Context context, HomeAdapterListener listener) {
        this.users = new ArrayList<>();
        this.listener = listener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case TYPE_LOADING:
                View loadingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_load_more, parent, false);
                return new SimpleRVHolder(loadingView);

            default:
            case TYPE_NORMAL:
                View normalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
                normalView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                return new ViewHolder(normalView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(getItemViewType(position)) {
            case TYPE_NORMAL:
                User user = users.get(position);

                ((HomeAdapter.ViewHolder) holder).click(user, listener);

                ((HomeAdapter.ViewHolder) holder).txtUserName.setText(user.getName());

                Glide.with(context)
                        .load(user.getAvatar())
                        .apply(RequestOptions.circleCropTransform())
                        .into(((HomeAdapter.ViewHolder) holder).imgUser);
                break;

            case TYPE_LOADING:
                pageNumber++;
                listener.loadMoreEntries(pageNumber);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return users.size() + (hasPagination ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if(hasPagination && position == getItemCount() - 1) {
            return TYPE_LOADING;
        }
        else {
            return TYPE_NORMAL;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtUserName)
        TextView txtUserName;

        @BindView(R.id.imgUser)
        ImageView imgUser;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void click(final User user, final HomeAdapterListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(user);
                }
            });
        }
    }

    public void addItems(ArrayList<User> users) {
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        setHasPagination(pageNumber < totalPages);
    }

    public void setHasPagination(boolean hasPagination) {
        this.hasPagination = hasPagination;
        notifyDataSetChanged();
    }

    public interface HomeAdapterListener {
        void onClick(User user);
        void loadMoreEntries(int pageNumber);
    }
}

