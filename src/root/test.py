from email.utils import formatdate
from datetime import datetime
from time import mktime

now = datetime.now()
stamp = mktime(now.timetuple())
x = input()
print("HTTP/1.1 200 OK\r\n\
Content-Length: 47\r\n\
Content-Type: charset=utf-8\r\n\r\n\
<!DOCTYPE html>\
<html>\
<body>\
sdfaf\
</body>\
</html>")
