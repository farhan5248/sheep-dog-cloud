cd ..
call mvn clean package -DskipTests
:: call docker image build --build-arg basedir="sheep-dog-dev-svc/mbt" -t farhan5248/sheep-dog-dev-svc:latest .
:: call docker container run --name="sheep-dog-dev-svc-lcl" --hostname="mbt.sheepdog.org" -v "C:\sheep-dog-dev-svc\mbt:/sheep-dog-dev-svc/mbt" --rm -d -p 8080:8080 farhan5248/sheep-dog-dev-svc:latest
:: call docker container ls -a
call docker compose -f docker/compose.yaml -p sheep-dog-dev up -d
cd scripts 