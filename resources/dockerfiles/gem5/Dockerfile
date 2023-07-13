FROM ubuntu:22.04
ENV DEBIAN_FRONTEND=noninteractive
RUN apt -y update && apt -y upgrade && \
    apt -y install build-essential git m4 scons zlib1g zlib1g-dev \
    libprotobuf-dev protobuf-compiler libprotoc-dev libgoogle-perftools-dev \
    python3-dev doxygen libboost-all-dev libhdf5-serial-dev python3-pydot \
    libpng-dev libelf-dev pkg-config pip python3-venv black
RUN pip install mypy pre-commit

# clone repo
WORKDIR /usr/local/src
RUN git clone --depth 1 --branch v22.1.0.0 https://gem5.googlesource.com/public/gem5

 

# build it
WORKDIR /usr/local/src/gem5
RUN scons build/X86/gem5.opt -j 8