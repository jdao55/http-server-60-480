#!/bin/bash
echo `ip addr | rg inet`
echo "starting server port:8081 site-path:root"
java httpServer.Main -p 8081 -d root/
