#!/bin/sh
# Copyright 2009 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Launch script for the noop interpreter.
# TODO(alex): we should extend this script to launch all the language tools

DIRNAME=$(dirname "$0")
SAVED="$(pwd)"
cd "$DIRNAME/.."
NOOP_HOME="$(pwd -P)"
cd "$SAVED"
CLASSPATH=''
for lib in $(ls ${NOOP_HOME}/lib); do
    CLASSPATH="${CLASSPATH}:${NOOP_HOME}/lib/$lib"
done
JAVA='java'
[ -d "${JAVA_HOME}" ] && JAVA="${JAVA_HOME}/bin/java"

CMD="${JAVA} -cp ${CLASSPATH} noop.interpreter.InterpreterMain"
echo "DEBUG: running noop with command line '${CMD} $@'"
$CMD $@
