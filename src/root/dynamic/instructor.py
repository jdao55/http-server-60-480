#!/bin/python3
import os
from urllib import parse
htmlpage = 'HTTP/1.1 200 OK\r\n\
Content-Length: 4000\r\n\
Content-Type: charset=utf-8\r\n\r\n\
<!DOCTYPE html>\n\
<html>\n\
<head>\n\
</head>\n\
<body>\n\
<h1>Instructor Page</h1>\n\
<div>\n\
<table style="width:50%" align="center">\n\
<tr>\n\
<th>Student</th>\n\
<th>Binary</th>\n\
<th>Executable</th>\n\
<th>Grade</th>\n\
</tr>\n\
{0}\n\
</table>\n\
<form action="/instructor.py">\n\
<div style="text-align:center">\n\
<input type="submit" value="Submit">\n\
</div>\n\
</form>\n\
</div>\n\
</body>\n\
</html>'

query = dict(parse.parse_qsl(input().strip()));
retval=htmlpage

#Find student names
student_names = [o for o in os.listdir('students/') if os.path.isdir(os.path.join('students/',o))]

student_string = ""
for name in student_names:
	name.replace('students/',"")
	student_string += '<tr><th>{0}</th><th>Binary</th><th>Executable</th><th><form><div><input type="txt"></div></form></th></tr>'.format(name)
retval = htmlpage.format(student_string)
#Find files for each student
    
    
print(retval)
