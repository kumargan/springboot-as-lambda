#!bin bash
#First Command line param to the script
debug=$1 
javapath=`which java`
export SPRING_PROFILES_ACTIVE=local
export ENV=local
export postgreDBUser=postgres
export postgreDBPwd=postgres

if [ -z != $javapath ] 
	then
		echo "java already installed"
	else
		echo " install java and continue, exiting "
		exit
fi

mavenpath=`which mvn`

if [ -z != $mavenpath ]
	then
		echo "maven already installed"
	else
		echo " install maven and continue, exiting "
		exit
	fi
	
stop_old_server(){
	echo "stopping old server"
	file="process.pid"
	if [ -f "$file" ]
	then
		kill `cat process.pid`
	fi
	cp /dev/null process.pid
}

rm -rf target
mvn package > build.logs

if [ $? -eq 0 ]; then
	echo "packaging successful "
	echo "**********************************************************************************"

	if [ "$debug" = 'test' ];
    	then
    		echo "running tests........."
    		rm -rf build
    		mvn test
	elif [ "$debug" = 'debug' ];
	then
		stop_old_server
		echo "starting server in debug mode on port 60001"
		java -Xmx1024M -Xms1024M -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=60001,suspend=n \
       -jar target/sample-1.0.jar --server.port=8085 1>>build.logs 2>>build.logs &
	else
		stop_old_server
		echo "starting server "
		java -Xmx3024M -Xms1024M -jar target/sample-1.0.jar --server.port=8085 1>>build.logs 2>>build.logs &
	fi
	echo $! >> process.pid
else
	echo "packaging failed, check build.logs"
fi

