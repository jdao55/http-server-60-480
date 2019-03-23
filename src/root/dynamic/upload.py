#!/bin/python3
import shutil
import os

dest1 = 'static/downloads/'

source = input()
source = source.rstrip(" \n")
files = os.listdir(source)

for f in files:
    if f[0] == '.':
        continue
    shutil.copy("temp/" + f, dest1)
    os.remove("temp/" + f)

print("<!DOCTYPE html>\
<html>\
<body>\
Upload Complete!\
<br><a href=\"uploadfiles.html\">back</a>\
</body>\
</html>")
