FROM ubuntu:14.04

# Update and install dependencies
RUN apt-get update && apt-get install -y \
    git \
    build-essential \
    gcc \
    libconfig-dev \
    libhdf5-dev   \
    wget	\
    scons	\
    gdb 	\
    libconfig++-dev \
    libconfig-dbg \
    libelf-dev \
    && rm -rf /var/lib/apt/lists/*


# Set the working directory
WORKDIR /usr/local/src

# Clone zsim-plusplus repository
RUN git clone https://github.com/dzhang50/zsim-plusplus.git


WORKDIR /usr/local/src/zsim-plusplus


# Set environment variables for libconfig
#ENV LIBCONFIG_HOME=/usr/include/libconfig/installation

# Set environment variables for libhdf5
#ENV HDF5_HOME=/usr/include/libhdf5/installation


ENV PINPATH=/usr/include/pin

WORKDIR /usr/include
RUN wget https://software.intel.com/sites/landingpage/pintool/downloads/pin-2.14-71313-gcc.4.4.7-linux.tar.gz && \
    tar -xzf pin-2.14-71313-gcc.4.4.7-linux.tar.gz && \
    rm pin-2.14-71313-gcc.4.4.7-linux.tar.gz && \
    mv pin-2.14-71313-gcc.4.4.7-linux pin

WORKDIR /usr/include
RUN cp -r /usr/include/asm-generic /usr/include/asm
# Install additional dependencies


#ENV ASLR off

#RUN echo 0 |sudo tee /proc/sys/kernel/randomize_va_space
WORKDIR /usr/local/src/zsim-plusplus/src
RUN sed -i '/\/\/Load tool/a\    args.push_back("-injection");\n    args.push_back("parent");\n		args.push_back("-ifeellucky");' pin_cmd.cpp

WORKDIR /usr/local/src/zsim-plusplus

RUN scons -j16

#RUN ./build/opt/zsim tests/simple.cfg