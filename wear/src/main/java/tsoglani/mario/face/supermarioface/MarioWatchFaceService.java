/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tsoglani.mario.face.supermarioface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.wearable.view.CardFrame;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.wearable.Wearable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MarioWatchFaceService extends CanvasWatchFaceService {
    private static final int JUMP_DOWN = 2;
    private static final int JUMP_NO = 0;
    private static final int JUMP_UP = 1;
    private static final float ANIM_STEP = 15f;
    private static final float BASELINE_H = 47f;
    private static final float BLOCK_Y = 36f;
    private static float MARIO_MAX_Y = 28f;
    private static float ORIGINAL_NUMBER_X1 = 120f;
    private static float ORIGINAL_NUMBER_X2 = 188f;
    private static float ORIGINAL_NUMBER_X3 = 250f;
    private static float ORIGINAL_NUMBER_X4 = 318f;
    private static final float ORIGINAL_NUMBER_Y = 72f;
    private static final String MY_PREFS_NAME = "MarioPref";
    public static final String evolutionSharedPrefString = "HasEvolution";
    public static final String animationSharedPrefString = "HasAnimation";

    private final Rect cardBounds;
    private final Paint mainPaint;

    private float blockStartX;
    private float blockStartY;
    private float currentBlockY;
    private float step_animation;
    private int blockDirection;
    private int currentHour;
    private int currentMinute;
    private float mainScale;
    private int marioDirection;
    private float marioStartX;
    private float marioStartY;
    private float currentMarioY;
    private float maxMarioY;
    private float numberStartY;
    private float numberHourX1;
    private float numberHourX2;
    private float numberMinuteX1;
    private float numberMinuteX2;
    private float currentNumberY;
    private int previousMinute = -1;
    private boolean isBlockAnimated;
    private boolean isMarioAnimated;
    private boolean isAnimatedByForce;
    public static boolean isAnimateMode = true,isDateEnable=false;
    public static boolean isChangingAnimationByTouch = true,isChangingBackgoundByTouch=false,is24HourType=true,isEnableAnimation=true;


//    private boolean getSharedPref(String text, boolean defVal) {
//        SharedPreferences prefs = Settings.getSharedPreferences(this,MY_PREFS_NAME, MODE_PRIVATE);
//        return prefs.getBoolean(text, defVal);
//
//    }
//
//    public static void storeSharePref(Context context, String text, boolean value) {
//        if (text.equals(evolutionSharedPrefString)) {
//            isChangingAnimationByTouch = value;
//        } else if (text.equals(animationSharedPrefString)) {
//            isAnimateMode = value;
//        }
//        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//        editor.putBoolean(text, value);
//        editor.commit();
//    }

    private class Engine extends android.support.wearable.watchface.CanvasWatchFaceService.Engine {


        private GoogleApiClient googleClient;
        private final BroadcastReceiver TZReceiver;
        private final Handler timeHandler;
        boolean isBIProtection;
        private Calendar currentCalendar;
        private int marioSteps;
        private boolean registeredTZReceiver;
        private int curentSecond;


        private Bitmap seconds_amb_bitmap;
        private Bitmap backgroundBitmap_amb;
        private Bitmap mBackgroundScaledBitmap_amb;
        private Bitmap blockBitmap_amb;
        private Bitmap blockScaledBitmap_amb;
        private Bitmap mario_amb_bitmap;
        private Bitmap marioScaled_amb_bitmap;
        private Bitmap zero_amb_bitmap;
        private Bitmap one_amb_bitmap;
        private Bitmap two_amb_bitmap;
        private Bitmap three_amb_bitmap;
        private Bitmap four_amb_bitmap;
        private Bitmap five_amb_bitmap;
        private Bitmap six_amb_bitmap;
        private Bitmap seven_amb_bitmap;
        private Bitmap eight_amb_bitmap;
        private Bitmap nine_amb_bitmap;
        private Bitmap scaledZero_amb_bitmap;
        private Bitmap scaledOne_amb_bitmap;
        private Bitmap scaledTwo_amb_bitmap;
        private Bitmap scaledThree_amb_bitmap;
        private Bitmap scaledFour_amb_bitmap;
        private Bitmap mScaledFive_amb_bitmap;
        private Bitmap scaledEight_amb_bitmap;
        private Bitmap scaledNine_amb_bitmap;
        private Bitmap scaledSix_amb_bitmap;
        private Bitmap scaledSeven_amb_bitmap;

        private Bitmap backgroundBitmap;
        private Bitmap backgroundScaledBitmap;
        private Bitmap blockBitmap;
        private Bitmap blockScaledBitmap;
        private Bitmap mario_bitmap;
        private Bitmap marioJump_bitmap;
        private Bitmap marioJumpScaled_bitmap;
        private Bitmap marioScaled_bitmap;
        private Bitmap zero_bitmap;
        private Bitmap one_bitmap;
        private Bitmap two_bitmap;
        private Bitmap three_bitmap;
        private Bitmap four_bitmap;
        private Bitmap five_bitmap;
        private Bitmap six_bitmap;
        private Bitmap seven_bitmap;
        private Bitmap eight_bitmap;
        private Bitmap nine_bitmap;
        private Bitmap seconds_bitmap;
        private Bitmap scaledZero_bitmap;
        private Bitmap scaledOne_bitmap;
        private Bitmap scaledTwo_bitmap;
        private Bitmap scaledThree_bitmap;
        private Bitmap scaledFour_bitmap;
        private Bitmap scaledFive_bitmap;
        private Bitmap scaledSix_bitmap;
        private Bitmap scaledSeven_bitmap;
        private Bitmap scaledEight_bitmap;
        private Bitmap scaledNine_bitmap;


        class MyBroadcast extends BroadcastReceiver {
            MyBroadcast() {
            }

            public void onReceive(Context context, Intent intent) {
                currentCalendar.setTimeZone(TimeZone.getDefault());
                Engine.this.invalidate();
            }
        }

        class MyHandler extends Handler {
            MyHandler() {
            }

            public void handleMessage(Message message) {
                switch (message.what) {
                    case MarioWatchFaceService.JUMP_NO:
                        Engine.this.invalidate();
                        if (Engine.this.shouldTimerBeRunning()) {
                            Engine.this.timeHandler.sendEmptyMessageDelayed(MarioWatchFaceService.JUMP_NO, 50 - (System.currentTimeMillis() % 50));
                        }
                    default:
                }
            }
        }

        private void addMarioStep(int second) {


            if (curentSecond == second) {
                return;
            }
            marioSteps++;
            if (!isAnimateMode) {
                marioSteps = 1;
            }
            curentSecond = second;
            if (marioSteps > 3) {
                marioSteps = 0;
            }
            switch (marioSteps) {
                case 0:
                    setRun0Image();
                    break;
                case 1:
                    setRun1Image();
                    break;
                case 2:
                    setRun2Image();
                    break;
                case 3:
                    setRun3Image();
                    break;


            }
            if (isAnimateMode) {
                animateStage();
            }

            marioScaled_bitmap = Bitmap.createScaledBitmap(mario_bitmap, (int) (((float) mario_bitmap.getWidth()) * mainScale), (int) (((float) mario_bitmap.getHeight()) * mainScale), true);


        }


        private void animateStage() {
        }

        private void setRun0Image() {
            switch (curentChapter) {
                case miniMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_l)).getBitmap();
                    break;
                case bigMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_big_l)).getBitmap();
                    break;
                case fireMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_fire_l)).getBitmap();
                    break;
                case tailMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_tail_l)).getBitmap();
                    break;
                case frogMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_frog_l)).getBitmap();
                    break;
                case skiouroseMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_skiouros_l)).getBitmap();
                    break;
                case tankMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_tank_l)).getBitmap();
                    break;
            }
        }

        private void setRun1Image() {
            switch (curentChapter) {
                case miniMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario)).getBitmap();
                    break;
                case bigMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_big)).getBitmap();
                    break;
                case fireMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_fire)).getBitmap();
                    break;
                case tailMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_tail)).getBitmap();
                    break;
                case frogMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_frog)).getBitmap();
                    break;
                case skiouroseMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_skiouros)).getBitmap();
                    break;
                case tankMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_tank)).getBitmap();
                    break;
            }
        }

        private void setRun2Image() {
            switch (curentChapter) {
                case miniMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_r)).getBitmap();
                    break;
                case bigMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_big_r)).getBitmap();
                    break;
                case fireMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_fire_r)).getBitmap();
                    break;
                case tailMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_tail_r)).getBitmap();
                    break;
                case frogMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_frog_r)).getBitmap();
                    break;
                case skiouroseMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_skiouros_r)).getBitmap();
                    break;
                case tankMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_tank_r)).getBitmap();
                    break;
            }

        }


        int backgroundIndex=0;
        final int numberOfBackgrounds=5;
        private void changeMarioBackground(){
            backgroundIndex++;
            if (backgroundIndex>=numberOfBackgrounds){
                backgroundIndex=0;
            }

            switch (backgroundIndex){
                case 0:
                    backgroundBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.mario_bg_320_320, null)).getBitmap();
                    backgroundBitmap_amb = ((BitmapDrawable) getResources().getDrawable(R.drawable.amb_mario_bg_320_320, null)).getBitmap();
                    break;
                case 1:
                    backgroundBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.mario2, null)).getBitmap();
                    backgroundBitmap_amb = ((BitmapDrawable) getResources().getDrawable(R.drawable.mario2_abc, null)).getBitmap();
                    break;
                case 2:
                    backgroundBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.mario3, null)).getBitmap();
                    backgroundBitmap_amb = ((BitmapDrawable) getResources().getDrawable(R.drawable.mario3_abc, null)).getBitmap();
                    break;
                case 3:
                    backgroundBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.mario4, null)).getBitmap();
                    backgroundBitmap_amb = ((BitmapDrawable) getResources().getDrawable(R.drawable.mario4_abc, null)).getBitmap();
                    break;
                case 4:
                    backgroundBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.mario5, null)).getBitmap();
                    backgroundBitmap_amb = ((BitmapDrawable) getResources().getDrawable(R.drawable.mario5_abc, null)).getBitmap();
                    break;
//                case 5:
//                    backgroundBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.mario6, null)).getBitmap();
//                    backgroundBitmap_amb = ((BitmapDrawable) getResources().getDrawable(R.drawable.mario6_abc, null)).getBitmap();
//                    break;

            }

            backgroundScaledBitmap = getScaledBitmap(backgroundBitmap);
            mBackgroundScaledBitmap_amb = getScaledBitmap(backgroundBitmap_amb);

        }

        private void setRun3Image() {
            setRun1Image();
        }


        final int miniMario = 0;
        final int bigMario = 1;
        final int fireMario = 2;
        final int tailMario = 3;
        final int frogMario = 4;
        final int skiouroseMario = 5;
        final int tankMario = 6;
        int curentChapter;



        private void changeMarioChapter() {
            if (isMarioAnimated || !isChangingAnimationByTouch) {
                return;
            }
            curentChapter++;
            if (curentChapter > 6) {
                curentChapter = 0;
            }


            switch (curentChapter) {
                case miniMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario)).getBitmap();
                    marioJump_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_jump)).getBitmap();
                    mario_amb_bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.amb_mario, null)).getBitmap();

                    break;
                case bigMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_big)).getBitmap();
                    marioJump_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_big_jump)).getBitmap();
                    mario_amb_bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.amb_mario_big, null)).getBitmap();


                    break;
                case fireMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_fire)).getBitmap();
                    marioJump_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_fire_jump)).getBitmap();
                    mario_amb_bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.amb_mario_fire, null)).getBitmap();

                    break;
                case tailMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_tail)).getBitmap();
                    marioJump_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_tail_jump)).getBitmap();
                    mario_amb_bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.amb_mario_tail, null)).getBitmap();

                    break;
                case frogMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_frog)).getBitmap();
                    marioJump_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_frog_jump)).getBitmap();
                    mario_amb_bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.amb_mario_frog, null)).getBitmap();

                    break;
                case skiouroseMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_skiouros)).getBitmap();
                    marioJump_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_skiouros_jump)).getBitmap();
                    mario_amb_bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.amb_mario_skiouros, null)).getBitmap();

                    break;
                case tankMario:
                    mario_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_tank)).getBitmap();
                    marioJump_bitmap = ((BitmapDrawable) getDrawable(R.drawable.mario_tank_jump)).getBitmap();
                    mario_amb_bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.amb_mario_tank, null)).getBitmap();
                    break;
            }

            marioScaled_bitmap = Bitmap.createScaledBitmap(mario_bitmap, (int) (((float) mario_bitmap.getWidth()) * mainScale), (int) (((float) mario_bitmap.getHeight()) * mainScale), true);
            marioJumpScaled_bitmap = Bitmap.createScaledBitmap(marioJump_bitmap, (int) (((float) marioJump_bitmap.getWidth()) * mainScale), (int) (((float) marioJump_bitmap.getHeight()) * mainScale), true);

            WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display display = window.getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();

            marioStartX = (float) ((width - marioScaled_bitmap.getWidth()) / JUMP_DOWN);
            marioStartY = (((float) backgroundScaledBitmap.getHeight()) - (BASELINE_H * mainScale)) - ((float) marioScaled_bitmap.getHeight());
            currentMarioY = marioStartY;
            marioScaled_amb_bitmap = Bitmap.createScaledBitmap(mario_amb_bitmap, (int) (((float) mario_amb_bitmap.getWidth()) * mainScale), (int) (((float) mario_amb_bitmap.getHeight()) * mainScale), true);


        }


        private Engine() {
            super();
            registeredTZReceiver = false;
            timeHandler = new MyHandler();
            TZReceiver = new MyBroadcast();
        }

        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            Resources resources = getResources();
            googleClient = new Builder(MarioWatchFaceService.this).addApi(Wearable.API).build();
            setWatchFaceStyle(new WatchFaceStyle.Builder(MarioWatchFaceService.this).setCardPeekMode(1).setBackgroundVisibility(0).setShowSystemUiTime(false).setAcceptsTapEvents(true).setHotwordIndicatorGravity(81).setStatusBarGravity(49).build());
            backgroundBitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.mario_bg_320_320, null)).getBitmap();
            mario_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.mario, null)).getBitmap();
            blockBitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.block2, null)).getBitmap();
            zero_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.number0, null)).getBitmap();
            one_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.number1, null)).getBitmap();
            two_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.number2, null)).getBitmap();
            three_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.number3, null)).getBitmap();
            four_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.number4, null)).getBitmap();
            five_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.number5, null)).getBitmap();
            six_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.number6, null)).getBitmap();
            seven_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.number7, null)).getBitmap();
            eight_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.number8, null)).getBitmap();
            nine_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.number9, null)).getBitmap();
            seconds_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.seconds, null)).getBitmap();

            backgroundBitmap_amb = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_mario_bg_320_320, null)).getBitmap();
            mario_amb_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_mario, null)).getBitmap();
            marioJump_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.mario_jump, null)).getBitmap();
            blockBitmap_amb = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_block2, null)).getBitmap();
            zero_amb_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_number0, null)).getBitmap();
            one_amb_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_number1, null)).getBitmap();
            two_amb_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_number2, null)).getBitmap();
            three_amb_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_number3, null)).getBitmap();
            four_amb_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_number4, null)).getBitmap();
            five_amb_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_number5, null)).getBitmap();
            six_amb_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_number6, null)).getBitmap();
            seven_amb_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_number7, null)).getBitmap();
            eight_amb_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_number8, null)).getBitmap();
            nine_amb_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_number9, null)).getBitmap();
            seconds_amb_bitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.amb_seconds, null)).getBitmap();

            currentCalendar = Calendar.getInstance();
            currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = currentCalendar.get(Calendar.MINUTE);
            isAnimateMode = Settings.getSharedPref(getApplicationContext(),Settings.ENABLE_ANIMATION, true);
            isChangingAnimationByTouch = Settings.getSharedPref(getApplicationContext(),Settings.CHANGE_ANIMATION_ON_CLICK, true);
            isChangingBackgoundByTouch = Settings.getSharedPref(getApplicationContext(),Settings.CHANGE_BACKGROUND_ON_CLICK, false);
            isDateEnable= Settings.getSharedPref(getApplicationContext(),Settings.IS_DATE_ENABLE, false);
        }


        private void updateTimer() {
            timeHandler.removeMessages(MarioWatchFaceService.JUMP_NO);
            if (shouldTimerBeRunning()) {
                timeHandler.sendEmptyMessage(MarioWatchFaceService.JUMP_NO);
            }
        }

        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                registeredTZReceiver = true;
                registerReceiver(TZReceiver, new IntentFilter("android.intent.action.TIMEZONE_CHANGED"));
                googleClient.connect();
                currentCalendar.setTimeZone(TimeZone.getDefault());
            } else {
                if (registeredTZReceiver) {
                    registeredTZReceiver = false;
                    unregisterReceiver(TZReceiver);
                }
                releaseGoogleClient();
            }
            updateTimer();
        }


        private int curentHour = -1;
        private int curentMin = -1;
        boolean last24TypeUsed=true;
        public void onDraw(Canvas canvas, Rect bounds) {
            int tempHour;
            int timeTextOneByOne;
            int tempMinute;
            String hourExtra=null;
            WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display display = window.getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            currentCalendar.setTimeInMillis(System.currentTimeMillis());
            if (isInAmbientMode()) {
                canvas.drawBitmap(mBackgroundScaledBitmap_amb, 0, 0, null);
            } else {
                canvas.drawBitmap(backgroundScaledBitmap, 0, 0, null);
            }

            currentMinute = currentCalendar.get(Calendar.MINUTE);
            Calendar calendar = currentCalendar;






            currentHour = calendar.get(Calendar.HOUR_OF_DAY);


            if (!is24HourType) {
                hourExtra=(curentHour>12)?"AM":"PM";
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                Date date = new Date();

                String newTimeFormat = sdf.format(date);

                try {
                    String newHourFormat = newTimeFormat.split(":")[0];
                tempHour= currentHour = Integer.parseInt(newHourFormat);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (last24TypeUsed!=is24HourType){
                curentHour=currentHour;
            }
            last24TypeUsed=is24HourType;

            if (curentMin == -1) {
                curentMin = currentMinute;
            }
            if (previousMinute != currentMinute && (curentMin != currentMinute) || isAnimatedByForce) {
                isAnimatedByForce = false;
                previousMinute = currentMinute;
                curentMin = currentMinute;
                if (!isInAmbientMode()) {
                    isMarioAnimated = true;
                    currentMarioY = marioStartY;
                    currentBlockY = blockStartY;
                    marioDirection = JUMP_UP;
                }
            }


            if (!isInAmbientMode()) {
                if (isMarioAnimated) {
                    if (currentMarioY > maxMarioY && (marioDirection == 0 || marioDirection == JUMP_UP)) {
                        setMarioYUp(step_animation);
                    } else if (marioDirection == JUMP_UP) {
                        marioDirection = JUMP_DOWN;
                    } else if (marioDirection == JUMP_DOWN && currentMarioY < marioStartY) {
                        setMarioYDown(step_animation);
                    } else if (marioDirection == JUMP_DOWN && currentMarioY >= marioStartY) {
                        isMarioAnimated = false;
                        marioDirection = MarioWatchFaceService.JUMP_NO;
                        currentMarioY = marioStartY;
                    }
                    if (currentMarioY <= currentBlockY + ((float) blockScaledBitmap.getHeight()) && blockDirection == 0) {
                        blockDirection = JUMP_UP;
                        isBlockAnimated = true;
                    }
                }

                if (isBlockAnimated) {

                    if (blockDirection == JUMP_UP && currentBlockY > blockStartY - ((float) blockScaledBitmap.getHeight())) {
                        setBlockYUp(step_animation);
                        setNumberYUp(step_animation);
                    } else if (blockDirection == MarioWatchFaceService.JUMP_UP && currentBlockY <= ((float) blockScaledBitmap.getHeight())) {
                        blockDirection = MarioWatchFaceService.JUMP_DOWN;
                    } else if (blockDirection == MarioWatchFaceService.JUMP_DOWN && currentBlockY < blockStartY) {
                        setBlockYDown(step_animation);
                        setNumberYDown(step_animation);
                    } else if (blockDirection == MarioWatchFaceService.JUMP_DOWN && currentBlockY >= blockStartY) {
                        currentBlockY = blockStartY;
                        blockDirection = MarioWatchFaceService.JUMP_NO;
                        isBlockAnimated = false;
                        currentNumberY =numberStartY;
                        curentHour = currentHour;
                    }


                } else {
                    addMarioStep(currentCalendar.get(Calendar.SECOND));
                }
            }


            if (isInAmbientMode()) {
                canvas.drawBitmap(blockScaledBitmap_amb, blockStartX, blockStartY, null);
                canvas.drawBitmap(blockScaledBitmap_amb, blockStartX + ((float) blockScaledBitmap.getWidth()) + 5, blockStartY, null);
                canvas.drawBitmap(seconds_amb_bitmap, blockStartX + ((float) 3 * blockScaledBitmap_amb.getWidth() / 2.0f), blockStartY + blockScaledBitmap_amb.getHeight(), null);


            } else {
                if (curentHour == -1) {
                    curentHour = currentHour;
                }
                if (curentHour == currentHour) {
                    canvas.drawBitmap(blockScaledBitmap, blockStartX, blockStartY, null);
                } else {

                    canvas.drawBitmap(blockScaledBitmap, blockStartX, currentBlockY, null);
                }
                canvas.drawBitmap(blockScaledBitmap, blockStartX + ((float) blockScaledBitmap.getWidth() + 5), currentBlockY, null);
                canvas.drawBitmap(seconds_bitmap, blockStartX + ((float) 3 * blockScaledBitmap.getWidth() / 2.0f), blockStartY + blockScaledBitmap.getHeight(), null);
                Paint p = new Paint();
                p.setTextSize(seconds_bitmap.getWidth() / 3.0f);
                canvas.drawText(Integer.toString(currentCalendar.getTime().getSeconds()), blockStartX + ((float) 3 * blockScaledBitmap.getWidth() / 2.0f + 2 * seconds_bitmap.getWidth() / 6), blockStartY + blockScaledBitmap.getHeight() + 6 * seconds_bitmap.getHeight() / 7, p);

            }
            if (isInAmbientMode()) {
                canvas.drawBitmap(marioScaled_amb_bitmap, marioStartX, marioStartY, null);
                mainPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
            } else {
                if (isMarioAnimated) {
                    canvas.drawBitmap(marioJumpScaled_bitmap, marioStartX, currentMarioY, null);
                } else {
                    canvas.drawBitmap(marioScaled_bitmap, marioStartX, currentMarioY, null);
                }
                mainPaint.setColor(-1);
            }
            if (isInAmbientMode()) {
                tempMinute = currentMinute;
                tempHour = currentHour;
            } else {
                tempMinute = curentMin;
                tempHour = currentHour;
            }
            if (tempHour < 10) {
                timeTextOneByOne = 0;

            } else {
                timeTextOneByOne = tempHour / 10;
            }

            Bitmap num1Bitmap = getTimeBitmap(timeTextOneByOne);

            numberHourX1 = blockStartX + blockScaledBitmap.getWidth() / 10;

            if (curentHour == currentHour) {
                canvas.drawBitmap(num1Bitmap, numberHourX1, numberStartY, null);


            } else {
                canvas.drawBitmap(num1Bitmap, numberHourX1, isInAmbientMode() ? numberStartY : currentNumberY, null);
            }
            if (tempHour < 10) {
                timeTextOneByOne = tempHour;
            } else {
                timeTextOneByOne = tempHour - ((tempHour / 10) * 10);
            }

            numberHourX2 = numberHourX1 + num1Bitmap.getWidth();
            Bitmap num2Bitmap = getTimeBitmap(timeTextOneByOne);

            if (curentHour == currentHour) {
                canvas.drawBitmap(num2Bitmap, numberHourX2, numberStartY, null);

            } else {
                canvas.drawBitmap(num2Bitmap, numberHourX2, isInAmbientMode() ? numberStartY : currentNumberY, null);
            }

            if(!isInAmbientMode()&&isDateEnable) {
                Calendar c = Calendar.getInstance();
Paint paint= new Paint();
                paint.setColor(getResources().getColor(R.color.green));

                String formattedDate =  c.get(Calendar.DAY_OF_MONTH)+"/"+ c.get(Calendar.MONTH)+"/"+ Integer.toString(c.get(Calendar.YEAR)).substring(Integer.toString(c.get(Calendar.YEAR)).length()-2);

                paint.setTextSize(20);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                canvas.drawText(formattedDate,  5,height/2, paint);
            }


            if (tempMinute < 10) {
                timeTextOneByOne = 0;
            } else {
                timeTextOneByOne = tempMinute / 10;
            }
            Bitmap num3Bitmap = getTimeBitmap(timeTextOneByOne);

            numberMinuteX1 = blockStartX + blockScaledBitmap.getWidth() + 5 + blockScaledBitmap.getWidth() / 10;

            canvas.drawBitmap(num3Bitmap, numberMinuteX1, isInAmbientMode() ? numberStartY : currentNumberY, null);
            if (tempMinute < 10) {
                timeTextOneByOne = tempMinute;
            } else {
                timeTextOneByOne = tempMinute - ((tempMinute / 10) * 10);
            }
            numberMinuteX2 = numberMinuteX1 + num3Bitmap.getWidth();
            canvas.drawBitmap(getTimeBitmap(timeTextOneByOne), numberMinuteX2, isInAmbientMode() ? numberStartY : currentNumberY, null);
            if(!is24HourType){
                Paint paint=new Paint();
                paint.setColor(getResources().getColor(R.color.white));
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                paint.setTextSize(20);
                canvas.drawText(hourExtra, (int)(width/2+3*blockScaledBitmap.getWidth()/7.0), isInAmbientMode() ? (numberStartY+num3Bitmap .getHeight()+22) : (currentNumberY +num3Bitmap .getHeight()+22)
                       , paint);

            }
            canvas.drawRect(cardBounds, mainPaint);
        }

        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            resetPositions();
            invalidate();
            updateTimer();
        }

        public void resetPositions() {
            isMarioAnimated = false;
            currentMarioY = marioStartY;
            currentBlockY = blockStartY;
            marioDirection = JUMP_NO;
        }

        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mainScale = ((float) width) / ((float) backgroundBitmap.getWidth());


            backgroundScaledBitmap = getScaledBitmap(backgroundBitmap);
            mBackgroundScaledBitmap_amb = getScaledBitmap(backgroundBitmap_amb);
            marioScaled_amb_bitmap = getScaledBitmap(mario_amb_bitmap);
            marioScaled_bitmap = getScaledBitmap(mario_bitmap);
            marioJumpScaled_bitmap = getScaledBitmap(marioJump_bitmap);
            marioStartX = (float) ((width - marioScaled_bitmap.getWidth()) / JUMP_DOWN);
            marioStartY = (((float) backgroundScaledBitmap.getHeight()) - (BASELINE_H * mainScale)) - ((float) marioScaled_bitmap.getHeight());

            currentMarioY = marioStartY;
            blockScaledBitmap_amb = getScaledBitmap(blockBitmap_amb);
            blockScaledBitmap = getScaledBitmap(blockBitmap);
            blockStartY = MarioWatchFaceService.BLOCK_Y * mainScale;
            blockStartX = (float) ((width / MarioWatchFaceService.JUMP_DOWN) - blockScaledBitmap.getWidth());

            currentBlockY = blockStartY;

            scaledZero_amb_bitmap = getScaledBitmap(zero_amb_bitmap);
            scaledOne_amb_bitmap = getScaledBitmap(one_amb_bitmap);
            scaledTwo_amb_bitmap = getScaledBitmap(two_amb_bitmap);
            scaledThree_amb_bitmap = getScaledBitmap(three_amb_bitmap);
            scaledFour_amb_bitmap = getScaledBitmap(four_amb_bitmap);
            mScaledFive_amb_bitmap = getScaledBitmap(five_amb_bitmap);
            scaledSix_amb_bitmap = getScaledBitmap(six_amb_bitmap);
            scaledSeven_amb_bitmap = getScaledBitmap(seven_amb_bitmap);
            scaledEight_amb_bitmap = getScaledBitmap(eight_amb_bitmap);
            scaledNine_amb_bitmap = getScaledBitmap(nine_amb_bitmap);


            scaledZero_bitmap = getScaledBitmap(zero_bitmap);
            scaledOne_bitmap = getScaledBitmap(one_bitmap);
            scaledTwo_bitmap = getScaledBitmap(two_bitmap);
            scaledThree_bitmap = getScaledBitmap(three_bitmap);
            scaledFour_bitmap = getScaledBitmap(four_bitmap);
            scaledFive_bitmap = getScaledBitmap(five_bitmap);
            scaledSix_bitmap = getScaledBitmap(six_bitmap);
            scaledSeven_bitmap = getScaledBitmap(seven_bitmap);
            scaledEight_bitmap = getScaledBitmap(eight_bitmap);
            scaledNine_bitmap = getScaledBitmap(nine_bitmap);

            step_animation = MarioWatchFaceService.ANIM_STEP * mainScale;
            currentNumberY = MarioWatchFaceService.ORIGINAL_NUMBER_Y * mainScale;
            numberStartY = currentNumberY;
            MARIO_MAX_Y = numberStartY + blockScaledBitmap.getHeight();
            maxMarioY = MarioWatchFaceService.MARIO_MAX_Y * mainScale;
            numberHourX1 = MarioWatchFaceService.ORIGINAL_NUMBER_X1 * mainScale;
            numberHourX2 = MarioWatchFaceService.ORIGINAL_NUMBER_X2 * mainScale;
            numberMinuteX1 = MarioWatchFaceService.ORIGINAL_NUMBER_X3 * mainScale;
            numberMinuteX2 = MarioWatchFaceService.ORIGINAL_NUMBER_X4 * mainScale;
            marioDirection = MarioWatchFaceService.JUMP_NO;
            blockDirection = MarioWatchFaceService.JUMP_NO;
            super.onSurfaceChanged(holder, format, width, height);
        }

        private Bitmap getScaledBitmap(Bitmap bitmap) {
            return Bitmap.createScaledBitmap(bitmap, (int) (((float) bitmap.getWidth()) * mainScale), (int) (((float) bitmap.getHeight()) * mainScale), true);
        }

        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            isBIProtection = properties.getBoolean(WatchFaceService.PROPERTY_BURN_IN_PROTECTION, false);
        }

        public Bitmap getTimeBitmap(int number) {
            switch (number) {
                case 0:
                    return isInAmbientMode() ? scaledZero_amb_bitmap : scaledZero_bitmap;
                case 1:
                    return isInAmbientMode() ? scaledOne_amb_bitmap : scaledOne_bitmap;
                case 2:
                    return isInAmbientMode() ? scaledTwo_amb_bitmap : scaledTwo_bitmap;
                case 3:
                    return isInAmbientMode() ? scaledThree_amb_bitmap : scaledThree_bitmap;
                case 4:
                    return isInAmbientMode() ? scaledFour_amb_bitmap : scaledFour_bitmap;
                case 5:
                    return isInAmbientMode() ? mScaledFive_amb_bitmap : scaledFive_bitmap;
                case 6:
                    return isInAmbientMode() ? scaledSix_amb_bitmap : scaledSix_bitmap;
                case 7:
                    return isInAmbientMode() ? scaledSeven_amb_bitmap : scaledSeven_bitmap;
                case 8:
                    return isInAmbientMode() ? scaledEight_amb_bitmap : scaledEight_bitmap;
                case 9:
                    return isInAmbientMode() ? scaledNine_amb_bitmap : scaledNine_bitmap;
                default:
                    if (isInAmbientMode())
                        return scaledZero_amb_bitmap;
                    else
                        return scaledZero_bitmap;
            }
        }

        public void onPeekCardPositionUpdate(Rect bounds) {
            super.onPeekCardPositionUpdate(bounds);
            if (!bounds.equals(cardBounds)) {
                cardBounds.set(bounds);
                invalidate();
            }
        }


        long touchTime=0;
        final long maxTouchTime=150;
        public void onTapCommand(@TapType int tapType, int x, int y, long eventTime) {


            if (tapType == TAP_TYPE_TOUCH) {
                touchTime=eventTime;
                Log.e("TAP_TYPE_TOUCH","TAP_TYPE_TOUCH");
            }
            if (tapType == TAP_TYPE_TAP) {
                touchTime=eventTime-touchTime;
                if (touchTime<maxTouchTime){
                    if(isChangingAnimationByTouch) {
                        changeMarioChapter();
                    }else if(isChangingBackgoundByTouch){
                        changeMarioBackground();
                    }
                }
                Log.e("TAP_TYPE_TAP",""+touchTime);

                touchTime=0;
            }

//            switch (tapType) {
//                case MarioWatchFaceService.JUMP_NO:
//
//                    changeMarioChapter();
//                default:
//                    super.onTapCommand(tapType, x, y, eventTime);
//            }
        }


        private void releaseGoogleClient() {
            if (googleClient != null && googleClient.isConnected()) {
                googleClient.disconnect();
            }
        }


        public void onDestroy() {
            releaseGoogleClient();
            super.onDestroy();
        }

        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);
        }
    }

    public MarioWatchFaceService() {
        cardBounds = new Rect();
        mainPaint = new Paint();
        isMarioAnimated = false;
        isBlockAnimated = false;
        isAnimatedByForce = false;
        mainScale = CardFrame.NO_EXPANSION;
    }

    private void setMarioYDown(float step) {
        float f = currentMarioY + step;
        currentMarioY = f;
    }


    private void setMarioYUp(float step) {
        float f = currentMarioY - step;
        currentMarioY = f;
    }

    private void setBlockYDown(float step) {
        float f = currentBlockY + step;
        currentBlockY = f;
    }


    private void setBlockYUp(float step) {
        float f = currentBlockY - step;
        currentBlockY = f;
    }

    private void setNumberYDown(float step) {
        float f = currentNumberY + step;
        currentNumberY = f;
    }

    private void setNumberYUp(float step) {
        float f = currentNumberY - step;
        currentNumberY = f;
    }

    public Engine onCreateEngine() {
        return new Engine();
    }
}

