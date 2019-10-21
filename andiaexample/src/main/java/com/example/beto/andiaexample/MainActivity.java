package com.example.beto.andiaexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import io.andia.ResultOnboarding;
import io.andia.SelfieValidator;
import io.andia.SelfieCallback;
import io.andia.ResultValidation;

public class MainActivity extends AppCompatActivity implements SelfieCallback {

    private Button onboardingButton;
    private Button validateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        onboardingButton = (Button) findViewById(R.id.buttonOnboard);
        validateButton = (Button) findViewById(R.id.buttonValidate);


        onboardingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String API_KEY = "API_KEY";
                SelfieValidator validator = new SelfieValidator();
                validator.onboarding("API_KEY", MainActivity.this);
             }
        });

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String API_KEY = "API_KEY";
                SelfieValidator validator = new SelfieValidator();
                String userId = preferences.getString("USERID", "");
                Boolean onboarded = preferences.getBoolean("ONBOARDED", false);
                if (!onboarded){
                    Log.d("ANDIA", "USER IS NOT REGISTRED YET");
                    return;
                }

                validator.validation(API_KEY, userId, MainActivity.this);
            }
        });
    }


    //Callback for OnBoarding.
    @Override
    public void resultOnBoarding(ResultOnboarding result) {
        Log.d("ANDIA", result.getMessage());
        if (result.getSuccess()){
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("ONBOARDED", true);
            editor.putString("USERID", result.getUserId());
            editor.commit();
        }
    }

    //Callback for Validation.
    @Override
    public void resultValidation(ResultValidation result){
        Log.d("ANDIA", result.getMessage());
    }

}
