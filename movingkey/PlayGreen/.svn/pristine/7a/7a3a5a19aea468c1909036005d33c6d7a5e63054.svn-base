package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.yazeed44.imagepicker.model.AlbumEntry;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;

/**
 * Created by jooyoung on 2016-04-05.
 */
public class AlbumAdapter extends BaseAdapter {

	public Context context;
	public ArrayList<AlbumEntry> albumList;

	public AlbumAdapter() {
	}

	public void setItemArray(ArrayList<AlbumEntry> array) {
		this.albumList = array;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(albumList!= null ) return  albumList.size();
		else return 0;
	}

	@Override
	public Object getItem(int position) {
		if(albumList== null || albumList.size() <= position)	return null;
		else return albumList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text , parent, false);

		TextView txtAlbum = (TextView)view.findViewById(R.id.txt_album);

		if(albumList==null || albumList.size()<= position) return view;
		else{
			txtAlbum.setText(albumList.get(position).name);
		}
		return view;
	}
}
