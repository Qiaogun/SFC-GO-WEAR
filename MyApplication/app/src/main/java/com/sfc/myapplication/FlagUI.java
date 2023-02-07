package com.sfc.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
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

    ListView listView;
    TextView pointView;
    private ListView mListView;
    private MyAdapter mAdapter;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            new GetDataTask().execute("http://192.168.88.24:23333/flagui");
            new GetPonintTask().execute("http://192.168.88.24:23333/scorer");
            mHandler.postDelayed(mRunnable, 5000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flagui);
        listView = findViewById(R.id.listView);
        pointView = findViewById(R.id.textView2);
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
                System.out.println("Response: " + content.toString());
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
    private class GetDataTask extends AsyncTask<String, Void, PointData> {

        @Override
        protected PointData doInBackground(String... params) {

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
                PointData pointData = new PointData();
                Point point = new Point();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject != null) {
                        point.setName(jsonObject.optString("name"));
                        point.setRed(jsonObject.optInt("red"));
                        point.setGreen(jsonObject.optInt("green"));
                        point.setOffset(jsonObject.optDouble("offset"));
                    }
                    pointData.getPoints().add(point);
                }
                return pointData;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(PointData myModels) {
            super.onPostExecute(myModels);
            if (myModels != null) {
                mAdapter.setData(myModels);
                mAdapter.notifyDataSetChanged();
            }
        }

        public void execute(String s) {
        }

    }

    private class MyAdapter extends BaseAdapter {
        private List<Point> mData;

        public void setData(PointData data) {
            mData = data.getPoints();
            List<Point> flaglist = new ArrayList<>();
            for (Point flag:flaglist ) {
                flag

            }

            FlagAdapter adapter=new FlagAdapter(FlagStatus.this,R.layout.flag_item,flaglist);
            listView.setAdapter(adapter);
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
