package com.mlrit.attendance;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetAllFaculty extends AppCompatActivity {
    final String get_students = "http://192.168.2.146:801/faculties";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getallfaculty);
        new Students().execute("hii");
    }
    private void showdetails(JSONObject jsonObject) {
        TableLayout tableLayout = findViewById(R.id.faculty);

        try {
            JSONArray jsondata = jsonObject.getJSONArray("data");
            for(int i = 0; i < jsondata.length(); i++){
                JSONObject object = jsondata.getJSONObject(i);
                TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                tableRowParams.setMargins(1,1,1,1);
                System.out.println(object.get("name").toString());
                TableRow tableRow = new TableRow(this);
                tableRow.setOrientation(LinearLayout.HORIZONTAL);
                TextView textView = new TextView(this);
                textView.setText(object.get("name").toString());
                textView.setTextSize(18);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setGravity(1);
                textView.setLayoutParams(tableRowParams);
                tableRow.addView(textView);


                TextView textView_roll = new TextView(this);
                textView_roll.setText(object.get("rollno").toString());
                textView_roll.setTextSize(18);
                textView_roll.setTextColor(getResources().getColor(R.color.white));
                textView_roll.setGravity(1);
                textView_roll.setLayoutParams(tableRowParams);
                tableRow.addView(textView_roll);

                TextView textView_dept = new TextView(this);
                textView_dept.setText(object.get("contact").toString());
                textView_dept.setTextSize(18);
                textView_dept.setTextColor(getResources().getColor(R.color.white));
                textView_dept.setGravity(1);
                textView_dept.setLayoutParams(tableRowParams);
                tableRow.addView(textView_dept);


                tableRow.setBackgroundResource(R.drawable.row_border);
                tableLayout.addView(tableRow);
            }


        }
        catch (JSONException j){
            Toast.makeText(GetAllFaculty.this,
                    "NO data", Toast.LENGTH_LONG).show();
        }

    }
    public class Students extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected String doInBackground(String... strings) {




            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
//                    .add("fullname", name)
//                    .add("contact", cont)
//                    .add("rollno", rollno)
//                    .add("pass", pass)
                    .add("dept", "all")
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
                    System.out.println(result);
                    final JSONObject Jobject = new JSONObject(result);

//                    System.out.println(Jobject.getJSONObject("data").get("name"));
//                    student.name = Jobject.getJSONObject("data").get("name").toString();
//                    student.average = Jobject.getJSONObject("data").get("average").toString() + "%";
//                    student.rollNo = Jobject.getJSONObject("data").get("rollno").toString();
//                    student.contact = Jobject.getJSONObject("data").get("contact").toString();
//                    student.CIS = Jobject.getJSONObject("data").get("CIS").toString() + "%";
//                    student.DSS = Jobject.getJSONObject("data").get("DSS").toString() + "%";
//                    student.HCI = Jobject.getJSONObject("data").get("HCI").toString() + "%";
//                    student.year = Jobject.getJSONObject("data").get("year").toString();
//                    student.department = Jobject.getJSONObject("data").get("department").toString();


//                    System.out.println(result.split("##")[0]);
//                    checkboxes(result.split("##"));
//                    h = result.split("##");
//                    System.out.println(result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showdetails(Jobject);
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


}


