package com.sfc.myapplication;


import static androidx.core.content.ContextCompat.getDrawable;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public class FlagAdapter extends ArrayAdapter<Flag> {
    public FlagAdapter(@NonNull Context context, int resource, @NonNull List<Flag> objects) {
        super(context, resource, objects);
    }

    //每个子项被滚动到屏幕内的时候会被调用
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Flag flag = getItem(position);//得到当前项的 Fruit 实例
        //为每一个子项加载设定的布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.flag_item, parent, false);
        //分别获取 image view 和 textview 的实例
        ImageView flagimage = view.findViewById(R.id.flag_image);
        TextView flagname = view.findViewById(R.id.flag_name);
        //TextView flagprice = view.findViewById(R.id.flag_price);
        ProgressBar mProgressBar = view.findViewById(R.id.ProgressBar);
        // 设置要显示的图片和文字
        flagimage.setImageResource(flag.getID());
        flagname.setText(flag.getName());
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setMax(200);  // Set the maximum progress value
        if (flag.getOffset() == 0) {
            mProgressBar.setProgress(100);
            //mProgressBar.setSecondaryProgress(50);
        }
        else {

            Log.d("flag.getOffset()", String.valueOf(flag.getOffset()));
            mProgressBar.setProgress((int) (100+flag.getOffset() * 20));
        }
        return view;
    }
}


