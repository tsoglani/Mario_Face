package tsoglani.mario.face.supermarioface;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;



    public class Settings extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);
            change_background_manual = (RadioButton) findViewById(R.id.change_background);
            change_animation_manual = (RadioButton) findViewById(R.id.change_evolution);
            radio_group=(RadioGroup)findViewById(R.id.radio_group);
            do_nothing = (RadioButton) findViewById(R.id.no_change);
            enable_date=(CheckBox) findViewById(R.id.enable_date);
            change_hour_type = (CheckBox) findViewById(R.id.hour_type);
            enable_animation= (CheckBox) findViewById(R.id.enable_animation);
            battery_enable= (CheckBox) findViewById(R.id.enable_battery);
            if (MarioWatchFaceService.isChangingBackgoundByTouch) {
                change_background_manual.setChecked(true);
            }

            else if (MarioWatchFaceService.isChangingAnimationByTouch) {
                change_animation_manual.setChecked(true);
            }
            else if(!MarioWatchFaceService.isChangingAnimationByTouch&&!MarioWatchFaceService.isChangingBackgoundByTouch){
                do_nothing.setChecked(true);
            }


            if (MarioWatchFaceService.is24HourType) {
                change_hour_type.setChecked(true);
            } else {
                change_hour_type.setChecked(false);
            }


            if (MarioWatchFaceService.isEnableAnimation) {
                enable_animation.setChecked(true);
            } else {
                enable_animation.setChecked(false);
            }


            if (MarioWatchFaceService.isBatteryVisible) {
                battery_enable.setChecked(true);
            } else {
                battery_enable.setChecked(false);
            }


            if (MarioWatchFaceService.isDateEnable) {
                enable_date.setChecked(true);
            } else {
                enable_date.setChecked(false);
            }



            addListener();
        }

        private RadioButton change_background_manual,change_animation_manual,do_nothing;
        private RadioGroup radio_group;
        private CheckBox change_hour_type;
        private CheckBox enable_animation,enable_date,battery_enable;

        private void addListener() {

            radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    Log.e("IDD",i+"");

                    if(change_background_manual.getId()==i){
                        storeSharePref(CHANGE_BACKGROUND_ON_CLICK, true);
                        MarioWatchFaceService.isChangingBackgoundByTouch = true;

                        MarioWatchFaceService.isChangingAnimationByTouch = false;
                        storeSharePref(CHANGE_ANIMATION_ON_CLICK, false);
                    }else  if(change_animation_manual.getId()==i){
                        MarioWatchFaceService.isChangingAnimationByTouch = true;
                        storeSharePref(CHANGE_ANIMATION_ON_CLICK, true);

                        storeSharePref(CHANGE_BACKGROUND_ON_CLICK, false);
                        MarioWatchFaceService.isChangingBackgoundByTouch = false;
                    }else  if(do_nothing.getId()==i){
                        storeSharePref(CHANGE_BACKGROUND_ON_CLICK, false);
                        MarioWatchFaceService.isChangingBackgoundByTouch = false;
                        MarioWatchFaceService.isChangingAnimationByTouch = false;
                        storeSharePref(CHANGE_ANIMATION_ON_CLICK, false);
                    }

                }
            });

//        change_background_manual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(change_background_manual.isChecked()) {
//                storeSharePref(CHANGE_BACKGROUND_ON_CLICK, isChecked);
//                MinecraftFace.isChangingBackgoundByTouch = isChecked;
//             if( MinecraftFace.isChangingAnimationByTouch){
//                MinecraftFace.isChangingAnimationByTouch=false;
//                storeSharePref(CHANGE_ANIMATION_ON_CLICK, false);}}
//
//            }
//        });
//        change_animation_manual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//if(change_animation_manual.isChecked()){
//                storeSharePref(CHANGE_ANIMATION_ON_CLICK, isChecked);
//                MinecraftFace.isChangingBackgoundByTouch = isChecked;
//                if( MinecraftFace.isChangingAnimationByTouch) {
//                    MinecraftFace.isChangingAnimationByTouch = false;
//                    storeSharePref(CHANGE_BACKGROUND_ON_CLICK, false);
//                }
//            }}
//        });
//
//        do_nothing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if(do_nothing.isChecked()) {
//                    if(MinecraftFace.isChangingBackgoundByTouch){
//                    storeSharePref(CHANGE_ANIMATION_ON_CLICK, false);
//                        MinecraftFace.isChangingBackgoundByTouch = false;
//                    }
//                    if(MinecraftFace.isChangingAnimationByTouch){
//                    MinecraftFace.isChangingAnimationByTouch = false;
//                    storeSharePref(CHANGE_BACKGROUND_ON_CLICK, false);
//                }}
//            }
//        });

            change_hour_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    storeSharePref(HOUR_TYPE, isChecked);
                    MarioWatchFaceService.is24HourType = isChecked;



                }
            });

            enable_animation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    storeSharePref(ENABLE_ANIMATION, isChecked);
                    MarioWatchFaceService.isEnableAnimation = isChecked;



                }
            });
            battery_enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    storeSharePref(ENABLE_BATTERY, isChecked);
                    MarioWatchFaceService.isBatteryVisible = isChecked;



                }
            });


            enable_date.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    storeSharePref(IS_DATE_ENABLE, isChecked);
                    MarioWatchFaceService.isDateEnable = isChecked;



                }
            });

        }

        protected static final String MY_PREFS_NAME = "SuperMarioFacePref_Mario_watch_face";
        protected static final String CHANGE_BACKGROUND_ON_CLICK = "isChangingBackgoundByTouch_Mario_watch_face";
        protected static final String CHANGE_ANIMATION_ON_CLICK = "isChangingAnimationByTouch_Mario_watch_face";
        protected static final String IS_DATE_ENABLE = "IS_DATE_ENABLE_Mario_watch_face";


        protected static final String HOUR_TYPE = "Hour_type_Mario_watch_face";
        protected static final String ENABLE_ANIMATION = "is enable animation_Mario_watch_face",ENABLE_BATTERY="ENABLE_Battery_mario";


        private boolean getSharedPref(String text, boolean defVal) {
            return getSharedPref(this, text, defVal);
        }

        protected static boolean getSharedPref(Context context, String text, boolean defVal) {
            SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            return prefs.getBoolean(text, defVal);

        }

        public void storeSharePref(String text, boolean value) {

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(text, value);
            editor.commit();
        }


    }

