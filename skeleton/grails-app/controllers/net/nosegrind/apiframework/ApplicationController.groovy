package net.nosegrind.apiframework

import grails.plugin.springsecurity.rest.token.AccessToken
import org.springframework.security.core.userdetails.UserDetails

class ApplicationController {
	
	def springSecurityService

	LinkedHashMap list() {
		Account acct = Account.get(params?.acct?.toLong())
		if (acct) {
			def result = Application.findAllByAcct(acct)
			return [application: result]
		}
	}

	LinkedHashMap create(){
		AcctPerson acctPerson = AcctPerson.getByPersonAndOwner(springSecurityService.principal.id, true)
		if(acctPerson) {
			Application.withTransaction { status ->
				Application app = new Application(appName: "${params.appName}", tokenValue: "${params.password}", enabled: true)
				if (!app.save(flush: true, failOnError: true)) {
					app.errors.allErrors.each { log.error it }
				}

				if (app.active) {
					AccessToken accessToken = tokenGenerator.generateAccessToken(springSecurityService.principal as UserDetails)
					AppToken appToken = new AppToken(app: app, tokenValue: accessToken.accessToken)
					return [application:app]
				} else {
					status.setRollbackOnly()
				}
			}
		}else{
			render(status: 400,text:"ACCOUNT NOT FOUND: NO ACCOUNT ASSOCIATED WITH CURRENT USER.")
		}
	}

	LinkedHashMap show(){
		try{
			Application app = Application.get(params?.id?.toLong())
			Person person = Person.get(springSecurityService.principal.id)
			AcctPerson acctPerson = AcctPerson.findByAcctAndPerson(app.acct,person)
			if(acctPerson) {
				return [application: app]
			}
		}catch(Exception e){
			//println("#[PersonController : get] : Exception - full stack trace follows:"+e)
			throw new Exception("[ApplicationController : get] : Exception - full stack trace follows:",e)
		}
    }


	LinkedHashMap delete() {
		try {
			Application app
			if(isSuperuser()) {
				app = Application.get(params?.id?.toLong())
			}else{
				app = Application.get(springSecurityService.principal.id)
			}
			app.delete(flush: true, failOnError: true)
			return [application: [id: params.id.toLong()]]
		}catch(Exception e){
			throw new Exception("#[ApplicationController : delete] : Exception - full stack trace follows:",e)
		}
	}

	protected boolean isSuperuser() {
		springSecurityService.principal.authorities*.authority.any { grailsApplication.config.apitoolkit.admin.roles.contains(it) }
	}
}
