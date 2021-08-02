package com.yuvalsuede.homefood;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yuvalsuede.homefood.model.Category;
import com.yuvalsuede.homefood.model.Cuisine;
import com.yuvalsuede.homefood.model.Currency;
import com.yuvalsuede.homefood.model.DishModel;
import com.yuvalsuede.homefood.model.FavouriteDishId;
import com.yuvalsuede.homefood.model.UserConfig;
import com.yuvalsuede.homefood.picasso.CircleTransform;

import java.text.DecimalFormat;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = RVAdapter.class.getName();
    private static final int CATEGORY_VIEW_TYPE = 0, ROW_VIEW_TYPE = 1;

    private List<Object> items;
    private List<FavouriteDishId> favouriteDishIds;
    private Context mContext;
    private RowViewHolderClicks listener;
    private App app;

    public RVAdapter(Context context, List<Object> items, List<FavouriteDishId> favouriteDishIds, RowViewHolderClicks listener, App app) {
        this.mContext = context;
        this.items = items;
        this.favouriteDishIds = favouriteDishIds;
        this.listener = listener;
        this.app = app;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryTitle;
        ImageView categoryPhoto;
        Button categoryDishes;
        private Category category;
        RowViewHolderClicks listener;

        CategoryViewHolder(View itemView, final RowViewHolderClicks mListener) {
            super(itemView);
            listener = mListener;
            categoryTitle = (TextView) itemView.findViewById(R.id.title_category_item);
            categoryPhoto = (ImageView) itemView.findViewById(R.id.image_category_item);
            categoryDishes = (Button) itemView.findViewById(R.id.button_category_item);
            categoryDishes.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onCategoryClick(category);
        }

        public void setCategory(Category category) {
            this.category = category;
        }
    }

    public static class CuisineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryTitle;
        ImageView categoryPhoto;
        Button categoryDishes;
        private Cuisine cuisine;
        RowViewHolderClicks listener;

        CuisineViewHolder(View itemView, final RowViewHolderClicks mListener) {
            super(itemView);
            listener = mListener;
            categoryTitle = (TextView) itemView.findViewById(R.id.title_category_item);
            categoryPhoto = (ImageView) itemView.findViewById(R.id.image_category_item);
            categoryDishes = (Button) itemView.findViewById(R.id.button_category_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onCuisineClick(cuisine);
        }

        public void setCuisine(Cuisine cuisine) {
            this.cuisine = cuisine;
        }
    }

    public static class RowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView rowTitle;
        TextView rowCategory;
        TextView rowPrice;
        ImageView rowImage;
        ImageView rowUserImage;
        ImageView rowLikeImage;
        RowViewHolderClicks listener;
        boolean isLiked = false;

        RowViewHolder(View itemView, final RowViewHolderClicks mListener) {
            super(itemView);
            listener = mListener;
            rowTitle = (TextView) itemView.findViewById(R.id.row_title);
            rowTitle.setOnClickListener(this);
            rowCategory = (TextView) itemView.findViewById(R.id.row_category);
            rowCategory.setOnClickListener(this);
            rowPrice = (TextView) itemView.findViewById(R.id.row_price);
            rowPrice.setOnClickListener(this);
            rowImage = (ImageView) itemView.findViewById(R.id.row_image);
            rowImage.setOnClickListener(this);
            rowUserImage = (ImageView) itemView.findViewById(R.id.row_icon_user);
            rowUserImage.setOnClickListener(this);
            rowLikeImage = (ImageView) itemView.findViewById(R.id.row_details_like);
            rowLikeImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == rowLikeImage.getId()) {
                listener.onLikeClick((ImageView) view, getAdapterPosition(), isLiked, rowLikeImage);
            } else if (view.getId() == rowUserImage.getId()) {
                listener.onUserImageClick((ImageView) view, getAdapterPosition());
            } else {
                listener.onDishClick(view, getAdapterPosition());
            }
        }
    }

    public interface RowViewHolderClicks {
        void onLikeClick(ImageView likeImage, int position, boolean isLiked, ImageView imageView);

        void onUserImageClick(ImageView userImage, int position);

        void onDishClick(View view, int position);

        void onCategoryClick(Category category);

        void onCuisineClick(Cuisine cuisine);
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Category) {
            return CATEGORY_VIEW_TYPE;
        } else if (items.get(position) instanceof DishModel) {
            return ROW_VIEW_TYPE;
        } else {
            return -1;
        }
    }

    public void setFavouriteDishIds(List<FavouriteDishId> favouriteDishIds) {
        this.favouriteDishIds = favouriteDishIds;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else
            return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case ROW_VIEW_TYPE:
                View v1 = inflater.inflate(R.layout.category_item_row, viewGroup, false);
                viewHolder = new RowViewHolder(v1, listener);
                break;
            case CATEGORY_VIEW_TYPE:
                View v2 = inflater.inflate(R.layout.category_item, viewGroup, false);
                viewHolder = new CategoryViewHolder(v2, listener);
                break;
            default:
                View v3 = inflater.inflate(R.layout.category_item, viewGroup, false);
                viewHolder = new CuisineViewHolder(v3, listener);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case ROW_VIEW_TYPE:
                final RowViewHolder rowViewHolder = (RowViewHolder) holder;
                DishModel mDish = (DishModel) items.get(position);
                if (mDish.getLikes() == null) {
                    mDish.setLikes(0);
                }
                rowViewHolder.rowTitle.setText(mDish.getTitle());

                if (mDish.getCuisineId() != null) {
                    if (App.getInstance().getCuisineById(mDish.getCuisineId()).getCuisineName() != null) {
                        Location userLocation = app.getLocation();
                        Location dishLocation = new Location("");
                        dishLocation.setLatitude(mDish.getLat());
                        dishLocation.setLongitude(mDish.getLng());
                        String dist = "";
                        if (userLocation != null) {
                            float distInMetersF = userLocation.distanceTo(dishLocation);
                            int distance = Math.round(distInMetersF);
                            dist = (distance < 5000 ? distance + mContext.getString(R.string.meters) : distance / 1000 + mContext.getString(R.string.search_km_text));
                            UserConfig userConfig = app.getUserConfig();
                            if (userConfig != null) {
                                int dis = userConfig.getUserDistanceUnit();
                                if (dis == 1) {
                                    distInMetersF = distance / 1.6F;
                                    distance = Math.round(distInMetersF);
                                    dist = (distance / 1000 + mContext.getString(R.string.search_miles_text));
                                }
                            }
                        }
                        String likes = (mDish.getLikes() == 1 ? mDish.getLikes() + mContext.getString(R.string.details_like) : mDish.getLikes() + mContext.getString(R.string.details_likes));
                        String cuisine = App.getInstance().getCuisineById(mDish.getCuisineId()).getCuisineName();
                        rowViewHolder.rowCategory.setText(cuisine + " • " + likes + " • " + dist);
                    }
                }
                setPrice(rowViewHolder.rowPrice, mDish);

//                Picasso.with(mContext)
//                        .load(Cloudinary.getImageLinkFromCloudinary(mDish.getMainPhoto()))
//                        .resize(600, 400)
//                        .centerCrop()
//                        .placeholder(R.drawable.placeholder_mana)
//                        .into(rowViewHolder.rowImage);

                new DownloadImageTask(rowViewHolder.rowImage).execute(Cloudinary.getImageLinkFromCloudinary(mDish.getMainPhoto()));

                Picasso.with(mContext)
                        .load(Cloudinary.getImageLinkFromCloudinary(mDish.getProfileImgUrl()))
                        .resize(300, 300)
                        .placeholder(R.drawable.placeholder_person)
                        .transform(new CircleTransform())
                        .into(rowViewHolder.rowUserImage);

                boolean like = false;
                if (favouriteDishIds != null) {
                    for (int i = 0; i < favouriteDishIds.size(); i++) {
                        if (favouriteDishIds.get(i).getDishId().equals(mDish.getDishId())) {
                            like = true;
                            break;
                        }
                    }
                    if (like) {
                        rowViewHolder.rowLikeImage.setImageResource(R.drawable.icon_love_full);
                        rowViewHolder.isLiked = true;
                    } else {
                        rowViewHolder.rowLikeImage.setImageResource(R.drawable.icon_love_none);
                        rowViewHolder.isLiked = false;
                    }
                }

                break;
            case CATEGORY_VIEW_TYPE:
                CategoryViewHolder viewHolder = (CategoryViewHolder) holder;
                Category mCategory = (Category) items.get(position);
                viewHolder.setCategory(mCategory);
                viewHolder.categoryTitle.setText(mCategory.getCategoryName());

                Picasso.with(mContext)
                        .load(Cloudinary.getImageLinkFromCloudinary(mCategory.getImgUrl()))
                        .resize(600, 400)
                        .placeholder(R.drawable.placeholder_cat_cuisine)
                        .into(viewHolder.categoryPhoto);

                viewHolder.categoryDishes.setText(String.format(mContext.getString(R.string.count_dishes), mCategory.getCount()));
                viewHolder.categoryDishes.setVisibility(View.VISIBLE);

                break;
            default:
                CuisineViewHolder viewCuisine = (CuisineViewHolder) holder;
                Cuisine mCuisine = (Cuisine) items.get(position);
                viewCuisine.setCuisine(mCuisine);
                viewCuisine.categoryTitle.setText(mCuisine.getCuisineName());

                Picasso.with(mContext)
                        .load(Cloudinary.getImageLinkFromCloudinary(mCuisine.getImgUrl()))
                        .resize(600, 400)
                        .placeholder(R.drawable.placeholder_cat_cuisine)
                        .into(viewCuisine.categoryPhoto);

                viewCuisine.categoryDishes.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setPrice(TextView textView, DishModel mDish) {
        String dishCurS = mDish.getCurrency();
        Currency dishCur = app.getCurrency(dishCurS);
        String symbol;
        if (dishCur == null) {
            symbol = mDish.getCurrency();
        } else {
            symbol = dishCur.getSymbol();
        }
        textView.setText(symbol + "" + mDish.getPrice());
        UserConfig userConfig = app.getUserConfig();
        if (userConfig != null) {
            Currency userCurrency = app.getCurrency(userConfig.getUserCurrency());
            if (dishCur != null && userCurrency != null && !dishCur.getCode().equals(userCurrency.getCode())) {

                double price = Double.parseDouble(mDish.getBaseCurrency());
                double currentUsdExchangeRate = Double.parseDouble(userCurrency.getCurrentUsdExchangeRate());
                double real = price * currentUsdExchangeRate;
                DecimalFormat formatter = new DecimalFormat("#0.0");
                String s = formatter.format(real);
                textView.setText(userCurrency.getSymbol() + "" + s);
            }
        }
    }
}
