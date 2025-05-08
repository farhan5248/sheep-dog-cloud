cd ..
call mvn clean
call mvn org.farhan:sheep-dog-dev-maven-plugin:1.26-SNAPSHOT:cucumber-to-uml -Dtags="%1" -DrepoDir=../sheep-dog-specs/
cd scripts 