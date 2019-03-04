package main.groovy.net.nosegrind.enumerator

public enum Status {
	 INACTIVE("INACTIVE"),
	 ACTIVE("ACTIVE"),
	 LOCKED("LOCKED"),
	 DELETED("DELETED"),
	 PENDING1("PENDING (Activation)"),
	 PENDING2("PENDING (Deactivation)")
	
	 private final String value
	
	 Status(String value){
	  this.value = value;
	 }
	
	 String toString() {
	  value
	 }
	
	 String getKey() {
	  name()
	 }
	
	 static list(){
		 [INACTIVE,ACTIVE,LOCKED,DELETED,PENDING1,PENDING2]
	 }
	 
	 public static Status fromString(String keyValue) {
		 for (wd in list()) {
			 if (wd.getKey().equals(keyValue))
			 	return wd
		 }
		 throw new IllegalArgumentException("There's no Status value with key " + keyValue)
	 }
}
