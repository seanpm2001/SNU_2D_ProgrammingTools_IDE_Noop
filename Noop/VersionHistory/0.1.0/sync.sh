#!/bin/sh
set -x

cmd_script=${0}
cmd_args=${@}

if [ ${#} -gt 0 ]; then
  if [ ${1} = -N ]; then
    shift cmd_args
    invoke ${cmd_args}
  fi
fi

orig_dir=`pwd`
project_dir=${orig_dir}/`dirname ${cmd_script}`
dirs=`find ${orig_dir} -name "pom.yml"`
#for dir in ${raw_dirs}; do
#  dir=`dirname ${dir}`
#  dirs+=`echo "${dir}"`
#done

for file in ${dirs}; do
    dir=`dirname ${file}`
    cd ${dir}
    mvn -s ${project_dir}/init.settings.xml org.twdata.maven:maven-yamlpom-plugin:0.4.1:sync ${cmd_args}
done
cd ${orig_dir}