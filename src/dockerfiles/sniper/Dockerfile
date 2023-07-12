FROM ubuntu:22.04
# Necessary for tzdata
ENV DEBIAN_FRONTEND=noninteractive
ARG TZ_ARG=UTC
ENV TZ=${TZ_ARG}





# Add i386 support for support for Pin
RUN dpkg --add-architecture i386
RUN apt-get update && apt-get install -y \
    python2 \
    python3 \
    screen \
    tmux \
    binutils \
    libc6:i386 \
    libncurses5:i386 \
    libstdc++6:i386 \
 && rm -rf /var/lib/apt/lists/*
# For building Sniper
RUN apt-get update && apt-get install -y \
    automake \
    build-essential \
    cmake \
    curl \
    wget \
    libboost-dev \
    libsqlite3-dev \
    zlib1g-dev \
    libbz2-dev \
 && rm -rf /var/lib/apt/lists/*
# For building RISC-V Tools
RUN apt-get update && apt-get install -y \
    autoconf \
    automake \
    autotools-dev \
    bc \
    bison \
    curl \
    device-tree-compiler \
    flex \
    gawk \
    gperf \
    libexpat-dev \
    libgmp-dev \
    libmpc-dev \
    libmpfr-dev \
    libtool \
    libusb-1.0-0-dev \
    patchutils \
    pkg-config \
    texinfo \
    zlib1g-dev \
 && rm -rf /var/lib/apt/lists/*
# Helper utilities
RUN apt-get update && apt-get install -y \
    gdb \
    gfortran \
    git \
    g++-9 \
    vim \
 && rm -rf /var/lib/apt/lists/*



# Download and extract Pin
RUN wget https://software.intel.com/sites/landingpage/pintool/downloads/pin-3.27-98718-gbeaa5d51e-gcc-linux.tar.gz && \
    tar -xzf pin-3.27-98718-gbeaa5d51e-gcc-linux.tar.gz && \
    rm pin-3.27-98718-gbeaa5d51e-gcc-linux.tar.gz && \
    mv pin-3.27-98718-gbeaa5d51e-gcc-linux pin-tool


# Get the Sniper source code
RUN git clone --depth 1 https://$GITHUB_USERNAME:$GITHUB_TOKEN@github.com/snipersim/snipersim.git /usr/local/src/sniper




# Set PIN_HOME to the location of Pin
ENV PIN_HOME /app/sniper/pin_kit/pin

# Build Sniper
WORKDIR /usr/local/src/sniper
RUN make -j 8

# Set the working directory for running an application
WORKDIR /usr/local/src/sniper/




