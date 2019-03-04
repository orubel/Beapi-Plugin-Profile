package net.nosegrind.apiframework

class PersonController{
	
	def springSecurityService

	LinkedHashMap list() {
		if(isSuperuser()){
			def result = Person.list()
			return [person:result]
		}
	}

	java.lang.String value(){}

	LinkedHashMap create(){
		try{
			Person user = new Person(username:"${params.username}",password:"${params.password}",email:"${params.email}")
			if(user){
				if(!user.save(flush:true,failOnError:true)){
					user.errors.allErrors.each { println(it) }
				}
				return [person:user]
			}else{
				render(status: 500,text:"Id does not match record in database.")
			}
		}catch(Exception e){
			throw new Exception("[PersonController : get] : Exception - full stack trace follows:",e)
		}
	}

	LinkedHashMap show(){
		try{
			Person user = new Person()
			if(isSuperuser()){
				user = Person.get(params?.id?.toLong())
			}else{
				user = Person.get(springSecurityService.principal.id)
			}
			if(user){
				return [person: user]
			}else{
				render(status: 500,text:"Id does not match record in database.")
			}

		}catch(Exception e){
			throw new Exception("[PersonController : get] : Exception - full stack trace follows:",e)
		}
    }

	LinkedHashMap getByUsername(){
		try{
			Person user
			user = Person.get(params?.username)
			if(user){
				return [person: user]
			}else{
				render(status: 500,text:"Id does not match record in database.")
			}
			return [person: user]
		}catch(Exception e){
			throw new Exception("[PersonController : get] : Exception - full stack trace follows:",e)
		}
	}


	LinkedHashMap delete() {
		Person user
		try {
			user = Person.get(params.id)
			if(user){
				user.delete(flush: true, failOnError: true)
				return [person: [id:params.id.toLong()]]
			}else{
				render(status: 500,text:"Id " + params.id + " does not match record in database.")
			}
		}catch(Exception e){
			throw new Exception("[PersonController : delete] : Exception - full stack trace follows:",e)
		}
	}

	protected boolean isSuperuser() {
		springSecurityService.principal.authorities*.authority.any { grailsApplication.config.apitoolkit.admin.roles.contains(it) }
	}
}
