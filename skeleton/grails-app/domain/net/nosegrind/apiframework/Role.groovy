package net.nosegrind.apiframework

//@ToString(includeNames = true, includeFields = true)
class Role{

	static transactional = true

	String authority

	static constraints = {
		authority blank: false, unique: true
	}

	static mapping = {
		//datasource 'user'
		cache true
	}

}
