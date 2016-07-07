package kr.innisfree.playgreen.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentFrag;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.user.EmailLoginAct;
import kr.innisfree.playgreen.adapter.CountryAdapter;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-04-18.
 */
public class CountrySelectFrag extends ParentFrag implements AdapterItemClickListener {

	private TextView txtTitle;
	private RecyclerView recyclerView;
	private CountryAdapter adapter;
	private LinearLayout layoutEmpty;
	private TextView txtEmpty;
	private OnCountrySelectListener countrySelectListener;

	public static CountrySelectFrag newInstance() {
		return new CountrySelectFrag();
	}

	public CountrySelectFrag() {}

	public void setCountrySelectListener(OnCountrySelectListener listener) {
		this.countrySelectListener = listener;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.act_simple_list, null);
		setCutOffBackgroundTouch(view);

		/** Init Toolbar*/
		Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
		if(getActivity() instanceof EmailLoginAct){
			mToolbar.setVisibility(View.GONE);
		}else{
			mToolbar.setVisibility(View.VISIBLE);
			view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().onBackPressed();
				}
			});
			((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
			((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
		}

		txtTitle = (TextView) view.findViewById(R.id.txt_title);
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter = new CountryAdapter();
		adapter.setAdapterItemClickListener(this);
		recyclerView.setAdapter(adapter);
		txtTitle.setText(R.string.str_country);

		layoutEmpty = (LinearLayout) view.findViewById(R.id.empty_view);
		txtEmpty = (TextView) view.findViewById(R.id.txt_empty_message);

		initialize();
		return view;
	}

	public void initialize() {
		ArrayList<NetworkData> countryList = new ArrayList<NetworkData>();

		String[] countryNames = getResources().getStringArray(R.array.array_country);
		String[] countryCodes = getResources().getStringArray(R.array.array_country_code);

		for(int i = 0; i<countryNames.length; i++){
			NetworkData country = new NetworkData();
			country.country = countryNames[i];
			country.LOCATION = countryCodes[i];
			countryList.add(country);
		}
		adapter.setItemArray(countryList);
	}

	@Override
	public void onAdapterItemClick(View view, int position) {
		NetworkData selectCountry = adapter.getItem(position);
		adapter.setSelectCountryPosition(position);
		if (countrySelectListener != null) {
			countrySelectListener.OnCountrySelect(selectCountry);
		}
		getActivity().onBackPressed();
	}

	public interface OnCountrySelectListener {
		void OnCountrySelect(NetworkData data);
	}

}
