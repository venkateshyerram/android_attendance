package com.mlrit.attendance;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.mlrit.attendance.Student;

public class ViewStudent extends AppCompatActivity {
    final String get_students = "http://192.168.2.146:801/get_student_data";
    Student student = new Student();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewstudent);
        System.out.println(getIntent().getStringExtra("student"));
        String rollno = getIntent().getStringExtra("student");
        new GetStudents().execute(rollno);
    }

    public void showdetails(){

//        TableLayout layout = findViewById(R.id.view_student);
//        TableRow tableRow = new TableRow(this);
//        tableRow.setOrientation(LinearLayout.HORIZONTAL);
//        TextView textView = new TextView(this);
//        textView.setText(student.name);
//        textView.setTextSize(30);
//        tableRow.addView(textView);
//        layout.addView(tableRow);
        TextView name = findViewById(R.id.name);
                name.setText(student.name);
        TextView contact = findViewById(R.id.contact);
        contact.setText(student.contact);
        TextView average = findViewById(R.id.average);
        average.setText(student.average);
        TextView cis = findViewById(R.id.cis);
        cis.setText(student.CIS);
        TextView hci = findViewById(R.id.hci);
        hci.setText(student.HCI);
        TextView rollno = findViewById(R.id.rollno);
        rollno.setText(student.rollNo);
        TextView year = findViewById(R.id.year);
        year.setText(student.year);
        TextView dss = findViewById(R.id.dss);
        dss.setText(student.DSS);
        TextView dept = findViewById(R.id.dept);
        dept.setText(student.department);

    }
    public class GetStudents extends AsyncTask<String, Void, String> {
        String result;
        @Override
        protected String doInBackground(String... strings) {


            String rollno = strings[0];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
//                    .add("fullname", name)
//                    .add("contact", cont)
                    .add("rollno", rollno)
//                    .add("pass", pass)
//                    .add("dept", dept)
//                    .add("year", year_selected)
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
                    JSONObject Jobject = new JSONObject(result);
                    System.out.println(Jobject.getJSONObject("data").get("name"));
                        student.name = Jobject.getJSONObject("data").get("name").toString();
                        student.average = Jobject.getJSONObject("data").get("average").toString() + "%";
                        student.rollNo = Jobject.getJSONObject("data").get("rollno").toString();
                        student.contact = Jobject.getJSONObject("data").get("contact").toString();
                        student.CIS = Jobject.getJSONObject("data").get("CIS").toString() + "%";
                        student.DSS = Jobject.getJSONObject("data").get("DSS").toString() + "%";
                        student.HCI = Jobject.getJSONObject("data").get("HCI").toString() + "%";
                        student.year = Jobject.getJSONObject("data").get("year").toString();
                        student.department = Jobject.getJSONObject("data").get("department").toString();


//                    System.out.println(result.split("##")[0]);
//                    checkboxes(result.split("##"));
//                    h = result.split("##");
//                    System.out.println(result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showdetails();
                        }
                    });

//                    if (result.equalsIgnoreCase("OK")) {
//                        showToast("Added successfully");
//                        Intent i = new Intent(ViewStudent.this,
//                                AdminActivity.class);
//                        startActivity(i);
//                        finish();
//                    } else {
//                        showToast("Email or Password mismatched!");
//                    }
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
                Toast.makeText(ViewStudent.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
