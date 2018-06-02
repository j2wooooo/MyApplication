package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    // firebaseremoteconfig를 이용하여 splash image를 보여주기 위해 사용했다.
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view를 가져오기 위해 꼭 setcontentview를 해 주어야 한다!!! 없으면 null 참조한다고 오류남!!
        setContentView(R.layout.activity_splash);

        // 액티비티의 상태바를 없애준다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // splash화면을 나타낼 레이아웃을 만든다
        linearLayout = (LinearLayout) findViewById(R.id.splashactivity_linaerlayout);

        // firebase의 remote config 입력을 받는다.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        // xml파일을 만들어 default 값을 정해주었다.
        mFirebaseRemoteConfig.setDefaults(R.xml.default_config);

        // firebase remote config 입력을 받아와 출력해준다.
        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            mFirebaseRemoteConfig.activateFetched();
                        } else {

                        }
                        displayMessage();
                    }
                });

    }

    // remote message 출력
    void displayMessage() {

        // 출력되는 값은 xml 파일의 default_config와 firebase의 remote config에서 정해준 것들임
        // default_config.xml에서 key로 splash_background, splash_message_caps, splash_message를 정해
        // firebase의 매개 변수와 일치시켜 주었다.
        String splash_background = mFirebaseRemoteConfig.getString("splash_background");
        boolean caps = mFirebaseRemoteConfig.getBoolean("splash_message_caps");
        String splash_message = mFirebaseRemoteConfig.getString("splash_message");

        linearLayout.setBackgroundColor(Color.parseColor(splash_background));

        // splash_message_caps 가 true일 때 실행한다 => splash 화면을 보여준다
        if (caps) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(splash_message).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            //splash 화면 gogogo~~
            builder.create().show();

        }else
        {
            startActivity(new Intent(this, LoginActivity.class));
        }

    }


}