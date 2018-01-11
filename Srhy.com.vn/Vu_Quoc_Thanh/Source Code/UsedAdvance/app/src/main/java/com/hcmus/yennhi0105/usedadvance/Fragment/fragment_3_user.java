package com.hcmus.yennhi0105.usedadvance.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hcmus.yennhi0105.usedadvance.CClass.CHighLightProduct;
import com.hcmus.yennhi0105.usedadvance.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 25/03/2017.
 */

public class fragment_3_user extends Fragment {
    ImageView imgIcon;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_3_user, container, false);
        imgIcon = (ImageView) fragment.findViewById(R.id.imageViewFrag3User);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle myBundle = getArguments();
        if (myBundle != null) {
            ArrayList<CHighLightProduct> arrayList = myBundle.getParcelableArrayList("arayHLUser");
            String linkIcon = "https://img.youtube.com/vi/" + arrayList.get(2).getLinkVideo() + "/hqdefault.jpg";
            Picasso.with(getContext()).load(linkIcon).into(imgIcon);
        }
    }
}
