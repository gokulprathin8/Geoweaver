#!/bin/bash
java -jar /usr/local/bin/geoweaver-1.1.0.jar &
SERVER_PID=$!

# Wait for the server to start (adjust the time as needed)
sleep 4

# Open the URL in the default web browser
xdg-open http://localhost:8070/Geoweaver

# Wait for the server to complete before exiting
wait $SERVER_PID
