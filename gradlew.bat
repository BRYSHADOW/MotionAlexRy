@rem Gradle wrapper for Windows
@echo off
if defined JAVA_HOME goto :OkJHome
set JAVA_HOME_ERR=JAVA_HOME not set
goto :End

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto :OkJavaExe
goto :End

:OkJavaExe
set GRADLE_WRAPPER_JAR=%~dp0gradle\wrapper\gradle-wrapper.jar
if not exist "%GRADLE_WRAPPER_JAR%" (
    gradle %*
    goto :End
)
"%JAVA_HOME%\bin\java.exe" -classpath "%GRADLE_WRAPPER_JAR%" org.gradle.wrapper.GradleWrapperMain %*

:End
