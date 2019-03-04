package net.nosegrind.apiframework

class AcctPerson implements Serializable {
	static belongsTo = [acct:Account,person:Person]

	//Account account
	//Person person
	boolean owner=false

	static constraints = {
		acct blank: false
		person blank: false
		owner blank:false, validator: { val, obj ->
			if (val == true) {
				AcctPerson acctPerson = AcctPerson.findByAcctAndOwner(obj.acct, val)
				if (acctPerson) {
					return '[APPLICATION OWNER EXISTS]'
				}
			}
		}
	}

	static mapping = {
		cache true
		acct lazy:false
		person lazy:false
	}

}
