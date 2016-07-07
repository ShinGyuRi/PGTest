package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.Definitions.YN;
import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

public class SearchLocationAdapter extends RecyclerView.Adapter<SearchLocationAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layoutItem;
        public TextView txtLocation, txtAddress;

        public ViewHolder(View v) {
            super(v);
            layoutItem = (LinearLayout) v.findViewById(R.id.layout_item);
            txtLocation = (TextView) v.findViewById(R.id.txt_item_location);
            txtAddress = (TextView) v.findViewById(R.id.txt_item_location_address);
        }
    }

    private Context context;
    private ArrayList<NetworkData> itemArray;
    private AdapterItemClickListener adapterItemClickListener;

    public void setAdapterItemClickListener(AdapterItemClickListener listener) {
        this.adapterItemClickListener = listener;
    }

    public SearchLocationAdapter(Context context) {
        this.context = context;
    }

    public void setItemArray(ArrayList<NetworkData> array) {
        this.itemArray = array;
        notifyDataSetChanged();
    }

    public NetworkData getItem(int position) {
        if (itemArray == null || itemArray.size() <= position) return null;
        return itemArray.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_location, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (itemArray == null || itemArray.size() <= position) return;
        NetworkData item = itemArray.get(position);

        if (!TextUtil.isNull(item.name))
            holder.txtLocation.setText(item.name);

        if (!TextUtil.isNull(item.vicinity))
            holder.txtAddress.setText(item.vicinity);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterItemClickListener != null)
                    adapterItemClickListener.onAdapterItemClick(v, position);
            }
        };
		holder.layoutItem.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        if (itemArray == null) {
            return 0;
        }
        return itemArray.size();
    }
}
