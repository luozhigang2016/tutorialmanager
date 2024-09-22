# Image Stage 1: Extract jar to layers 
FROM eclipse-temurin:17-jre-focal as builder
WORKDIR /tmp
COPY --from=bindir / .
RUN cp *.jar app.jar && \
    java -Djarmode=layertools -jar app.jar extract && \
    mkdir -p /tmp/dist

# Image stage 2: Main
FROM eclipse-temurin:17-jre-focal
LABEL maintainer="brain_wang@163.com"
ENV DEBIAN_FRONTEND noninteractive
ENV PATH="/bin:$PATH" LANG="en_US.UTF-8"
# Update and configure
RUN echo ">>> Configure envionment ..." && \
    LC_ALL=C.UTF-8 && \
    echo ">>> Installing packages ..." && \
    apt-get update -y --fix-missing && \
    apt-get -y clean && \
    echo ">>> Update timezone ..." && \
    rm -f /etc/localtime && ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo ">>> Adding user and group for app ..." && \
    groupadd -g 1000 app && useradd -u 1000 -g app app && \
    echo ">>> App env" && \
    echo "export PATH=$PATH" >> ~/.profile && \
    echo "export LANG=$LANG" >> ~/.profile && \
    mkdir -p /home/app/config && chown -R app:app /home/app && \
    echo ">>> Update done."

USER app
# Copy app
WORKDIR /home/app
COPY --from=builder /tmp/dependencies/ ./
COPY --from=builder /tmp/spring-boot-loader/ ./
COPY --from=builder /tmp/snapshot-dependencies/ ./
COPY --from=builder /tmp/dist/ ./BOOT-INF/classes/static/
COPY --chown=app:app --from=builder /tmp/application/ ./
RUN mv ./BOOT-INF/classes/application*.*  ./config/ 

# Entry
EXPOSE 8080
VOLUME  ["/home/app/config"]
ENV TZ "Asia/Shanghai"
ENV JVM_OPTS ""
ENV VUE_APP_API_BASE_URL "/api"
ENTRYPOINT []
CMD ["/bin/sh", "-c", "java $JVM_OPTS org.springframework.boot.loader.launch.JarLauncher"]
