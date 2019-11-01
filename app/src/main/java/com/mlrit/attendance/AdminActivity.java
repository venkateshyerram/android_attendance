package com.mlrit.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {
    Button add_student, btn_viewstudent, btn_viewfaculty, btn_attendance;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        add_student = findViewById(R.id.addstudent);

        add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this,
                        AddStudent.class);
                startActivity(i);
            }
        });

        btn_viewstudent = findViewById(R.id.viewstudent);
        btn_viewstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this,
                        GetAllStudents.class);
                startActivity(i);
            }
        });
        btn_viewfaculty = findViewById(R.id.viewfaculty);
        btn_viewfaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this,
                        GetAllFaculty.class);
                startActivity(i);
            }
        });
        btn_attendance = findViewById(R.id.attendancestudent);
        btn_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this,
                        AttendancePerStudent.class);
                startActivity(i);
            }
        });
    }

}
