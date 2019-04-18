package net.nosegrind.apiframework

class Stat {

	Long user
	Integer code
	String uri
	Long timestamp

	static constraints = {
		user nullable:false
		code nullable:false
		uri nullable:false
		timestamp nullable:false
	}

	static mapping = {
		timestamp sqlType: "bigint"
	}
}
