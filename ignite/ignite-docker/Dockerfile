FROM centos:7
MAINTAINER jschmidt10@github.com

LABEL description="A simple Apache Ignite node." \
      run="docker run -p 47500:47500 --net=host -v config/:/etc/ignite/conf -e IGNITE_CONF=default-ignite.xml jschmidt10/ignite-docker" \
      source="http://github.com/jschmidt10/side-projects/tree/master/ignite/ignite-docker"

RUN yum update && \
    yum install -y java-1.8.0-openjdk && \
    yum install -y unzip && \
    yum clean all

RUN curl https://archive.apache.org/dist/ignite/1.6.0/apache-ignite-fabric-1.6.0-bin.zip -o /apache-ignite-fabric-1.6.0-bin.zip && \
    unzip /apache-ignite-fabric-1.6.0-bin.zip

RUN mkdir -p /etc/ignite/conf
VOLUME /etc/ignite/conf

EXPOSE 47500
CMD /apache-ignite-fabric-1.6.0-bin/bin/ignite.sh file:///etc/ignite/conf/$IGNITE_CONF