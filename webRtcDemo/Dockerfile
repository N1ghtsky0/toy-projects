# Base image with Tomcat and JDK 21
FROM tomcat:10.1-jdk21

# Remove default webapps to keep image clean
RUN rm -rf /usr/local/tomcat/webapps/*

# Add the WAR file to the Tomcat webapps directory
# WAR 파일 이름은 Maven build 후 생성된 이름과 일치해야 함 (예: webRtcDemo-0.0.1-SNAPSHOT.war)
COPY target/webRtcDemo-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
