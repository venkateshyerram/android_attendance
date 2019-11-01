package com.mlrit.attendance;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddStudent extends AppCompatActivity {
    Button btn_submit, btn_cancel;
    EditText fullname, contact, rollno, password;
    final String url_Login = "http://192.168.2.146:801/register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudent);

        final Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"--None--", "CSE", "IT"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);

        final Spinner year = findViewById(R.id.spinner2);
        String[] years_items = new String[]{"--None--", "I", "II", "III", "IV"};

        ArrayAdapter<String> year_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years_items);

        year.setAdapter(year_adapter);

        final Spinner role = findViewById(R.id.role);
        String[] role_items = new String[]{"Student", "Faculty"};

        ArrayAdapter<String> role_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, role_items);

        role.setAdapter(role_adapter);

        fullname = findViewById(R.id.fullname);
        contact = findViewById(R.id.contact);
        rollno = findViewById(R.id.rollnumber);
        password = findViewById(R.id.pass);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddStudent.this,
                        AdminActivity.class);
                startActivity(i);

        }});

        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i = new Intent(AdminActivity.this,
//                        AddStudent.class);
//                startActivity(i);

                String name = fullname.getText().toString();
                String cont = contact.getText().toString();
                String roll = rollno.getText().toString();
                String pass = password.getText().toString();
                String year_selected = year.getSelectedItem().toString();
                String role_selected = role.getSelectedItem().toString();
                String dept = dropdown.getSelectedItem().toString();

//                System.out.println(year_selected);
                new Register().execute(name, cont, roll, pass, year_selected, role_selected, dept);

            }
        });


    }

    public class Register extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0];
            String cont = strings[1];
            String roll = strings[2];
            String pass = strings[3];
            String year_selected = strings[4];
            String role_selected = strings[5];
            String dept = strings[6];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("fullname", name)
                    .add("contact", cont)
                    .add("rollno", roll)
                    .add("pass", pass)
                    .add("dept", dept)
                    .add("year", year_selected)
                    .add("role", role_selected)
                    .build();

            Request request = new Request.Builder()
                    .url(url_Login)
                    .post(formBody)
                    .build();
            System.out.println(request.url());
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (result.equalsIgnoreCase("OK")) {
                        showToast("Added successfully");
                            Intent i = new Intent(AddStudent.this,
                                    AdminActivity.class);
                            startActivity(i);
                            finish();
                    } else {
                        showToast("Email or Password mismatched!");
                    }
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
                Toast.makeText(AddStudent.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}