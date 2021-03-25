FROM openjdk:8

ARG EURECA_COMMON_BRANCH="develop"
ARG EURECA_AS_BRANCH="develop"
ARG EURECA_BACKEND_BRANCH="develop"
ARG ALUMNI_BACKEND_BRANCH="develop"

# Install.
RUN \
  sed -i 's/# \(.*multiverse$\)/\1/g' /etc/apt/sources.list && \
  apt-get update -y && \
  apt-get upgrade -y && \
  apt-get install -y build-essential && \
  apt-get install -y software-properties-common && \
  apt-get install -y byobu curl git htop man unzip vim wget maven && \
  apt-get install -y net-tools iputils-ping && \
  rm -rf /var/lib/apt/lists/*

# Set environment variables.
ENV HOME /root

# Define working directory.
WORKDIR /root

# Installing Common
RUN \
  git clone https://github.com/computacao-ufcg/eureca-common.git && \
  (cd eureca-common && git checkout $EURECA_COMMON_BRANCH && mvn install -Dmaven.test.skip=true) 


# Downloading Authentication service
RUN \
  git clone https://github.com/computacao-ufcg/eureca-as.git && \
  (cd eureca-as && git checkout $EURECA_AS_BRANCH && mvn install -Dmaven.test.skip=true )

# Downloading Eureca Backend
RUN \
  git clone https://github.com/computacao-ufcg/eureca-backend.git && \
  (cd eureca-backend && git checkout $EURECA_BACKEND_BRANCH && mvn install -Dmaven.test.skip=true)

# Downloading Alumni Backend
RUN \
  git clone https://github.com/computacao-ufcg/alumni-backend.git && \
  (cd alumni-backend && git checkout $ALUMNI_BACKEND_BRANCH && mvn install -Dmaven.test.skip=true)  

WORKDIR /root/alumni-backend