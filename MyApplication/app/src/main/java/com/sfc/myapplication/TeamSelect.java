package com.sfc.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class TeamSelect extends Activity {
        private Button mButton1;
        private Button mButton2;


        //SharedPreferences sharedPref = getSharedPreferences("button_state", MODE_PRIVATE);
        private int mSelectedButtonId; // -1 indicates no button is selected
        private String mSelectedteam; // "" indicates no button is selected
        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_team);

            mButton1 = findViewById(R.id.button_green);
            mButton2 = findViewById(R.id.button_red);
            EditText usernameInput = findViewById(R.id.username_input);

            SharedPreferences sharedPref = getSharedPreferences("button_state", MODE_PRIVATE);
            int selectedButtonId = sharedPref.getInt("selected_button_id", -1);
            String selectedteam = sharedPref.getString("selectedteam", "");
            String savedUsername = sharedPref.getString("username","");
            usernameInput.setText(savedUsername);
            //Log.d("MyTagsave", savedUsername);
            if (selectedteam == "Green") {
                mButton1.setSelected(true);
                mSelectedteam ="Green";
                mButton1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lime_green)));
                showToast("Green Team");
            } 
            else if (selectedteam == "Red") {
                mButton2.setSelected(true);
                mSelectedteam ="Red";
                mButton2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_red)));
                showToast("Red Team");
            }

            mButton1.setOnClickListener(view -> {
                if (mButton1.isSelected()==true) {
                    // If button 1 is already selected, reset to initial state
                    mSelectedButtonId = -1;
                    mButton1.setSelected(false);
                    mSelectedteam = "";
                    mButton1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gary)));
                } else {
                    // Otherwise, select button 1 and unselect the other button
                    mSelectedButtonId = R.id.button_green;
                    mButton1.setSelected(true);
                    mButton1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lime_green)));
                    mSelectedteam ="Green";
                    showToast("Green Team set");
                    mButton2.setSelected(false);
                    mButton2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gary)));
                }
            });

            mButton2.setOnClickListener(view -> {
                if (mButton2.isSelected()==true) {
                    // If button 2 is already selected, reset to initial state
                    mSelectedButtonId = -1;
                    mSelectedteam ="";
                    mButton2.setSelected(false);
                    mButton2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gary)));
                } else {
                    // Otherwise, select button 2 and unselect the other button
                    mSelectedButtonId = R.id.button_red;
                    mButton2.setSelected(true);
                    mButton2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_red)));
                    mButton1.setSelected(false);
                    mSelectedteam ="Red";
                    showToast("Red Team set");
                    mButton1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gary)));
                }
            });
        }
        private void saveUsernameInput(){
        EditText textContent = findViewById(R.id.username_input);
        String content = textContent.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences("button_state", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", content);
        editor.putString("selectedteam", mSelectedteam);
        //Log.d("MyTag", content);
        editor.apply();
        }
        private void saveSelectedButtonId(int buttonId) {
            SharedPreferences sharedPref = getSharedPreferences("button_state", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("selected_button_id", buttonId);
            editor.apply();
        }
    private void showToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
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

