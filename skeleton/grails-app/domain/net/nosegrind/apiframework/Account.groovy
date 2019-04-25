package net.nosegrind.apiframework

class Account implements Serializable {

	static hasMany = [apps:Application]
	
	String acctName
	boolean enabled=true

	static constraints = {
		acctName blank: false, unique: true
	}

	static mapping = {
		cache true
	}

}
