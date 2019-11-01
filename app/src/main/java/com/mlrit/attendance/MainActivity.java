package com.mlrit.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText email, password;

    Button btnLogin;
    final String url_Login = "http://192.168.2.146:801/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        email = findViewById(R.id.et_user_name);
        password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_submit);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();

                new LoginUser().execute(Email, Password);
            }
        });


    }


    public class LoginUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String Email = strings[0];
            String Password = strings[1];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("username", Email)
                    .add("password", Password)
                    .build();

            Request request = new Request.Builder()
                    .url(url_Login)
                    .post(formBody)
                    .build();

            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (result.equalsIgnoreCase("admin")) {
                        showToast("Ahoy");
                        Intent i = new Intent(MainActivity.this,
                                AdminActivity.class);
                        startActivity(i);
                        finish();
                    } else if(result.equalsIgnoreCase("Student")) {
                        Intent i = new Intent(MainActivity.this,
                                ViewStudent.class);
                        i.putExtra("student", email.getText().toString());
                        startActivity(i);
                        showToast("Student");
                    }
                    else if(result.equalsIgnoreCase("faculty")){
                        showToast("Faculty");
                        Intent i = new Intent(MainActivity.this,
                                AddAttendance.class);
                        startActivity(i);
                        finish();
                    }
                }else{
                    showToast("Username or Password incorrect");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}

