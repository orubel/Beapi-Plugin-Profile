package net.nosegrind.apiframework


import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.*
import spock.lang.*
import geb.spock.*
import grails.util.Holders
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import net.nosegrind.apiframework.ApiCacheService
import grails.util.Metadata


/**
 * See http://www.gebish.org/manual/current/ for more instructions
 */

@Integration
@Rollback
class ApiFunctionalSpec extends Specification {

    @Autowired
    ApplicationContext applicationContext

    @Shared String token
    @Shared List authorities = ['permitAll']
    @Shared String controller = 'person'
    @Shared String testDomain = 'http://localhost:8080'
    @Shared String currentId
    @Shared String appVersion = "v${Metadata.current.getProperty(Metadata.APPLICATION_VERSION, String.class)}"

    void "login and get token"(){
        setup:"logging in"
            String login = Holders.grailsApplication.config.root.login
            String password = Holders.grailsApplication.config.root.password
            String loginUri = Holders.grailsApplication.config.grails.plugin.springsecurity.rest.login.endpointUrl

            String url = "curl -H 'Content-Type: application/json' -X POST -d '{\"username\":\"${login}\",\"password\":\"${password}\"}' ${this.testDomain}${loginUri}"
            def proc = ['bash','-c',url].execute();
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
    void "CREATE api call"() {
        setup:"api is called"
            String METHOD = "POST"

            ApiCacheService apiCacheService = applicationContext.getBean("apiCacheService")
            LinkedHashMap cache = apiCacheService.getApiCache(this.controller)
            Integer version = cache['cacheversion']

            String action = 'create'
            String data = "{"
            cache?."${version}"?."${action}".receives.each(){ k,v ->
                v.each(){
                    data += "'"+it.name+"': '"+it.mockData+"',"
                }
            }
            data += "}"

            def info

            def proc = ["curl", "-H", "Content-Type: application/json", "-H", "Authorization: Bearer ${this.token}", "--request", "${METHOD}", "-d", "${data}", "${this.testDomain}/${this.appVersion}/${this.controller}/${action}"].execute();

            proc.waitFor()
            def outputStream = new StringBuffer()
            proc.waitForProcessOutput(outputStream, System.err)
            String output = outputStream.toString()


            info = new JsonSlurper().parseText(output)

        when:"info is not null"
            assert info!=null
        then:"created user"
            cache?."${version}"?."${action}".returns.each(){ k,v ->
                assert this.authorities.contains(k)
                v.each(){
                    if(it.keyType=='PRIMARY'){
                        this.currentId = info."${it.name}"
                    }
                    assert info."${it.name}" != null
                }
            }
    }

    void "GET api call: [domain object]"() {
        setup:"api is called"
            String METHOD = "GET"
            LinkedHashMap info = [:]
            ApiCacheService apiCacheService = applicationContext.getBean("apiCacheService")
            LinkedHashMap cache = apiCacheService.getApiCache(this.controller)

            Integer version = cache['cacheversion']
            String action = 'show'
            String pkey = cache?."${version}"?."${action}".pkey[0]

            def proc = ["curl","-H","Content-Type: application/json","-H","Authorization: Bearer ${this.token}","--request","${METHOD}","${this.testDomain}/${this.appVersion}/${this.controller}/show?id=${this.currentId}"].execute();
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
            cache?."${version}"?."${action}".returns.each(){ k,v ->
                assert this.authorities.contains(k)
                v.each(){ it ->
                    assert info."${it.name}" != null
                }
            }
    }

    // test list of domain objects
    void "GET list api call: [list of domain objects]"() {
        setup:"api is called"
            String METHOD = "GET"
            LinkedHashMap info = [:]
            ApiCacheService apiCacheService = applicationContext.getBean("apiCacheService")
            LinkedHashMap cache = apiCacheService.getApiCache(this.controller)

            Integer version = cache['cacheversion']

            String action = 'show'

            String pkey = cache?."${version}"?."${action}".pkey[0]

            def proc = ["curl","-H","Content-Type: application/json","-H","Authorization: Bearer ${this.token}","--request","${METHOD}","${this.testDomain}/${this.appVersion}/${this.controller}/list"].execute();
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
            assert info.size()==Person.count()
    }

    // create using mockdata
    void "DELETE api call: [map]"() {
        setup:"api is called"
            String METHOD = "DELETE"
            LinkedHashMap info = [:]
            ApiCacheService apiCacheService = applicationContext.getBean("apiCacheService")
            LinkedHashMap cache = apiCacheService.getApiCache(this.controller)

            Integer version = cache['cacheversion']

            String action = 'delete'
            def proc = ["curl","-H","Content-Type: application/json","-H","Authorization: Bearer ${this.token}","--request","${METHOD}","${this.testDomain}/${this.appVersion}/${this.controller}/delete?id=${this.currentId}"].execute();
            proc.waitFor()
            def outputStream = new StringBuffer()
            proc.waitForProcessOutput(outputStream, System.err)
            String output = outputStream.toString()

            def slurper = new JsonSlurper()
            slurper.parseText(output).each(){ k,v ->
                info[k] = v

            }
        when:"info is not null"
            assert info!=null
        then:"delete created user"
            def id
            cache?."${version}"?."${action}".returns.each(){ k,v ->
                v.each(){ it ->
                    if(it.keyType=='PRIMARY'){
                        id = info."${it.name}"
                    }

                }
            }
            assert this.currentId == id
    }



}

