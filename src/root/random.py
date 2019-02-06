#!/bin/python3
import random
from urllib import parse
htmlpage = '<!DOCTYPE html>\n\
<html>\n\
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">\
<head>\n\
</head>\n\
<body>\n\
<h1>Ramdom num gen</h1>\n\
<form style="padding-left: 30px; display: block; box-sizing: border-box;">\n\
<div>\n\
<label>\n\
Min:\n\
<input class=".w3-input" type="text" name="min" size="12">\n\
</label>\n\
<label>\n\
Max:\n\
<input class=".w3-input" type="text" name="max" size="12">\n\
</label>\n\
</div>\n\
<br>\n\
<div>\n\
<button class="w3-button w3-black w3-section" type="submit">SEND</button>\
</div>\n\
</form>\n\
{0}\n\
</body>\n\
</html>'


query = dict(parse.parse_qsl(input().strip()));
retval=htmlpage
if "min" in query and "max" in query :
    rand_int = random.randint( int(query["min"]) , int(query["max"]) )
    value = '<div style="padding-left: 15px"><h2>{0}</h2></div>'.format( rand_int)
    retval= htmlpage.format(value)
else:
    retval= htmlpage.format("")
    
    
print(retval)
