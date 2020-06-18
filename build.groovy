// static params, they maybe will be changed when i'll use the script with versionning.
containerName = "minecraft"
imageName = "minecraft"
imageTag = "1.4"
serversRootPath = "/var/opt/minecraft_servers"
newMapTag = "new"

def isContainerRunning() {
	return sh(script : "sudo docker inspect $containerName", returnStatus: true, returnStdout: true)
}

def stopServerIfItRun() {
	def isRunning = isContainerRunning()
    if (isRunning == 0) {
        echo "killing last running container."
        try {
        sh (script: '''
        sudo docker exec minecraft screen -S minecraft -X stuff "stop\n"
        ''')
        } catch (Exception) {
            echo "No Minecraft server is running in the container."
        }
    } 
    else {
        echo "There is no container 'minecraft'."
    }
}


/**
 * Check existence of the last remaining container named $containerName, and kill it.
 *
 * 
 **/
def killLastContainer() {
    def isRunning = isContainerRunning()
    if (isRunning == 0) {
        try {
            sh "sudo docker kill minecraft"
        } 
        catch (Exception) {
            echo "A problem occured when killing the container."
        }
    } 
    else {
        echo "There is no container 'minecraft'."
    }
}

def runDetachedContainer(map) {
    sh "sudo docker run -t -d --rm -p 25565:25565 -v $serversRootPath/$map:/home/minecraft/world --name $containerName $imageName:$imageTag"
}

def launchServer() {
	def isRunning = isContainerRunning()
    if (isRunning == 0) {
        echo "running server"
        try {
        sh '''
		sudo docker exec minecraft screen -Smd minecraft java -Xmx2048M -Xms2048M -jar minecraft_server.jar nogui
		'''
        } catch (Exception) {
            echo "A problem occured when trying to launch the server"
        }
    } 
    else {
        echo "There is no container 'minecraft'."
    }	
}

return this