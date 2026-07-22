#!/bin/sh
# Gradle wrapper – tu dong download Gradle neu chua co
# Source: https://github.com/gradle/gradle/blob/master/gradlew

set -e

DIRNAME=$(dirname "$0")
APP_HOME=$(cd "$DIRNAME" && pwd)
APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")

# Path to java
if [ -n "$JAVA_HOME" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD=java
fi

# Wrapper properties
GRADLE_WRAPPER_PROPERTIES="$APP_HOME/gradle/wrapper/gradle-wrapper.properties"
GRADLE_WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Fall back to system gradle if jar missing (CI environment)
if [ ! -f "$GRADLE_WRAPPER_JAR" ]; then
    exec gradle "$@"
fi

exec "$JAVACMD" \
    -classpath "$GRADLE_WRAPPER_JAR" \
    org.gradle.wrapper.GradleWrapperMain \
    "$@"
