package main.groovy.net.nosegrind.enumerator;

public enum Provider {
	GIT("Github"),
	FACE("Facebook"),
	LINK("LinkedIn"),
	TWIT("Twitter"),
	GOOG("Google Plus")

	private final String value

	Provider(String value){
	 this.value = value;
	}

	String toString() {
	 value
	}

	String getKey() {
	 name()
	}

	static list(){
		[GIT,FACE,LINK,TWIT,GOOG]
	}
}
