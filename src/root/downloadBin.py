from urllib import parse
import sys
line = input()
query_str = (line.split(" ")[1]).split("?")[1]

query = dict(parse.parse_qsl(query_str.strip()));

with open("students/"+query["name"]+"/binary/executable.out", "rb") as binaryfile:
    myArr = bytearray(binaryfile.read())

resp = ("HTTP/1.1 200 OK\r\n\
Content-Length: {0}\r\n\
Content-Disposition: attachment\r\n\
Content-Type: application/octet-stream\r\n\r\n")

print(resp.format(len(myArr)))
sys.stdout.flush()
sys.stdout.buffer.write(myArr);
      