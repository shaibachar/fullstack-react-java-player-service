@echo off
REM Remove all unused Docker images
docker image prune -a -f
echo All unused Docker images have been removed.
pause