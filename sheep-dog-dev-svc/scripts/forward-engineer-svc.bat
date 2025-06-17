cd ..
call mvn clean org.farhan:sheep-dog-dev-svc-maven-plugin:1.29-SNAPSHOT:uml-to-cucumber-spring -Dtags="round-trip"
cd scripts 