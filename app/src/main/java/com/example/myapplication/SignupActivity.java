package com.example.myapplication;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class SignupActivity extends AppCompatActivity {

    private EditText email;
    private EditText name;
    private EditText password;
    private Button signup;
    private String splash_background;

    // firebase를 이용할 떄 항상 중요한 것은 bradle에 dependency되는 api들의 version이다.
    // 서로 version이 맞는 지 확인하자!! error가 난다면 이곳에서 났을 확률이 99%이다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // splash_background에 res->values->string에 있는 id 값을 넣어준다.
        // id에는 splash_background라는 string이 들어있는데 이것은 firebase의 remoteconfig에서 변경가능한 값(색상)이다.
        splash_background = mFirebaseRemoteConfig.getString(getString((R.string.rc_color)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        // 밑의 코드는 롤리팝부터 적용 가능하다. 그 이하면 추가로 코드 써 주어야함.
        // 아이디 패스워드 밑에 있는 bar의 색을 적용시켜준다.
        getWindow().setStatusBarColor(Color.parseColor(splash_background));


        // 회원가입을 하기 위해서는 firebase 권한 연동이 되어야 한다. 때문에
        // tool -> firebase -> authentication -> 2) add firebase authentication to your app을 해준다.
        // 권한을 얻는 데에 필요한 firebase api가 추가된다.

        // 회원가입에 필요한 email, name, password와 signup 버튼을 불러온다.
        email = (EditText) findViewById(R.id.signupActivity_edittext_email);
        name = (EditText) findViewById(R.id.signupActivity_edittext_name);
        password = (EditText) findViewById(R.id.signupActivity_edittext_password);
        signup = (Button) findViewById(R.id.signupActivity_button_signup);
        signup.setBackgroundColor(Color.parseColor(splash_background));

        // signup button을 누르면 작성된 이메일, 비밀번호로 회원가입이 된다.
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // text가 비워져 있는 란이 있다면 return한다. 회원가입 xx
                // password가 6자리 미만일 때에는 에러가 난다.
                if (email.getText().toString() == null || name.getText().toString() == null
                        || password.getText().toString() == null){
                    return; }

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override

                            // 회원가입이 완료되면 => firebase에 권한이 올라가고 나서 complete으로 넘어온다.
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                // 새로운 유저모델을 만들어 사용자의 uid으로 ui를 저장한다.
                                UserModel userModel = new UserModel();
                                userModel.userName = name.getText().toString();

                                String uid = task.getResult().getUser().getUid();
                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);
                            }
                        });
            }
        });
    }
}
