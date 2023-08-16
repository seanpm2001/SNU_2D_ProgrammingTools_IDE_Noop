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

# scalac runs out of heap on my machine otherwise
ENV['JAVA_OPTS'] ||= '-Xmx512m -XX:MaxPermSize=256m'

require 'buildr/antlr'
# require 'buildr/scala'

VERSION_NUMBER = "0.1.0-SNAPSHOT"
GROUP = "com.google"
COPYRIGHT = "Apache 2.0"

repositories.remote << "http://www.ibiblio.org/maven2"

ANTLR = ["org.antlr:antlr:jar:3.1.1"]
ANTLR_RUNTIME = ["org.antlr:antlr-runtime:jar:3.1.1"]
SLF4J = ["org.slf4j:slf4j-api:jar:1.5.6", "org.slf4j:slf4j-simple:jar:1.5.6"]
GUICE = ["aopalliance:aopalliance:jar:1.0",
         "com.google.inject:guice:jar:2.0", "com.google.inject.extensions:guice-assisted-inject:jar:2.0" ]
GCOLLECT = ["com.google.collections:google-collections:jar:1.0"]
JUNIT = ["junit:junit:jar:4.7"]
COMMONS_LANG = ["commons-lang:commons-lang:jar:2.4"]

# Force Buildr Antlr integration to use the version we specify
Buildr::ANTLR::REQUIRES.clear
Buildr::ANTLR::REQUIRES.concat(ANTLR)

desc "The Noop language"
define 'noop', :version=>VERSION_NUMBER do

  manifest["Implementation-Vendor"] = COPYRIGHT

  define "core" do
    compile.with [SLF4J, GCOLLECT, JUNIT, COMMONS_LANG]
    package :jar
  end

  define "grammar" do
    antlr = antlr([_('src/main/antlr3/noop/grammar/antlr/Doc.g'), \
                   _('src/main/antlr3/noop/grammar/antlr/Noop.g'), \
                   _('src/main/antlr3/noop/grammar/antlr/NoopAST.g')],
        :in_package=>'noop.grammar.antlr')
    compile.from(antlr).
      with [project("core"), ANTLR, SLF4J]
    package :jar
  end

  define "compiler" do
    resources.from [_('src/main/stringtemplate')]
    compile.with [project("core"), ANTLR, SLF4J]
    package :jar
  end

  define "examples" do
    resources.from [_('noop')]
  end

  define "interpreter" do
    # TODO - only want examples as a test resource
    resources.from [_('src/main/noop'), project("examples")._('noop')]
    package(:jar).with(:manifest=>{'Main-Class' => 'noop.interpreter.InterpreterMain'})
    compile.with [project("core"), project("grammar"), ANTLR_RUNTIME, SLF4J, GUICE]
    package(:zip).
      include(compile.dependencies, :path=>'lib').
      include(_("target/#{id}-#{version}.jar"), :path=>'lib').
      # TODO(alex): need to chmod 755 if I can figure out how
      include(project("noop")._("scripts/noop.sh"), :as=>"noop", :path=>'bin').
      include(project("noop")._("COPYING"))
    package :sources
  end
end
