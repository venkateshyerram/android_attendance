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

public class AttendancePerStudent extends AppCompatActivity {
    final String set_attendance = "http://192.168.2.146:801/editattendance";

    String year1 = null;

    String department = null;

    String subject_ = null;


    Button submit;
    EditText class_text, roll_no, date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendanceperstudent);
        final Spinner dropdown = findViewById(R.id.spinner_dept1);
        String[] items = new String[]{"CSE", "IT"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);

        final Spinner year = findViewById(R.id.spinner_year1);
        String[] years_items = new String[]{"I", "II", "III", "IV"};

        ArrayAdapter<String> year_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years_items);

        year.setAdapter(year_adapter);

        final Spinner subject = findViewById(R.id.spinner_subject1);
        String[] subject_items = new String[]{"DSS", "HCI", "CIS"};

        ArrayAdapter<String> subject_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subject_items);

        subject.setAdapter(subject_adapter);

        final Spinner status = findViewById(R.id.spinner_attendance);
        String[] status_items = new String[]{"Present", "Absent"};

        ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, status_items);

        status.setAdapter(status_adapter);

        class_text = findViewById(R.id.class_);
        date = findViewById(R.id.date);
        roll_no = findViewById(R.id.roll_no);
//        getstudents = findViewById(R.id.get_students);
//        getstudents.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent i = new Intent(AddStudent.this,
////                        AdminActivity.class);
////                startActivity(i);
//                department = dropdown.getSelectedItem().toString();
//                year1 = year.getSelectedItem().toString();
//                new GetStudents().execute(department, year1);
//                class_ = class_text.getText().toString();
//                subject_ = subject.getSelectedItem().toString();
//
//
//            }});



        submit = findViewById(R.id.attendance1);
//        submit.setVisibility(LinearLayout.GONE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(AddStudent.this,
//                        AdminActivity.class);
//                startActivity(i);
                    String roll = roll_no.getText().toString();
                    String Class = class_text.getText().toString();
                    String date_ = date.getText().toString();
                    year1 = year.getSelectedItem().toString();
                    department = dropdown.getSelectedItem().toString();
                    subject_ = subject.getSelectedItem().toString();
                    String status_ = status.getSelectedItem().toString();

                    new PostPresent().execute(roll, Class, date_, department, year1, status_ , subject_);
//                    System.out.println(AllCheckbox.get(2));
                }});


    }
//    public void checkboxes(String[] a){
//        LinearLayout my_layout = findViewById(R.id.fdgi);
//        getstudents.setVisibility(LinearLayout.GONE);
//        LinearLayout dept = findViewById(R.id.dept);
//        dept.setVisibility(LinearLayout.GONE);
//        LinearLayout year = findViewById(R.id.year);
//        year.setVisibility(LinearLayout.GONE);
//        submit.setVisibility(LinearLayout.VISIBLE);
//        for (int i = 0; i < a.length; i++)
//        {
//
//            TableRow row =new TableRow(this);
//            row.setId(i);
//            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
//            CheckBox checkBox = new CheckBox(this);
////            checkBox.setOnCheckedChangeListener(this);
//            checkBox.setId(i);
//            checkBox.setText(a[i]);
//            int color = ContextCompat.getColor(this, R.color.white);
//            checkBox.setTextColor(color);
//            checkBox.setTextColor(color);
//            row.addView(checkBox);
//            Checkboxes.add(checkBox);
//            my_layout.addView(row);
//        }
//
//
//    }



    public class PostPresent extends AsyncTask<String, Void, String> {

        String result;
        @Override
        protected String doInBackground(String... strings) {
           String roll = strings[0];
           String Class = strings[1];
           String date = strings[2];
           String dept = strings[3];
           String year = strings[4];
           String status = strings[5];
           String subject = strings[6];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("subject", subject)
                    .add("class", Class)
                    .add("rollno", roll)
                    .add("date", date)
                    .add("dept", dept)
                    .add("year", year)
                    .add("status", status)
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
                        Intent i = new Intent(AttendancePerStudent.this,
                                AdminActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        showToast("Something went wrong");
                    }
                } else {
                    showToast("Something went wrong");
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
                Toast.makeText(AttendancePerStudent.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}

