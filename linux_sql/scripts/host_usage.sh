#!/bin/bash

#Setup arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Validate arguments
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

# Retrieve hardware specification variables
memory_free=$(echo "$vmstat_mb" | awk '{print $4}' | tail -n1 | xargs)
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk '{print $15}' | xargs) 
cpu_kernel=$(echo "$vmstat_mb" | tail -1 | awk '{print $14}' | xargs)  
disk_io=$(vmstat -d | tail -1 | awk '{print $10}' | xargs)  
disk_available=$(df -BM / | tail -1 | awk '{print $4}' | sed 's/M//')
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

host_id="(SELECT id FROM host_info WHERE hostname='$hostname')"

# SQL insert statement
insert_stmt="INSERT INTO host_usage(timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) 
VALUES('$timestamp', $host_id, $memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available);"

# Set up env var for pql cmd
export PGPASSWORD=$psql_password

psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?
