#!/bin/python3
import sys
from os import listdir
from os.path import isfile, join
from os import remove

path = sys.stdin.read()
path = path.rstrip(" \n")
#path = path.replace("\"","")
files = [f for f in listdir(path) if isfile(join(path, f))]

for file in files:
    openfile = open(path+"/"+file,"r")
    newfile = open("./uploads/"+file,"w+")
    for line in openfile:
        newfile.write(line)
    openfile.close()
    newfile.close()
    remove(path+"/"+file)

print("<!DOCTYPE html>\
<html>\
<body>\
Upload Complete!\
<br><a href=\"uploadfiles.html\">back</a>\
</body>\
</html>")

