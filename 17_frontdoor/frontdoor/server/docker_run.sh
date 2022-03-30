#!/bin/bash
docker run "$@" --rm -p "127.0.0.1:8085:80" --name "reachingout_server" -it "spritzers/reachingout_server"
