cd ..
call git reset --hard HEAD
call git clean -fdx
call git pull
call ./gradlew.bat release
call git push
cd scripts
