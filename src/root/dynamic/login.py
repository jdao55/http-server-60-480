#!/bin/python3
import os
from urllib import parse
pswd = {
    "Adam": ["Adoreh", 0],
    "Vivek": ["Chopra", 1],
    "Joseph": ["Dao", 2],
    "Tajinder": ["Dhillon", 3],
    "Zach": ["Easterbrook", 4],
    "Tomisin": ["Falodun", 5],
    "Yicheng": ["Lu", 6],
    "Brandon": ["Lucier", 7],
    "Ali": ["Mallem", 8],
    "Mike": ["Melanson", 9],
    "Manpreet": ["Singh", 10],
    "Zheng": ["Wang", 11],
    "Xiaobang": ["Zhang", 12],
    "Matthew": ["Zeidler", 13],
    "admin": ["root", 14]
}
query = dict(parse.parse_qsl(input().strip()))
Cookie = False
user = query["username"]
user_pswd = query["pswd"]

if user in pswd:
    if user_pswd == pswd[user][0]:
        Cookie = True

if Cookie == False:
    body = ('<!DOCTYPE html>\n\
<html>\n\
<head>\n\
</head>\n\
<body>\n\
<h1>Login Error</h1>\n\
<a href="login.html">Back</a>\
</body>\n\
</html>')
    header = ('HTTP/1.1 200 OK\r\n\
Content-Length: {0}\r\n\
Content-Type: charset=utf-8\r\n\r\n').format(len(body))

    print(header)
    print(body)
elif pswd[user][1] < 14:
    body = ('<!DOCTYPE html>\
<html>\
<body>\
    <h1>Welcome to the University of Windsor portal!</h1>\
    <img src="uwin_logo.jpg" alt="UWin Logo">\
        <br>\
        <a href="home.py">Home</a>\
        <br>\
        <a href="viewgrade.py">view grade</a>\
        <br>\
        <a href="uploadfiles.html">upload files</a>\
    <br>\
<a href="logout.py">Logout</a>\
    </body>\
</html>')
    header = ('HTTP/1.1 200 OK\r\n\
Content-Length: {0}\r\n\
Content-Type: charset=utf-8\r\n\
Set-Cookie: id={1}\r\n\r\n').format(len(body), pswd[user][1])
    print(header)
    print(body)
else:
    body = ('<!DOCTYPE html>\
<html>\
<body>\
    <h1>Welcome to the University of Windsor portal!</h1>\
    <img src="uwin_logo.jpg" alt="UWin Logo">\
        <br>\
        <a href="computer_science.html">School of Computer Science</a>\
        <br>\
        <a href="instructor.out">see grades</a>\
        <br>\
        <a href="logout.py">Logout</a>\
    </body>\
</html>')
    header = ('HTTP/1.1 200 OK\r\n\
Content-Length: {0}\r\n\
Content-Type: charset=utf-8\r\n\
Set-Cookie: id={1}\r\n\r\n').format(len(body), pswd[user][1])
    print(header)
    print(body)
