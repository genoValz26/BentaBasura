package com.android.bentabasura.benta_basura.Page_Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Pages.Login;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.Utils.AppIntroSlider;
import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by HP on 10/23/2016.
 */
public class MyIntroPageAdapter extends AppIntro {
    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        //adding the three slides for introduction app you can ad as many you needed
        addSlide(AppIntroSlider.newInstance(R.layout.app_intro_first));
        addSlide(AppIntroSlider.newInstance(R.layout.app_intro_second));
        addSlide(AppIntroSlider.newInstance(R.layout.app_intro_third));

        // Show and Hide Skip and Done buttons
        showStatusBar(false);
        showSkipButton(false);

        // Turn vibration on and set intensity
        // You will need to add VIBRATE permission in Manifest file
        setVibrate(true);
        setVibrateIntensity(30);

        //Add animation to the intro slider
        setDepthAnimation();
    }

    @Override
    public void onSkipPressed() {
        // Do something here when users click or tap on Skip button.
        Toast.makeText(getApplicationContext(),
                getString(R.string.app_intro_skip), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }

    @Override
    public void onNextPressed() {
        // Do something here when users click or tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something here when users click or tap tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something here when slide is changed
    }
}