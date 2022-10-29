#!/bin/bash
apt update && apt upgrade -y
apt install wget build-essential libncursesw5-dev apt-utils -y
apt install openjdk-17-jre -y

# Python
if [ ! -d "/opt/Python-3.10.0" ]
then
  apt install software-properties-common zlib1g-dev libncurses5-dev libgdbm-dev libnss3-dev libssl-dev libreadline-dev libffi-dev libsqlite3-dev libbz2-dev -y
  cd opt || return 1
  wget https://www.python.org/ftp/python/3.10.0/Python-3.10.0.tgz
  tar -xf Python-3.10.*.tgz
  cd Python-3.10.*/ || return 1
  ./configure --enable-optimizations
  make -j 4
  make altinstall
  cd .. && rm -rf Python-3.10.0.tgz
  cd /
fi

# CMAKE
if [ ! -d "/opt/cmake-3.25.0-rc2" ]
then
  apt install libssl-dev apt-utils -y
  cd /opt || return 1
  wget https://github.com/Kitware/CMake/releases/download/v3.25.0-rc2/cmake-3.25.0-rc2.tar.gz
  tar -xf cmake-3.25.0-rc2.tar.gz
  cd cmake-3.25.0-rc2 || return 1
  ./bootstrap
  make -j 4
  make install
  cd .. && rm -rf cmake-3.25.0-rc2.tar.gz
  cd /
fi

# CuraEngine:
if [ ! -d "/opt/CuraEngine" ]
  then
  cd opt || return 1
  apt install python3-pip apt-utils -y
  apt install git ninja-build -y
  pip3 install conan --upgrade
  conan config install https://github.com/ultimaker/conan-config.git
  conan profile new default --detect --force
  git clone https://github.com/Ultimaker/CuraEngine.git
  cd CuraEngine || return 1
  mkdir build
  conan install . --build=missing --update
  cmake --preset release
  cmake --build --preset release
  . build/generators/conanrun.sh
  ln -s /opt/CuraEngine/build/Release/CuraEngine /usr/bin/curaengine
  cd /
fi


# Cura
if [ ! -d "/opt/Cura" ]
then
  cd opt || return 1
  wget clone https://github.com/Ultimaker/Cura.git
  cp ./Cura/resources/extruders/creality_base_extruder_0.def.json ./Cura/resources/definitions/
  cd /
fi

# Environment variables
if  ! grep -qF 'export CURA_ENGINE_SEARCH_PATH=/opt/Cura/resources/definitions' $(home)/.bashrc ; then
  echo "export CURA_ENGINE_SEARCH_PATH=/opt/Cura/resources/definitions" >> ~/.bashrc
fi