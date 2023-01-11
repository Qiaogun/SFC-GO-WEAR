package com.sfc.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;




    public class TeamSelect extends Activity {
        private Button mButton1;
        private Button mButton2;


        //SharedPreferences sharedPref = getSharedPreferences("button_state", MODE_PRIVATE);
        private int mSelectedButtonId = -1; // -1 indicates no button is selected


        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_team);

            mButton1 = findViewById(R.id.button_green);
            mButton2 = findViewById(R.id.button_red);

            SharedPreferences sharedPref = getSharedPreferences("button_state", MODE_PRIVATE);
            int selectedButtonId = sharedPref.getInt("selected_button_id", -1);

            if (selectedButtonId == R.id.button_green) {
                mButton1.setSelected(true);
                mButton1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lime_green)));
            } else if (selectedButtonId == R.id.button_red) {
                mButton2.setSelected(true);
                mButton2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_red)));
            }

            mButton1.setOnClickListener(view -> {
                if (mSelectedButtonId == R.id.button_green) {
                    // If button 1 is already selected, reset to initial state
                    mSelectedButtonId = -1;
                    mButton1.setSelected(false);
                    mButton1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                } else {
                    // Otherwise, select button 1 and unselect the other button
                    mSelectedButtonId = R.id.button_green;
                    mButton1.setSelected(true);
                    mButton1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lime_green)));
                    mButton2.setSelected(false);
                    mButton2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                }
            });

            mButton2.setOnClickListener(view -> {
                if (mSelectedButtonId == R.id.button_red) {
                    // If button 2 is already selected, reset to initial state
                    mSelectedButtonId = -1;
                    mButton2.setSelected(false);
                    mButton2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                } else {
                    // Otherwise, select button 2 and unselect the other button
                    mSelectedButtonId = R.id.button_red;
                    mButton2.setSelected(true);
                    mButton2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_red)));
                    mButton1.setSelected(false);
                    mButton1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                }
            });
        }
        private void saveSelectedButtonId(int buttonId) {
            SharedPreferences sharedPref = getSharedPreferences("button_state", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("selected_button_id", buttonId);
            editor.apply();
        }
        @Override
        protected void onPause() {
            super.onPause();
            saveSelectedButtonId(mSelectedButtonId);
        }
        @Override
        protected void onStop() {
            super.onStop();
            saveSelectedButtonId(mSelectedButtonId);
        }
    }
