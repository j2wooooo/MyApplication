package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private Button signup;

    // 원격으로 테마를 받기 위해서 필요하다.
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // splash때와 마찬가지로 remoteconfig와 연동되는 변수를 받아온다.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // splash_background에 res->values->string에 있는 id 값을 넣어준다.
        // id에는 splash_background라는 string이 들어있는데 이것은 firebase의 remoteconfig에서 변경가능한 값(색상)이다.
        String splash_background = mFirebaseRemoteConfig.getString(getString((R.string.rc_color)));

        // 밑의 코드는 롤리팝부터 적용 가능하다. 그 이하면 추가로 코드 써 주어야함.
        // 아이디 패스워드 밑에 있는 bar의 색을 적용시켜준다.
        getWindow().setStatusBarColor(Color.parseColor(splash_background));

        // id에 해당하는 버튼을 불러온다.
        login = (Button)findViewById(R.id.loginActivity_button_login);
        signup = (Button)findViewById(R.id.loginActivity_button_signup);

        // 해당하는 버튼의 색깔을 적용시킨다.
        login.setBackgroundColor(Color.parseColor(splash_background));
        signup.setBackgroundColor(Color.parseColor(splash_background));

        //signup button을 click했을 때 signupActivity가 호출된다.
        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });
    }
}
