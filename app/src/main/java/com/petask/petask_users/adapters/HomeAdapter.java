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

/**
 * Adapter to populate the recycler view
 */
public class HomeAdapter extends RecyclerView.Adapter {

    private final static int TYPE_NORMAL = 0, TYPE_LOADING = 1;

    private Context context;
    private List<User> users;
    private final HomeAdapterListener listener;

    private int totalPages;
    private int pageNumber = HomeActivity.FIRST_PAGE;
    private boolean hasPagination;

    /**
     * Constructor which takes context and listener as its variable
     *
     * @param context Context
     * @param listener used to communicate between adapter and view.
     *                 This listener contains couple of methods to notify on item click & load more data on pagination
     */
    public HomeAdapter(Context context, HomeAdapterListener listener) {
        this.users = new ArrayList<>();
        this.listener = listener;
        this.context = context;
    }

    /**
     * onCreateViewHolder of the adapter
     *
     * @param parent
     * @param viewType type of view which belongs to the position - returned by {getItemViewType}
     * @return View Holder - could be normal or loading row view holder
     */
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

    /**
     * onBindViewHolder of the adapter
     *
     * @param holder view holder of the position - if TYPE_NORMAL view holder load the data else if TYPE_LOADING notify the view to load paginated data
     * @param position current position
     */
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

    /**
     * Method which returns the number of elements of adapter
     *
     * @return int - item count of adapter
     */
    @Override
    public int getItemCount() {
        return users.size() + (hasPagination ? 1 : 0);
    }

    /**
     * Method which returns the item view type for that particular row position
     *
     * @param position
     * @return int - view type based on the position (either TYPE_NORMAL or TYPE_LOADING in our case)
     */
    @Override
    public int getItemViewType(int position) {
        if(hasPagination && position == getItemCount() - 1) {
            return TYPE_LOADING;
        }
        else {
            return TYPE_NORMAL;
        }
    }

    /**
     * ViewHolder for TYPE_NORMAL row item
     */
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

    /**
     * Method used to add items to the adapter and notify the change
     * @param users - list of users to be added
     */
    public void addItems(ArrayList<User> users) {
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    /**
     * Method used to set total pages of data
     * @param totalPages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        setHasPagination(pageNumber < totalPages);
    }

    /**
     * Method used to set whether there is pagination available or not
     * @param hasPagination
     */
    public void setHasPagination(boolean hasPagination) {
        this.hasPagination = hasPagination;
        notifyDataSetChanged();
    }

    /**
     * Listener used to communicate with view from adapter
     */
    public interface HomeAdapterListener {
        void onClick(User user);
        void loadMoreEntries(int pageNumber);
    }
}

