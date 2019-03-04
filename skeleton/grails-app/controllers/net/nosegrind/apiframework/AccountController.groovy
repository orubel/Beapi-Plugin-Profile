package net.nosegrind.apiframework


class AccountController {
	
	def springSecurityService


	LinkedHashMap create(){
		try{
			Account.withTransaction { status ->
				Account acct = new Account(acctName: "${params.acctName}", enabled: true)
				if (!acct.save(flush: true, failOnError: true)) {
					status.setRollbackOnly()
				}else{
					AcctPerson.withTransaction { status2 ->
						AcctPerson acctPerson = new AcctPerson(acct: acct, person: springSecurityService.principal.id, owner: true)
						if(!acctPerson.save(flush: true, failOnError: true)){
							status2.setRollbackOnly()
						}else{
							return [account: acct]
						}
					}
				}
			}
		}catch(Exception e){
			throw new Exception("[AccountController : get] : Exception - full stack trace follows:",e)
		}
	}

	LinkedHashMap get(){
		try{
			Account acct = Account.get(params?.id?.toLong())
			return [account: acct]
		}catch(Exception e){
			throw new Exception("[AccountController : get] : Exception - full stack trace follows:",e)
		}
    }

	LinkedHashMap delete() {
		try {
			Account acct = Account.get(params.id?.toLong())
			acct.delete(flush: true, failOnError: true)
			return [account: [id: params.id.toLong()]]
		}catch(Exception e){
			throw new Exception("[AccountController : delete] : Exception - full stack trace follows:",e)
		}
	}

	// TODO : give account ownership to someone else
	// def changeOwner(){}

	// TODO : add acct user
	// def createAcctUser(){}

	protected boolean isSuperuser() {
		springSecurityService.principal.authorities*.authority.any { grailsAccount.config.apitoolkit.admin.roles.contains(it) }
	}

}
