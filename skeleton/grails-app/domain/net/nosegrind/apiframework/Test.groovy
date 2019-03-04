package net.nosegrind.apiframework

class Test implements Serializable {

	String name

	static constraints = {
		name blank: false
	}

	static mapping = {
		cache false
	}

}
