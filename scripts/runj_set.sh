CURRVER='1.2'
export CURRVER
echo CURRVER

CURRPROG='linkingserver-'$CURRVER'-jar-with-dependencies.jar'
export CURRPROG
export CURRPROG

JAVA_RUN='java -cp target/'$CURRPROG 
export JAVA_RUN
echo JAVA_RUN

alias runj=$JAVA_RUN' edu.uprm.capstone.areatech.linkingserver.ClientManager'

NUMBER='7879992020'
export NUMBER
echo NUMBER

HOST='localhost'
export HOST
echo HOST

PORT='6189'
export PORT
echo PORT

RQT_D='NONE'
export RQT_D
echo RQT_D

DIA_D='DIAG'
export DIA_D
echo DIA_D

alias runADiag=$JAVA_RUN' edu.uprm.capstone.areatech.linkingserver.connection.client.application.TestNativeClient '$HOST' '$PORT' A '$NUMBER' DIA '$DIA_D

alias runDDiag=$JAVA_RUN' edu.uprm.capstone.areatech.linkingserver.connection.client.application.TestNativeClient '$HOST' '$PORT' D '$NUMBER' DIA '$DIA_D

alias runARQT=$JAVA_RUN' edu.uprm.capstone.areatech.linkingserver.connection.client.application.TestNativeClient '$HOST' '$PORT' A '$NUMBER' RQT '$RQT_D

alias runDRQT=$JAVA_RUN' edu.uprm.capstone.areatech.linkingserver.connection.client.application.TestNativeClient '$HOST' '$PORT' D '$NUMBER' RQT '$RQT_D