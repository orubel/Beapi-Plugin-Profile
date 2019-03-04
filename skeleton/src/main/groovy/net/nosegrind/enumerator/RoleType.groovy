package main.groovy.net.nosegrind.enumerator

public enum RoleType {
	ROOT("SuperUser"),
	ADMIN("Administrator"),
	 TECH("Technical"),
	 MARK("Marketing"),
	 SALES("Sales"),
	 SUPP("Support")
	
	 private final String value
	
	 RoleType(String value){
	  this.value = value;
	 }
	
	 String toString() {
	  value
	 }
	
	 String getKey() {
	  name()
	 }
	
	 static list(){
		 [ROOT,ADMIN,TECH,MARK,SALES,SUPP]
	 }
}
