#!/bin/sh
until docker container exec -it mysql_db mysqladmin ping -P 3306 -proot | grep "SUPERVISOR_ID" ; do
  >&2 echo "MySQL is unavailable - waiting for it... 😴"
  sleep 1
done