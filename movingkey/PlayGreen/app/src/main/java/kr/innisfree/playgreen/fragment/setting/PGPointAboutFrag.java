package kr.innisfree.playgreen.fragment.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentFrag;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.TextUtil;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;

/**
 * Created by jooyoung on 2016-04-12.
 */
public class PGPointAboutFrag extends ParentFrag {

    private ArrayList<NetworkData> pointPlusArray, pointMinusArray;
    private LinearLayout layoutPointUse, layoutPointGain;
    private LayoutInflater inflater;

    public static PGPointAboutFrag newInstance() {
        return new PGPointAboutFrag();
    }

    public PGPointAboutFrag() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_about_pg_point, null);
        setCutOffBackgroundTouch(view);
        this.inflater = inflater;
        layoutPointGain = (LinearLayout) view.findViewById(R.id.layout_point_gain);
        layoutPointUse = (LinearLayout) view.findViewById(R.id.layout_point_use);

        setData();

        return view;
    }

    @Override
    public void onUIRefresh() {
        super.onUIRefresh();
        layoutPointGain.removeAllViews();
        layoutPointUse.removeAllViews();

        for (NetworkData item : pointPlusArray) {
            View itemView = inflater.inflate(R.layout.item_about_pg_point, null);
            TextView txtContent = (TextView) itemView.findViewById(R.id.txt_content);
            TextView txtPoint = (TextView) itemView.findViewById(R.id.txt_point);
            TextView txtLimit = (TextView) itemView.findViewById(R.id.txt_limit);
            txtContent.setText(item.POINT_TEXT);

            if (item.POINT_TEXT.equals(getString(R.string.str_how_to_point_add_08))) {
                txtPoint.setText(getString(R.string.str_point_plus01));
            } else if (item.POINT_TEXT.equals(getString(R.string.str_how_to_point_add_13))) {
                txtPoint.setText(getString(R.string.str_point_plus_minus));
            } else {
                txtPoint.setText(getString(R.string.str_point_plus, item.POINT));
            }

            if (!TextUtil.isNull(item.POINT_DEPT) && item.POINT_DEPT.equals(Definitions.YN.YES)) {
                if (item.POINT_TEXT.equals(getString(R.string.str_how_to_point_add_02))) {
                    txtLimit.setText(getString(R.string.str_point_add_limit_5_for_day));
                } else if (item.POINT_TEXT.equals(getString(R.string.str_how_to_point_add_03)) ||
                        item.POINT_TEXT.equals(getString(R.string.str_how_to_point_add_04))) {
                    txtLimit.setText(getString(R.string.str_point_add_limit_20_for_day));
                } else {
                    txtLimit.setText(getString(R.string.str_point_add_sp));
                }
                txtLimit.setVisibility(View.VISIBLE);
            } else {
                txtLimit.setVisibility(View.GONE);
            }
            layoutPointGain.addView(itemView);
        }

        for (NetworkData item : pointMinusArray) {
            View itemView = inflater.inflate(R.layout.item_about_pg_point, null);
            TextView txtContent = (TextView) itemView.findViewById(R.id.txt_content);
            TextView txtPoint = (TextView) itemView.findViewById(R.id.txt_point);
            txtPoint.setTextColor(Color.parseColor("#e9552c"));
            TextView txtLimit = (TextView) itemView.findViewById(R.id.txt_limit);

            if (item.POINT_TEXT.equals(getString(R.string.str_how_to_point_use_08))) {
                txtPoint.setText(getString(R.string.str_point_plus_minus));
            } else {
                txtPoint.setText(getString(R.string.str_point_minus, item.POINT));
            }
            txtContent.setText(item.POINT_TEXT);
            txtLimit.setVisibility(View.GONE);
            layoutPointUse.addView(itemView);
        }

    }

    public void setData() {
        NetworkData item;

        /** plus **/
        pointPlusArray = new ArrayList<NetworkData>();

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_01);
        item.POINT = 1;
        item.POINT_DEPT = Definitions.YN.NO;
        pointPlusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_02);
        item.POINT = 5;
        item.POINT_DEPT = Definitions.YN.YES;
        pointPlusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_03);
        item.POINT = 3;
        item.POINT_DEPT = Definitions.YN.YES;
        pointPlusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_04);
        item.POINT = 1;
        item.POINT_DEPT = Definitions.YN.YES;
        pointPlusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_05);
        item.POINT = 10;
        item.POINT_DEPT = Definitions.YN.NO;
        pointPlusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_06);
        item.POINT = 5;
        item.POINT_DEPT = Definitions.YN.NO;
        pointPlusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_07);
        item.POINT = 100;
        item.POINT_DEPT = Definitions.YN.NO;
        pointPlusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_08);
        item.POINT = 0;
        item.POINT_DEPT = Definitions.YN.YES;
        pointPlusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_09);
        item.POINT = 10;
        item.POINT_DEPT = Definitions.YN.NO;
        pointPlusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_10);
        item.POINT = 50;
        item.POINT_DEPT = Definitions.YN.NO;
        pointPlusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_11);
        item.POINT = 20;
        item.POINT_DEPT = Definitions.YN.NO;
        pointPlusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_12);
        item.POINT = 30;
        item.POINT_DEPT = Definitions.YN.NO;
        pointPlusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_13);
        item.POINT = 0;
        item.POINT_DEPT = Definitions.YN.NO;
        pointPlusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_add_14);
        item.POINT = 50;
        item.POINT_DEPT = Definitions.YN.NO;
        pointPlusArray.add(item);

        /** minus **/
        pointMinusArray = new ArrayList<NetworkData>();

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_use_01);
        item.POINT = 10;
        pointMinusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_use_02);
        item.POINT = 50;
        pointMinusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_use_03);
        item.POINT = 10;
        pointMinusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_use_04);
        item.POINT = 500;
        pointMinusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_use_05);
        item.POINT = 200;
        pointMinusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_use_06);
        item.POINT = 1000;
        pointMinusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_use_07);
        item.POINT = 500;
        pointMinusArray.add(item);

        item = new NetworkData();
        item.POINT_TEXT = getString(R.string.str_how_to_point_use_08);
        item.POINT = 0;
        pointMinusArray.add(item);

        onUIRefresh();
    }

}
