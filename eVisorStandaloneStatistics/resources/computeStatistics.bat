echo off

echo Patch antiguo: %CLASSPATH%

set JDK_LIB=%JAVA_HOME%

set oldCP = %CLASSPATH%

set CLASSPATH=.;.\configuration;.\configuration\messages-content
set CLASSPATH=%CLASSPATH%;.\lib\antlr-2.7.6.jar
set CLASSPATH=%CLASSPATH%;.\lib\asm-3.3.1.jar
set CLASSPATH=%CLASSPATH%;.\lib\commons-collections-3.2.1.jar
set CLASSPATH=%CLASSPATH%;.\lib\dom4j-1.6.1.jar
set CLASSPATH=%CLASSPATH%;.\lib\eVisorCore-3.0.0.jar
set CLASSPATH=%CLASSPATH%;.\lib\eVisorPersistence-3.0.0.jar
set CLASSPATH=%CLASSPATH%;.\lib\eVisorStandaloneStatistics-3.0.0.jar
set CLASSPATH=%CLASSPATH%;.\lib\hibernate-commons-annotations-5.0.1.Final.jar
set CLASSPATH=%CLASSPATH%;.\lib\hibernate-core-4.0.1.Final.jar
set CLASSPATH=%CLASSPATH%;.\lib\hibernate-entitymanager-4.0.1.Final.jar
set CLASSPATH=%CLASSPATH%;.\lib\hibernate-jpa-2.0-api-1.0.1.Final.jar
set CLASSPATH=%CLASSPATH%;.\lib\javassist-3.18.1-GA.jar
set CLASSPATH=%CLASSPATH%;.\lib\jboss-logging-3.1.0.CR2.jar
set CLASSPATH=%CLASSPATH%;.\lib\jboss-transaction-api_1.2_spec-1.0.0.Final.jar
set CLASSPATH=%CLASSPATH%;.\lib\log4j-1.2.16.jar
set CLASSPATH=%CLASSPATH%;.\lib\tika-core-1.9.jar
set CLASSPATH=%CLASSPATH%;.\lib\ojdbc6-11.2.0.3.jar


if not "%oldCP%" == "" set CLASSPATH=%CLASSPATH%;%oldCP%

echo Usando path: %CLASSPATH%

%JDK_LIB%\bin\java -classpath "%CLASSPATH%" es.gob.signaturereport.standalone.maudit.statistics.ExternalStatisticsComputer %1 %2