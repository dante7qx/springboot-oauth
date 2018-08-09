# !/bin/sh

m2_home=/Users/dante/Documents/Server/Maven/apache-maven-3.5.0
deploy=$1

${m2_home}/bin/mvn -v
${m2_home}/bin/mvn clean package -Dmaven.test.skip=true

if [ "$deploy" = "deploy" ]; then
	echo "替换新的 jar --> third-client-1.0.jar"
	cp -f target/third-client-1.0.jar /Users/dante/Documents/Technique/Docker/mvnrepo/
fi