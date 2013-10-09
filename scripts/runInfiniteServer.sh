for (( i=0 ; i <= 999 ; i++ ))
do
    java -cp target/linkingserver-1.2-jar-with-dependencies.jar edu.uprm.capstone.areatech.linkingserver.ClientManager 6189
    wait $!
done