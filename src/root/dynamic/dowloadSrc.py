from urllib import parse

line = input()
query_str = (line.split(" ")[1]).split("?")[1]

query = dict(parse.parse_qsl(query_str.strip()));

with open("student/"+query["name"]+"/source.c", "rb") as binaryfile:
    myArr = bytearray(binaryfile.read())

resp = ("HTTP/1.1 200 OK\r\n\
Content-Length: {0}\r\n\
Content-Disposition: attachment\r\n\
Content-Type: application/octet-stream\r\n\r\n{1}")
print(resp.format( len(myArr), myArr.decode("utf-8")))
