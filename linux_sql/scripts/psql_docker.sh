#!/bin/sh

# Capture CLI arguments
cmd=$1
db_username=$2
db_password=$3
echo $cmd
# Start docker service if not running
# Check if docker is running, if not, start it


#This code is commented because it was causing issues (we viewed it together in a meeting)
#sudo systemctl status docker || systemctl start docker

# Check container status
echo "test"
docker container inspect jrvs-psql
echo "after"
container_status=$?

# User switch case to handle create|stop|start options
case $cmd in 
  create)
  
  # Check if the container is already created
  if [ $container_status -eq 0 ]; then
    echo 'Container already exists'
    exit 1	
  fi

  # Check # of CLI arguments
  if [ $# -ne 3 ]; then
    echo 'Create requires username and password'
    exit 1
  fi
  
  # Create container
  docker volume create pgdata
  
  # Start the container
  docker run --name jrvs-psql -e POSTGRES_PASSWORD=$db_password -e POSTGRES_USER=$db_username -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine
  
  # Make sure you understand what's `$?`
  exit $?
  ;;

  start|stop) 
  # Check instance status; exit 1 if container has not been created
  if [ $container_status -ne 0 ]; then
    echo "Container does not exist. Please create it first."
    exit 1
  fi

  # Start or stop the container
  echo $cmd
  docker container $cmd jrvs-psql

  exit $?
  ;;	

  *)
    echo 'Illegal command'
    echo 'Commands: start|stop|create'
    exit 1
    ;;
esac

