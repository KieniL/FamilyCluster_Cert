FROM luke19/spring-base-image:1627557346

ENV DB_CERT_HOST=tmp
ENV DB_CERT_DB=tmp
ENV DB_CERT_USER=tmp
ENV DB_CERT_PASS=tmp
ENV CERT_LOG_LEVEL=DEBUG
ENV AUTH_URL=test1234

COPY ./target/cert.jar /APP/app.jar

ENTRYPOINT ["java" ,"-jar app.jar"]

EXPOSE 8080