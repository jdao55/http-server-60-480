#!/bin/python3
import shutil
import os


dest1 = 'uploads/'

source = input()
source = source.rstrip(" \n")
files = os.listdir(source)

for f in files:
        shutil.move(source+f, dest1)


print("<!DOCTYPE html>\
<html>\
<body>\
Upload Complete!\
<br><a href=\"uploadfiles.html\">back</a>\
</body>\
</html>")

