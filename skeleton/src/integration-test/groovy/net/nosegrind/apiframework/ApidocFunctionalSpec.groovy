package net.nosegrind.apiframework

import geb.spock.*
import grails.gorm.transactions.*
import grails.testing.mixin.integration.Integration
import grails.util.Holders
import grails.util.Metadata
import groovy.json.JsonSlurper
import net.nosegrind.apiframework.ApiCacheService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import spock.lang.*

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

/**
 * See http://www.gebish.org/manual/current/ for more instructions
 */

@Integration
@Rollback
class ApidocFunctionalSpec extends Specification {

    @Autowired
    ApplicationContext applicationContext

    @Shared String token
    @Shared String guestToken
    @Shared List authorities = ['permitAll']
    @Shared String controller = 'apidoc'
    @Shared String testDomain = 'http://localhost:8080'
    @Shared String currentId
    @Shared String guestId
    @Shared String appVersion = "v${Metadata.current.getProperty(Metadata.APPLICATION_VERSION, String.class)}"

    void "login and get token"(){
        setup:"logging in"
            String login = Holders.grailsApplication.config.root.login
            String password = Holders.grailsApplication.config.root.password
            String loginUri = Holders.grailsApplication.config.grails.plugin.springsecurity.rest.login.endpointUrl

            String url = "curl -H 'Content-Type: application/json' -X POST -d '{\"username\":\"${login}\",\"password\":\"${password}\"}' ${this.testDomain}${loginUri}"
            def proc = ['bash','-c',url].execute()
            proc.waitFor()
            def info = new JsonSlurper().parseText(proc.text)

        when:"set token"
            this.token = info.access_token
            info.authorities.each(){ it ->
                this.authorities.add(it)
            }
        then:"has bearer token"
            assert info.token_type == 'Bearer'
    }

    // create using mockdata
    void "CREATE guest id call"() {
        setup:"api is called"
            String METHOD = "POST"
            String action = 'create'
            String data = "{'username': 'guesttest','password':'testamundo','email':'guest@guesttest.com'}"
            def info
            def proc = ["curl", "-H", "Content-Type: application/json", "-H", "Authorization: Bearer ${this.token}", "--request", "POST", "-d", "${data}", "${this.testDomain}/${this.appVersion}/person/create"].execute()
            proc.waitFor()
            def outputStream = new StringBuffer()
            proc.waitForProcessOutput(outputStream, System.err)
            String output = outputStream.toString()
            info = new JsonSlurper().parseText(output)
        when:"info is not null"
            this.guestId = info['id']
            assert info!=null
        then:"created user"
            assert info['id'] != null

    }

    void "GUEST login and get token"(){
        setup:"logging in"
            String login = Holders.grailsApplication.config.root.login
            String password = Holders.grailsApplication.config.root.password
            String loginUri = Holders.grailsApplication.config.grails.plugin.springsecurity.rest.login.endpointUrl

            String url = "curl -H 'Content-Type: application/json' -X POST -d '{\"username\":\"guesttest\",\"password\":\"testamundo\"}' ${this.testDomain}${loginUri}"
            def proc = ['bash','-c',url].execute();
            proc.waitFor()
            def info = new JsonSlurper().parseText(proc.text)

        when:"set token"
            this.guestToken = info.access_token
        then:"has bearer token"
            assert info.token_type == 'Bearer'
    }

    void "GET admin apidoc"() {
        setup:"apidoc is called"
            String METHOD = "GET"
            String action = 'show'
            def info = [:]
            def proc = ["curl","-H","Content-Type: application/json","-H","Authorization: Bearer ${this.token}","--request","${METHOD}","${this.testDomain}/${this.appVersion}/${this.controller}/show"].execute()
            proc.waitFor()
            def outputStream = new StringBuffer()
            proc.waitForProcessOutput(outputStream, System.err)
            String output = outputStream.toString()
            def slurper = new JsonSlurper()
            slurper.parseText(output).each(){ k,v ->
                info[k] = v
            }
        when:"info is not null"
            assert info!=[:]
        then:"get user"
            assert info['apidoc']['stat']['show']!=[:]
    }

    void "GET guest apidoc"() {
        setup:"apidoc is called"
            String METHOD = "GET"
            String action = 'show'
            def info = [:]
            def proc = ["curl","-H","Content-Type: application/json","-H","Authorization: Bearer ${this.guestToken}","--request","${METHOD}","${this.testDomain}/${this.appVersion}/${this.controller}/show"].execute()
            proc.waitFor()
            def outputStream = new StringBuffer()
            proc.waitForProcessOutput(outputStream, System.err)
            String output = outputStream.toString()

            def slurper = new JsonSlurper()
            slurper.parseText(output).each(){ k,v ->
                info[k] = v
            }
        when:"info is not null"
            assert info!=[:]
        then:"get user"
            assert info['apidoc']['stat']==null
    }

    // create using mockdata
    void "DELETE guest id:"() {
        setup:"api is called"
            String METHOD = "DELETE"
            LinkedHashMap info = [:]
            def proc = ["curl","-H","Content-Type: application/json","-H","Authorization: Bearer ${this.token}","--request","${METHOD}","${this.testDomain}/${this.appVersion}/person/delete?id=${this.guestId}"].execute()
            proc.waitFor()
            def outputStream = new StringBuffer()
            proc.waitForProcessOutput(outputStream, System.err)
            String output = outputStream.toString()

            info = new JsonSlurper().parseText(output)
        when:"info is not null"
            assert info!=null
        then:"delete created user"
            assert this.guestId == info.id
    }



}

