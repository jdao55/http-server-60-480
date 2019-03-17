#!/bin/python3
import sys

cookies = "None found"
for line in sys.stdin:
    if "Cookie" in line:
        cookies = line.split()[1]
print("<!DOCTYPE html><html>Session Cookies : %s</html>" % cookies)
