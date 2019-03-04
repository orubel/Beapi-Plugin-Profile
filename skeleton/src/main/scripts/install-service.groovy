import grails.util.BuildSettings
import groovy.util.AntBuilder

description "Installation Script for BeAPI", "grails install-service"

println "#### Installing and Configuring BeApi..."

initInstall()

void initInstall(){
    println "### Installing Local Directory ..."

    String basedir = BuildSettings.BASE_DIR
    def ant = new AntBuilder()
    ant.mkdir(dir: "/home/beapi/.beapi")
    // move YML/Groovy config
    installConfigFiles()
}

void installConfigFiles() {
    println "### Installing Local Config Files ..."

    def ant = new AntBuilder()
    //String basedir = applicationContext.getResource("../../..").getFile().path
    String basedir = BuildSettings.BASE_DIR

    //def yml = "${basedir}/grails-app/conf/beapi.yml"
    //def groovy = "${basedir}/grails-app/conf/beapi.groovy"

    File ymlFile = new File("${basedir}/grails-app/conf/templates/xConf/beapi.yml")
    File groovyFile = new File("${basedir}/grails-app/conf/templates/xConf/beapi.groovy")

    File newDir = new File("/home/beapi/.beapi/")
    boolean fileMoved1 = ymlFile.renameTo(new File(newDir, ymlFile.getName()))
    ant.exec(executable: 'chown') {
        arg(value: 'beapi')
        arg(file: "${newDir}beapi.yml")
    }

    boolean fileMoved2 = groovyFile.renameTo(new File(newDir, groovyFile.getName()))
    ant.exec(executable: 'chown') {
        arg(value: 'beapi')
        arg(file: "${newDir}beapi.groovy")
    }

    installDaemon()
}

void installDaemon(){
    println "### Installing Daemon ..."
    def ant = new AntBuilder()
    String basedir = BuildSettings.BASE_DIR

    File daemonFile = new File("${baseDir}beapi_backend")

    File newDir = new File("/etc/init.d/")
    boolean fileMoved = daemonFile.renameTo(new File(newDir, daemonFile.getName()))
}

void installStartupScript(){
    println "### Installing Service ..."
    def ant = new AntBuilder()
    String basedir = BuildSettings.BASE_DIR

    File serviceFile = new File("${baseDir}beapi_backend.sh")

    File newDir = new File("/home/orp/")
    boolean fileMoved = serviceFile.renameTo(new File(newDir, serviceFile.getName()))
    ant.exec(executable: 'chown') {
        arg(value: 'beapi')
        arg(file: "${newDir}beapi_backend.sh")
    }
}