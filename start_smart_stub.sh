# Build the code
./gradlew clean check build
# Stops any existing smart stub container
docker container stop my-smart-stub
# Removes all exited containers
docker rm -v $(docker ps -aq -f status=exited)
# Runs teh smart stub container using the local Dockerfile and calls it my-smart-stub
docker run --name my-smart-stub -d -p 8120:8120 smart-stub
# Lists all running containers.
docker container ls