import os
import random
import string
import mysql.connector
from flask import Flask, render_template, url_for, request, redirect, Response, session, flash, send_from_directory, \
    send_file, abort, jsonify
from flask_cors import CORS, cross_origin
from datetime import datetime

app = Flask(__name__)
CORS(app, resources={r"/login1": {"origins": "http://localhost/"}})

sql_conn = mysql.connector.connect(
    host="localhost",
    user="root",
    passwd="root",
    database="test"
)


@app.route('/login', methods=['GET', 'POST', 'OPTIONS'])
# @cross_origin(origin='localhost', headers=['Content- Type', 'application/json'])
def android_login():
    if request.method == "POST":
        username = str(request.form.get('username')).strip()
        password = str(request.form.get('password'))
        print(password)
        cursor = sql_conn.cursor()
        cursor.execute("select role from users where rollno = '%s' && password = '%s'" % (username, password))
        role = ""
        count = 0
        for row in cursor:
            role = row[0]
            count += 1

        if count == 1:
            print(role)
            return role, 200
        else:
            return "", 401


@app.route('/register', methods=['POST', 'OPTIONS'])
def register():
    if request.method == "POST":
        fullname = request.form.get('fullname')
        contact = request.form.get('contact')
        rollno = request.form.get('rollno')
        password = request.form.get('pass')
        department = request.form.get('dept')
        year = request.form.get('year')
        role = request.form.get('role')
        cursor = sql_conn.cursor()
        cursor.execute("insert into users(rollno, name, role, password, contact, year, department) "
                       "VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s')"
                       % (rollno, fullname, role, password, contact, year, department))
        sql_conn.commit()

        return "OK", 200


@app.route('/faculties', methods=['POST', 'OPTIONS'])
def get_faculties():
    if request.method == "POST":
        faculties = []
        cursor = sql_conn.cursor()
        cursor.execute("select rollno,name,contact from users where role='Faculty'")
        for row in cursor:
            faculties.append({"rollno": row[0], "name": row[1],
                             "contact": row[2]})
        print(faculties)
        return jsonify({"data": faculties}), 200


@app.route('/students', methods=['POST', 'OPTIONS'])
def get_students():
    if request.method == "POST":
        department = request.form.get('dept')
        year = request.form.get('year')
        print(department)
        print(year)
        students = []
        students_string = ""
        cursor = sql_conn.cursor()
        if not department == "all":
            cursor.execute("select rollno from users where department='%s' and year='%s'" % (department, year))
            for row in cursor:
                students.append(row[0])
                students_string += row[0]
                students_string += "##"
            print(students)
            if students_string == "":
                return "", 404
            return students_string, 200
        else:
            cursor.execute("select rollno,name,contact,department,year from users where role='Student'")
            for row in cursor:
                students.append({"rollno": row[0], "name": row[1],
                                 "contact": row[2], "department": row[3], "year": row[4]})
            print(students)
            return jsonify({"data": students}), 200


@app.route('/attendance', methods=['POST', 'OPTIONS'])
def set_attendance():
    if request.method == "POST":
        presents = request.form.get('present')
        absents = request.form.get('absent')
        dept = request.form.get('dept')
        clss = request.form.get('class')
        subject = request.form.get('subject')
        print(presents)
        print(absents)
        date = datetime.today().strftime("%Y-%m-%d")
        present_list = str(presents)[:-2].split("##")
        absent_list = str(absents)[:-2].split("##")
        print(clss)
        print(present_list)
        print(absent_list)
        for item in present_list:
            if not item == "":
                cursor = sql_conn.cursor()
                cursor.execute("insert into attendance(rollno, department, class, date, subject, status)"
                               " values('%s','%s','%s','%s','%s',%s)"
                               % (str(item), dept, clss, date, subject, 1))

        for item in absent_list:
            if not item == "":
                cursor = sql_conn.cursor()
                cursor.execute("insert into attendance(rollno, department, class, date, subject, status)"
                               " values('%s','%s',%s,'%s','%s','%s')"
                               % (str(item), dept, clss, date, subject, 0))
        sql_conn.commit()
        return "OK", 200


@app.route('/editattendance', methods=['POST', 'OPTIONS'])
def edit_attendance():
    if request.method == "POST":
        subject = request.form["subject"]
        rollno = request.form["rollno"]
        class_ = request.form["class"]
        date = request.form["date"]
        dept = request.form["dept"]
        year = request.form["year"]
        status = request.form["status"]
        print(subject)
        print(rollno)
        print(class_)
        print(date)
        print(dept)
        print(year)
        print(status)
        if status == "Present":
            status = 1
        else:
            status = 0
        cursor = sql_conn.cursor()
        cursor.execute("update attendance set status='%s' where rollno='%s' and department='%s' and class=%s and "
                       "date='%s' and subject='%s'" % (status, rollno, dept, class_, date, subject))
        sql_conn.commit()
        return "OK", 200


@app.route('/get_student_data', methods=['POST', 'OPTIONS'])
def get_student_data():
    if request.method == "POST":
        rollno = str(request.form.get('rollno')).strip()
        # print(rollno)
        subjects = []
        data = {}
        total_classes = 0
        total_present = 0
        cursor = sql_conn.cursor()
        cursor.execute("select name,contact,year,department from users where rollno='%s'" % rollno)
        for row in cursor:
            print(row[0])
            data["name"] = row[0]
            data["contact"] = row[1]
            data["year"] = row[2]
            data["department"] = row[3]
        print(data)
        cursor1 = sql_conn.cursor()
        cursor1.execute("select total.subject,total.tstatus,present.pstatus from "
                       "(select distinct subject,rollno,count(status) as tstatus "
                       "from attendance where rollno='%s' group by subject)as total "
                       "join"
                       " (select distinct subject,rollno,count(status)as pstatus "
                       "from attendance where rollno='%s' and status='1' group by subject) as present "
                       "on total.subject=present.subject" % (rollno, rollno))
        for row in cursor1:
            if row[0] not in subjects:
                subjects.append(row[0])
                total = int(row[1])
                present = int(row[2])
                average = present/total
                average *= 100
                total_classes += total
                total_present += present
                data[row[0]] = round(average, 2)
                print(average)

        total_average = total_present/total_classes
        total_average *= 100
        data["average"] = round(total_average, 2)
        data["rollno"] = rollno

        print(data)

        return jsonify({"data": data}), 200


if __name__ == '__main__':
    app.secret_key = 'random string'
    app.debug = True
    # app.run()
    app.run(host='192.168.2.146', port=801, threaded=True)
