package com.sfc.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;


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
            EditText usernameInput = findViewById(R.id.username_input);

            SharedPreferences sharedPref = getSharedPreferences("button_state", MODE_PRIVATE);
            int selectedButtonId = sharedPref.getInt("selected_button_id", -1);
            String savedUsername = sharedPref.getString("username","");
            usernameInput.setText(savedUsername);
            Log.d("MyTagsave", savedUsername);
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
        private void saveUsernameInput(){
        EditText textContent = findViewById(R.id.username_input);
        String content = textContent.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences("button_state", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", content);
        Log.d("MyTag", content);
        editor.apply();
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
            saveUsernameInput();
        }
        @Override
        protected void onStop() {
            super.onStop();
            saveSelectedButtonId(mSelectedButtonId);
            saveUsernameInput();
        }
    }

