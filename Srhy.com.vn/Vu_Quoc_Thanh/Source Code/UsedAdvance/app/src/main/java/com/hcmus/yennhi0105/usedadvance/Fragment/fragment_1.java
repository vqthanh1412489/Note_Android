package com.hcmus.yennhi0105.usedadvance.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hcmus.yennhi0105.usedadvance.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 17/03/2017.
 */

public class fragment_1 extends Fragment {
    ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_1, container, false);


        imageView = (ImageView) fragment.findViewById(R.id.imageViewfrag1);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        ArrayList<String> arrayIcon = new ArrayList<String>();
        arrayIcon = bundle.getStringArrayList("imageArray");

        if (arrayIcon.size() >= 1) {
            Picasso.with(getContext()).load(arrayIcon.get(0)).into(imageView);
        }

    }
}
