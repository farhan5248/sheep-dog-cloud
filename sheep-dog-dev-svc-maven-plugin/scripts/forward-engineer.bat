cd ..
call mvn clean
call mvn org.farhan:sheep-dog-dev-maven-plugin:1.26-SNAPSHOT:uml-to-cucumber-spring -DrepoDir=../../sheep-dog-qa/sheep-dog-specs/ -Dtags="round-trip"
cd scripts 