package com.sfc.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyAdapter extends ArrayAdapter<Button> {
    public MyAdapter(@NonNull Context context, int resource, @NonNull List<Button> objects) {
        super(context, resource, objects);
    }

    //每个子项被滚动到屏幕内的时候会被调用
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Button btn = getItem(position);//得到当前项的 Fruit 实例
        //为每一个子项加载设定的布局v
        View view = LayoutInflater.from(getContext()).inflate(R.layout.main_btn, parent, false);
        //分别获取 image view 和 textview 的实例
        TextView btntext = view.findViewById(R.id.button);
        // 设置要显示的图片和文字
        btntext.setText(btn.getText());
        return view;
    }
}