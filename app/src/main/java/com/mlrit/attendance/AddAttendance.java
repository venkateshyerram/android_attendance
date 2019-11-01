package com.mlrit.attendance;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddAttendance extends AppCompatActivity{
    final String get_students = "http://192.168.2.146:801/students";
    final String set_attendance = "http://192.168.2.146:801/attendance";
    String[] h = null;
    String present= "";
    String year1 = null;
    String absent= "";
    String department = null;
    String class_ = null;
    String subject_ = null;

    ArrayList<String> AllCheckbox = new ArrayList<String>();
    ArrayList<CheckBox> Checkboxes = new ArrayList<CheckBox>();
    Button submit, getstudents;
    EditText class_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addattendace);
        final Spinner dropdown = findViewById(R.id.spinner_dept);
        String[] items = new String[]{"CSE", "IT"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);

        final Spinner year = findViewById(R.id.spinner_year);
        String[] years_items = new String[]{"I", "II", "III", "IV"};

        ArrayAdapter<String> year_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years_items);

        year.setAdapter(year_adapter);

        final Spinner subject = findViewById(R.id.spinner_subject);
        String[] subject_items = new String[]{"DSS", "HCI", "CIS"};

        ArrayAdapter<String> subject_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subject_items);

        subject.setAdapter(subject_adapter);
        class_text = findViewById(R.id.class_);

        getstudents = findViewById(R.id.get_students);
        getstudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(AddStudent.this,
//                        AdminActivity.class);
//                startActivity(i);
                department = dropdown.getSelectedItem().toString();
                year1 = year.getSelectedItem().toString();
                new GetStudents().execute(department, year1);
                class_ = class_text.getText().toString();
                subject_ = subject.getSelectedItem().toString();


               }});



        submit = findViewById(R.id.attendance);
        submit.setVisibility(LinearLayout.GONE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(AddStudent.this,
//                        AdminActivity.class);
//                startActivity(i);

                if(h != null){
                    StringBuilder builder = new StringBuilder();
                    StringBuilder absents = new StringBuilder();
                    for (int i = 0; i < Checkboxes.size(); i++) {
                        CheckBox check = Checkboxes.get(i);
                        if (check.isChecked()) {
//                            Toast.makeText(AddAttendance.this,
//                                   check.getText() , Toast.LENGTH_LONG).show();
                            builder.append(check.getText().toString());
                            builder.append("##");
                        }
                        else{
                            absents.append(check.getText().toString());
                            absents.append("##");
                        }
                    }
                    present = builder.toString();
                    new PostPresent().execute(present, absents.toString(), department, year1, class_, subject_);
//                    System.out.println(AllCheckbox.get(2));
            }}});


    }
    public void checkboxes(String[] a){
        LinearLayout my_layout = findViewById(R.id.fdgi);
        getstudents.setVisibility(LinearLayout.GONE);
        LinearLayout dept = findViewById(R.id.dept);
        dept.setVisibility(LinearLayout.GONE);
        LinearLayout year = findViewById(R.id.year);
        year.setVisibility(LinearLayout.GONE);
        submit.setVisibility(LinearLayout.VISIBLE);
        for (int i = 0; i < a.length; i++)
        {

            TableRow row =new TableRow(this);
            row.setId(i);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            CheckBox checkBox = new CheckBox(this);
//            checkBox.setOnCheckedChangeListener(this);
            checkBox.setId(i);
            checkBox.setText(a[i]);
            int color = ContextCompat.getColor(this, R.color.white);
            checkBox.setTextColor(color);
            checkBox.setTextColor(color);
            row.addView(checkBox);
            Checkboxes.add(checkBox);
            my_layout.addView(row);
        }


    }

    public class GetStudents extends AsyncTask<String, Void, String> {
        String[] results;
        String result;

        @Override
        protected String doInBackground(String... strings) {


            String year_selected = strings[1];

            String dept = strings[0];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
//                    .add("fullname", name)
//                    .add("contact", cont)
//                    .add("rollno", roll)
//                    .add("pass", pass)
                    .add("dept", dept)
                    .add("year", year_selected)
//                    .add("role", role_selected)
                    .build();

            Request request = new Request.Builder()
                    .url(get_students)
                    .post(formBody)
                    .build();
            System.out.println(request.url());
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    result = response.body().string();
//                    System.out.println(result.split("##")[0]);
//                    checkboxes(result.split("##"));
                    h = result.split("##");
                    System.out.println(result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checkboxes(h);
                        }
                    });

//                    if (result.equalsIgnoreCase("OK")) {
//                        showToast("Added successfully");
//                        Intent i = new Intent(AddAttendance.this,
//                                AdminActivity.class);
//                        startActivity(i);
//                        finish();
//                    }
                }else {
                    showToast("Couldn't find any students");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }

    public class PostPresent extends AsyncTask<String, Void, String> {

        String result;
        @Override
        protected String doInBackground(String... strings) {
            String presnties = strings[0];
            String absenties = strings[1];
            String department = strings[2];
            String class_ = strings[4];
            String subject = strings[5];

//            String cont = strings[1];
//            String roll = strings[2];
//            String pass = strings[3];
//            String year_selected = strings[0];
//            String role_selected = strings[5];
//            String dept = strings[1];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
//                    .add("fullname", name)
//                    .add("contact", cont)
                    .add("subject", subject)
                    .add("class", class_)
                    .add("present", presnties)
                    .add("absent", absenties)
                    .add("dept", department)
//                    .add("role", role_selected)
                    .build();

            Request request = new Request.Builder()
                    .url(set_attendance)
                    .post(formBody)
                    .build();
            System.out.println(request.url());
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    result = response.body().string();
//                    System.out.println(result.split("##")[0]);
//                    checkboxes(result.split("##"));


                    if (result.equalsIgnoreCase("OK")) {
                        showToast("Added successfully");
                        Intent i = new Intent(AddAttendance.this,
                                AddAttendance.class);
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
                Toast.makeText(AddAttendance.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}

