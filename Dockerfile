FROM luke19/spring-base-image:1629374721

LABEL maintainer="KieniL"
LABEL name="cert"
LABEL version="1.0.0"
LABEL author="KieniL"
LABEL contact="https://github.com/KieniL/FamilyCluster_Cert/issues"
LABEL documentation="https://github.com/KieniL/FamilyCluster_Cert"

ENV DB_CERT_HOST=tmp
ENV DB_CERT_DB=tmp
ENV DB_CERT_USER=tmp
ENV DB_CERT_PASS=tmp
ENV CERT_LOG_LEVEL=DEBUG
ENV AUTH_URL=test1234

COPY ./target/cert.jar /app/app.jar

ENTRYPOINT ["java", "-Djava.io.tmpdir=/app/tmp" ,"-jar", "app.jar"]

EXPOSE 8080