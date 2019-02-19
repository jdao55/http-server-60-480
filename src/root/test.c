#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int main(int argc, char **argv)
{
    char buff[2000];
    scanf("%s", &buff);
    printf("HTTP/1.1 200 OK\r\n\
Content-Length: 47\r\n\
Content-Type: charset=utf-8\r\n\r\n\
<!DOCTYPE html>\
<html>\
<body>\
sdf%saf\
</body>\
</html>", buff);
}
