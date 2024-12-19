#!/bin/bash

# Setup arguments
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
cpu_number=$(lscpu | grep "^CPU(s):" | awk '{print $2}' | xargs) 
cpu_architecture=$(lscpu | grep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(lscpu | grep "^Model name:" | awk -F ':' '{print $2}' | xargs)
cpu_mhz=$(lscpu | grep "^CPU MHz:" | awk '{print $3}' | xargs)
l2_cache=$(lscpu | grep "^L2 cache:" | awk '{print $3}' | sed 's/K//g' | sed 's/M//g')
total_mem=$(vmstat --unit M | tail -1 | awk '{print $4}' | xargs)
timestamp=$(vmstat -t | awk '{print $18, $19}')

# SQL insert statement
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, timestamp, total_mem)
VALUES('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, '$timestamp', $total_mem);"

# Set up env var for pql cmd
export PGPASSWORD=$psql_password

psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?
