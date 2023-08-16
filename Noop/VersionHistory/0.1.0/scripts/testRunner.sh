CP=$HOME/.m2/repository/org/slf4j/slf4j-simple/1.5.6/slf4j-simple-1.5.6.jar
CP=$CP:$HOME/.m2/repository/org/slf4j/slf4j-api/1.5.6/slf4j-api-1.5.6.jar
CP=$CP:$HOME/.m2/repository/org/antlr/antlr-runtime/3.1.1/antlr-runtime-3.1.1.jar
CP=$CP:$HOME/.m2/repository/org/scala-lang/scala-library/2.7.5/scala-library-2.7.5.jar
CP=$CP:$HOME/.m2/repository/com/google/inject/guice/2.0/guice-2.0.jar
CP=$CP:$HOME/.m2/repository/com/google/inject/extensions/guice-assisted-inject/2.0/guice-assisted-inject-2.0.jar
CP=$CP:$HOME/.m2/repository/aopalliance/aopalliance/1.0/aopalliance-1.0.jar
CP=$CP:core/target/classes
CP=$CP:core/target/generated
CP=$CP:interpreter/target/classes
CP=$CP:interpreter/src/main/noop
java -classpath $CP noop.interpreter.testing.TestRunnerMain $@

