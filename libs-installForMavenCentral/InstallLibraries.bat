@echo off
rem #Dependencias con ZXing-1.6.
call mvn install:install-file -Dfile=./com/google/zxing/core/1.6-SNAPSHOT/core-1.6-SNAPSHOT.jar -DgroupId=com.google.zxing -DartifactId=core -Dversion=1.6-SNAPSHOT -Dpackaging=jar -DpomFile=./com/google/zxing/core/1.6-SNAPSHOT/core-1.6-SNAPSHOT.pom
call mvn install:install-file -Dfile=./com/google/zxing/javase/1.6-SNAPSHOT/javase-1.6-SNAPSHOT.jar -DgroupId=com.google.zxing -DartifactId=core -Dversion=1.6-SNAPSHOT -Dpackaging=jar -DpomFile=./com/google/zxing/javase/1.6-SNAPSHOT/javase-1.6-SNAPSHOT.pom
rem #Dependencia con Barcode4j v 2.1 
call mvn install:install-file -Dfile=./net/sf/barcode4j/barcode4j-fop-ext-complete/2.1/barcode4j-fop-ext-complete-2.1.jar -DgroupId=net.sf.barcode4j -DartifactId=barcode4j-fop-ext-complete -Dversion=2.1 -Dpackaging=jar -DpomFile=./net/sf/barcode4j/barcode4j-fop-ext-complete/2.1/barcode4j-fop-ext-complete-2.1.pom