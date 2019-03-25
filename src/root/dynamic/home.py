#!/bin/python3
import os
import sys
name = {
    0: "Aldoreh, Adam",
    1: "Chopra, Vivek",
    2: "Dao, Joseph",
    3: "Dhillon, Tajinder",
    4: "Easterbrook, Zach",
    5: "Falodun, Tomisin",
    6: "Lu, Yicheng",
    7: "Lucier, Brandon",
    8: "Mallem, Ali",
    9: "Melanson, Mike",
    10: "Singh, Manpreet",
    11: "Wang, Zheng",
    12: "Zang, Xiaobang",
    13: "Zeidler, Matthew"
}
a = input()
with open("students/grades.txt") as f:
    grades = f.readlines()
grades = [x.strip() for x in grades]
sid = -1

for line in sys.stdin.readlines():
    if ("Cookie:" in line) or ("cookie:" in line):
        L = line.split(":")
        for s in L[1].split(";"):
            if "id" in s:
                sid = int(s.split("=")[1].strip())
        break

if 0 <= sid and sid <= 13:
    body = ('<!DOCTYPE html>\
<html>\
<body>\
    <h1>Welcome {0} to the University of Windsor portal!</h1>\
    <img src="uwin_logo.jpg" alt="UWin Logo">\
        <br>\
        <a href="computer_science.html">School of Computer Science</a>\
        <br>\
        <a href="instructor.out">see grades</a>\
        <br>\
        <a href="logout.py">Logout</a>\
    </body>\
</html>').format(name[sid])

    print(body)
elif sid == 14:
    body = ('<!DOCTYPE html>\
<html>\
<body>\
    <h1>Welcome Instructor to the University of Windsor portal!</h1>\
    <img src="uwin_logo.jpg" alt="UWin Logo">\
        <br>\
        <a href="computer_science.html">School of Computer Science</a>\
        <br>\
        <a href="instructor.out">see grades</a>\
        <br>\
        <a href="logout.py">Logout</a>\
    </body>\
</html>')

    print(body)
    
else:
    body = ('<!DOCTYPE html>\
<html>\
<body>\
<h2>Not signed in</h2>\
<a href="login.html">login</a>\
</body>\
</html>')
    print(body)

