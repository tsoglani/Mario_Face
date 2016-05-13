package mario.nikos.tsoglani.wface;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Settings extends Activity {

    CheckBox evolution_checkBox, animation_checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        evolution_checkBox = (CheckBox) findViewById(R.id.evolution_checkBox);
        animation_checkBox = (CheckBox) findViewById(R.id.animation_checkBox);

        evolution_checkBox.setChecked(MarioWatchFaceService.hasEvolution);
        animation_checkBox.setChecked(MarioWatchFaceService.isAnimateMode);
        animation_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MarioWatchFaceService.storeSharePref(getApplicationContext(), MarioWatchFaceService.animationSharedPrefString, isChecked);
            }
        });


        evolution_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MarioWatchFaceService.storeSharePref(getApplicationContext(), MarioWatchFaceService.evolutionSharedPrefString, isChecked);
            }
        });

    }
}
