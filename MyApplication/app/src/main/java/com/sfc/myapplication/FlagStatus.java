package com.sfc.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

//public class FlagStatus extends AppCompatActivity {
//    ListView listView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_flag_status);
//        listView = findViewById(R.id.listView);
//        String[] values = new String[] { "Android List View", "Adapter implementation", "Simple List View In Android", "Create List View Android", "Android Example", "List View Source Code", "List View Array Adapter", "Android Example List View" };
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//}

//package com.example.listview1;
//        import androidx.appcompat.app.AppCompatActivity;
//        import android.os.Bundle;
//        import android.view.View;
//        import android.widget.AdapterView;
//        import android.widget.ArrayAdapter;
//        import android.widget.ListView;
//        import android.widget.TextView;
//        import android.widget.Toast;
public class FlagStatus extends Activity {
    //1、定义对象

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_status);
        //2、绑定控件
        listView=(ListView) findViewById(R.id.listView);
        //3、准备数据
        List<Flag> flaglist = new ArrayList<>();
        //String[] data={"テニスコート","Δ棟","諭吉像","スポーツグランド", "鴨池"};
        for (int i = 0; i <1 ; i++) {
            Flag pineapple=new Flag(R.drawable.tennis,"テニスコート",0);
            flaglist.add(pineapple);
            Flag mango = new Flag(R.drawable.monitor, "Δ棟",0);
            flaglist.add(mango);
            Flag pomegranate = new Flag(R.drawable.yukichi, "諭吉像", 0);
            flaglist.add(pomegranate);
            Flag grape = new Flag(R.drawable.library, "Mu",0);
            flaglist.add(grape);
            Flag apple = new Flag(R.drawable.duck, "鴨池",0);
            flaglist.add(apple);
        }

        FlagAdapter adapter=new FlagAdapter(FlagStatus.this,R.layout.flag_item,flaglist);
        listView.setAdapter(adapter);


//        //4、创建适配器 连接数据源和控件的桥梁
//        //参数 1：当前的上下文环境
//        //参数 2：当前列表项所加载的布局文件
//        //(android.R.layout.simple_list_item_1)这里的布局文件是Android内置的，里面只有一个textview控件用来显示简单的文本内容
//        //参数 3：数据源
//        ArrayAdapter<String> adapter=new ArrayAdapter<>(FlagStatus.this,android.R.layout.simple_list_item_1,data);
//        //5、将适配器加载到控件中
//        listView.setAdapter(adapter);
//        //6、为列表中选中的项添加单击响应事件
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
//                String result=((TextView)view).getText().toString();
//                Toast.makeText(FlagStatus.this,"アップデート："+result,Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}