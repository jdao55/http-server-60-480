#!/bin/python3
import os
import mmap
from urllib import parse

htmlpage = 'HTTP/1.1 200 OK\r\n\
Content-Length: {0}\r\n\
Content-Type: charset=utf-8\r\n\r\n\
<!DOCTYPE html>\n\
<html>\n\
<head>\n\
<style>\n\
label {{margin-right: 100px;}}\n\
div{{margin-bottom: 10px;}}\n\
</style>\n\
</head>\n\
<body>\n\
<h1>Instructor Page</h1>\n\
<div>\n\
<form action="/submitted.py">\n\
<div>\n\
<label>Student</label>\n\
<label>Binary</label>\n\
<label>Source</label>\n\
<label>Grade</label>\n\
</div>\n\
{1}\n\
<div>\n\
<input type="submit">\n\
</div>\n\
</form>\n\
</div>\n\
</body>\n\
</html>'

retval=htmlpage

#Find student names
student_names = [o for o in os.listdir('students/') if os.path.isdir(os.path.join('students/',o))]
	
student_string = ""
for name in student_names:
	binary = [os.listdir('students/'+name+'/binary/')]
	source = [os.listdir('students/'+name+'/source/')]

	name.replace('students/',"")
	
	grade=""
	with open('grades.txt','rU') as gfile:
		for line in gfile:
			if name in line:
				grade = line.split(' ')[1]
				break

	student_string += '<div><label>{0}</label><label><a href="downloadBin.py?name={1}" download="{2}">{3}</a></label><label><a href="downloadSrc.py?name={4}" download="{5}">{6}</a></label><label><input type="txt" name="{7} grade" value={8}></label></div>'.format(name,name,binary[0],binary[0],name,source[0],source[0],name,grade)
retval = htmlpage.format(322+len(student_string),student_string)
    
print(retval)
