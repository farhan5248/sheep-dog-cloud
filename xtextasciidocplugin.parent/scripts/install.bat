cd ..
call .\gradlew.bat clean
call .\gradlew.bat publishToMavenLocal installExtension
cd scripts 