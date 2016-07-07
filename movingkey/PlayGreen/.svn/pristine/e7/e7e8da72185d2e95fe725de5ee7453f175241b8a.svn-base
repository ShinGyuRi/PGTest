package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.Definitions.YN;
import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;
import java.util.Locale;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imgProfile, imgBadge;
        public TextView txtNickname, txtUserInfo, txtFollow;

        private AdapterItemClickListener adapterItemClickListener;

        public ViewHolder(View v) {
            super(v);
            imgProfile = (ImageView) v.findViewById(R.id.img_profile_item);
            imgBadge = (ImageView) v.findViewById(R.id.img_icon_badge);
            txtNickname = (TextView) v.findViewById(R.id.txt_nickname_item);
            txtUserInfo = (TextView) v.findViewById(R.id.txt_info_item);
            txtFollow = (TextView) v.findViewById(R.id.txt_follow_item);

            imgProfile.setOnClickListener(this);
            txtNickname.setOnClickListener(this);
            txtUserInfo.setOnClickListener(this);
            txtFollow.setOnClickListener(this);
        }

        public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener) {
            this.adapterItemClickListener = adapterItemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (adapterItemClickListener != null)
                adapterItemClickListener.onAdapterItemClick(v, getLayoutPosition());
        }
    }

    private Context context;
    private ArrayList<NetworkData> itemArray;
    private AdapterItemClickListener adapterItemClickListener;

    public FriendAdapter(Context context) {
        this.context = context;
    }

    public void setItemArray(ArrayList<NetworkData> array) {
        this.itemArray = array;
        notifyDataSetChanged();
    }

    public NetworkData getItem(int position) {
        if (itemArray != null && itemArray.size() > position) {
            return itemArray.get(position);
        } else
            return null;
    }

    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        ViewHolder vh = new ViewHolder(view);
        vh.setAdapterItemClickListener(this.adapterItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (itemArray == null || itemArray.size() < position) return;
        NetworkData item = itemArray.get(position);

        holder.txtNickname.setText("");
        holder.txtNickname.setText(item.MEMB_NAME);

        StringBuffer userInfo = new StringBuffer();
//		userInfo.append(context.getString(R.string.str_follower));
//		userInfo.append(" "+TextUtil.makeStringWithComma(item.FOLLOWER_COUNT + "", false));
//		userInfo.append(" / ");
//		userInfo.append(context.getString(R.string.str_following));
//		userInfo.append(" " + TextUtil.makeStringWithComma(item.FOLLOWING_COUNT + "", false));
//		userInfo.append(" / ");
//		userInfo.append(context.getString(R.string.str_pg_point));
//		userInfo.append(" " + TextUtil.makeStringWithComma(item.POINT + "", false) + "p");
        String currentLanguage = Locale.getDefault().getLanguage();
        if (currentLanguage.equals(Locale.ENGLISH.getLanguage())) {
            userInfo.append(context.getString(R.string.str_playgreen_ko));
        } else if (currentLanguage.equals(Locale.CHINA.getLanguage())
                || currentLanguage.equals(Locale.CHINESE.getLanguage())
                || currentLanguage.equals(Locale.SIMPLIFIED_CHINESE.getLanguage())
                || currentLanguage.equals(Locale.TRADITIONAL_CHINESE.getLanguage())) {
            userInfo.append(context.getString(R.string.str_playgreen_ko));
        } else {
            userInfo.append(context.getString(R.string.str_playgreen_ko));
        }
        userInfo.append(" " + TextUtil.makeStringWithComma(item.TIMELINE_COUNT + "", false));
        userInfo.append(" / ");
        userInfo.append(context.getString(R.string.str_title_playgreen_21));
        userInfo.append(" " + TextUtil.makeStringWithComma(item.PG21_DAY + "" + context.getString(R.string.str_day), false));
        holder.txtUserInfo.setText(userInfo.toString());

        if (!TextUtil.isNull(item.MEMB_IMG))
            Picasso.with(context).load(item.MEMB_IMG).transform(new BitmapCircleResize(context, context.getResources().getDimensionPixelOffset(R.dimen.dp_30))).into(holder.imgProfile);

        /** 프로필 뱃지 */
        boolean isSuperGreener = false, isBestGreener = false;
        if (!TextUtils.isEmpty(item.SUPERGREENER_YN) && item.SUPERGREENER_YN.equals(YN.YES))
            isSuperGreener = true;
        if (!TextUtils.isEmpty(item.BESTGREENER_YN) && item.BESTGREENER_YN.equals(YN.YES))
            isBestGreener = true;
        if (isSuperGreener || isBestGreener) {
            holder.imgBadge.setVisibility(View.VISIBLE);
            if (isSuperGreener && isBestGreener)
                holder.imgBadge.setBackgroundResource(R.drawable.icon_badge_both);
            else if (isSuperGreener)
                holder.imgBadge.setBackgroundResource(R.drawable.icon_badge_sg);
            else if (isBestGreener)
                holder.imgBadge.setBackgroundResource(R.drawable.icon_badge_bestpg);
        } else {
            holder.imgBadge.setVisibility(View.GONE);
        }


        if (!TextUtil.isNull(item.FRIEND_YN) && item.FRIEND_YN.equals(YN.YES)) {
            holder.txtFollow.setSelected(true);
        } else {
            holder.txtFollow.setSelected(false);
        }

    }

    @Override
    public int getItemCount() {
        if (itemArray == null) {
            return 0;
        }
        return itemArray.size();
    }
}
