#!/bin/sh
#
JDK_LIB=$JAVA_HOME

oldCP=$CLASSPATH

lib1=./lib


#unset CLASSPATH
CLASSPATH=""
for i in $lib1/* ; do
  if [ "x${CLASSPATH}" != "x" ]; then
    CLASSPATH=${CLASSPATH}:$i
  else
    CLASSPATH=.:./conf:$i
  fi
done

if [ "x${oldCP}" != "x" ]; then
    CLASSPATH=${CLASSPATH}:${oldCP}
fi


echo Usando path ${CLASSPATH}

$JDK_LIB/bin/java -Xms128m -Xmx1024m -classpath ${CLASSPATH} es.gob.signaturereport.maudit.statistics.ExternalStatisticsComputer $1 $2
 
