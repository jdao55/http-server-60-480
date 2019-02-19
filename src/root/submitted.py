#!/bin/python3
import os
import mmap
from urllib import parse


htmlpage = 'HTTP/1.1 200 OK\r\n\
Content-Length: 135\r\n\
Content-Type: charset=utf-8\r\n\r\n\
<!DOCTYPE html>\n\
<html>\n\
<head>\n\
</head>\n\
<body>\n\
<p>Grades submitted!\n\
<form action="/instructor.py"><div><input type="submit" value="Back"></div></form>\n\
</p>\n\
</body>\n\
</html>'

student_names = [o for o in os.listdir('students/') if os.path.isdir(os.path.join('students/',o))]


url=input().split(' ')[1]
split=url.split('?')[1] 
query = dict(parse.parse_qsl(split));

grades = open('grades.txt','w')
for x in query:
	grades.write(x.replace(" grade"," ")+query[x]+"\n")

retval=htmlpage
    
print(retval)
