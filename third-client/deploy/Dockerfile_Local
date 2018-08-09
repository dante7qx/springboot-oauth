FROM dante2012/spirit-java:10

LABEL MAINTAINER "dante <sunchao.0129@163.com>"

ADD third-client-1.0.jar app.jar

RUN sh -c 'touch app.jar'
RUN chmod 644 app.jar

VOLUME ["/logs"]

ENV JAVA_OPTS="-Xms1g -Xmx1g"

EXPOSE 8001
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]