package com.sfc.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
class PointData {
    private List<Point> points;

    public List<Point> getPoints() {
        return points;
    }
}

class Point {
    private String name;
    private int red;
    private int green;
    private double offset;

    public String getName() {
        return name;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public double getOffset() {
        return offset;
    }

    public double getStaus(){
        if  (-5 < this.offset && this.offset < 5 && this.offset!=0){
                return 1;
        } else if (this.offset == -5) {
            return 2;
        } else if (this.offset == 5) {
            return  3;
        }
        return 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }
}


public class FlagUI extends Activity {

    private ListView listView;
    private TextView pointView;
    private ProgressBar mProgressBar;
    private final Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

//            new GetDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://43.206.213.194:23333/flagui");
//            new GetPonintTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://43.206.213.194:23333/scorer");

            new GetDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://172.20.10.13:23333/flagui");
            new GetPonintTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://172.20.10.13:23333/scorer");

            mHandler.postDelayed(mRunnable, 4000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flagui);
        listView = findViewById(R.id.listView);
        pointView = findViewById(R.id.textView2);
        mProgressBar = findViewById(R.id.progress_bar);
        listView.setEmptyView(mProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mHandler.postDelayed(mRunnable, 0);
    }

    private class GetPonintTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int status = connection.getResponseCode();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                connection.disconnect();
                return content.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (res != null) {
                pointView.setText(res);
            }
        }
    }
    private class GetDataTask extends AsyncTask<String, Void, List<Point>> {
        @Override
        protected List<Point> doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("accept", "application/json");
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                String json = builder.toString();
                JSONArray jsonArray = new JSONArray(json);
                List<Point> points = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Point point = new Point();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject != null) {
                        point.setName(jsonObject.optString("name"));
                        point.setRed(jsonObject.optInt("red"));
                        point.setGreen(jsonObject.optInt("green"));
                        point.setOffset(jsonObject.optDouble("offset"));
                    }
                    points.add(point);
                }
                return points;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Point> points) {
            super.onPostExecute(points);
            List<Flag> basePoints = new ArrayList<>();
            if (points != null) {
                List<String> array = new ArrayList<>();
                for (Point p : points) {
                    Flag base = new Flag(R.drawable.placeholder,p.getName(), (float) p.getOffset());
                    basePoints.add(base);
                    //BasePoints.add(new Flag(R.drawable.flag, p.getName(), String.format("Red Team人数%d Green Team人数%d \n offset(%f)", p.getRed(), p.getGreen(), p.getOffset())));
                    //array.add(String.format("%s: \n" +
                    //        "Red Team人数%d Green Team人数%d \n offset(%f)", p.getName(), p.getRed(), p.getGreen(), p.getOffset()));
                }
                //ArrayAdapter<String> adapter = new ArrayAdapter<>(FlagUI.this, android.R.layout.simple_list_item_1, array);
                FlagAdapter adapter=new FlagAdapter(FlagUI.this,R.layout.flag_item,basePoints);
                listView.setAdapter(adapter);
                mProgressBar.setVisibility(View.GONE);
            }
        }

    }

    private class MyAdapter extends BaseAdapter {
        private List<Point> mData;

        public void setData(PointData data) {
            mData = data.getPoints();
            List<Point> flaglist = new ArrayList<>();
            for (Point flag:flaglist ) {

            }

//            FlagAdapter adapter=new FlagAdapter(getApplicationContext(),R.layout.flag_item,flaglist);
//            listView.setAdapter(adapter);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
