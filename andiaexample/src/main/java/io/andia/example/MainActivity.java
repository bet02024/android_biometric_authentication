package io.andia.example;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.andia.example.ResultOnboarding;
import io.andia.example.SelfieValidator;
import io.andia.example.SelfieCallback;
import io.andia.example.ResultValidation;


public class MainActivity extends AppCompatActivity implements SelfieCallback {

    private Button selfieButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isOnBoarded = preferences.getBoolean("ONBOARDED", false);


        selfieButton = (Button) findViewById(R.id.buttonOnboard);
        if (isOnBoarded){
            selfieButton.setText("Validate example");
        }
        selfieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String API_KEY = "YOUR_API_KEY";
                boolean isOnBoarded = preferences.getBoolean("ONBOARDED", false);
                String userId = preferences.getString("USERID", "");
                if (isOnBoarded){

                    SelfieValidator validator = new SelfieValidator();
                    validator.onboarding("API_KEY", MainActivity.this);
                } else {
                    SelfieValidator validator = new SelfieValidator();
                    validator.validation(API_KEY, userId, MainActivity.this);
                }
            }
        });
    }


    public void resultOnBoarding(ResultOnboarding result){
        Log.d("ANDIA", result.getMessage());
        Toast toast = Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT);
        toast.show();
        if (result.getSuccess()){
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("ONBOARDED", true);
            editor.putString("USERID", result.getUserId());
            selfieButton.setText("Validate example");
            editor.commit();
        }  else {
            Log.e("ANDIA", result.getMessage());
        }

    }

    public void resultValidation(ResultValidation result){
        Log.d("ANDIA", result.getMessage());
        Toast toast = Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT);
        toast.show();
    }

}
