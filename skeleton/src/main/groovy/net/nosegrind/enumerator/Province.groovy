package main.groovy.net.nosegrind.enumerator

public enum Province{
	
	AB("Alberta"),
	BC("British Columbia"),
	MB("Manitoba"),
	NB("New Brunswick"),
	NF("Newfoundland"),
	NT("Northwest Territories"),
	NS("Nova Scotia"),
	ON("Ontario"),
	PE("Prince Edward Island"),
	QC("Quebec"),
	SK("Saskatchewan"),
	YT("Yukon"),
	
	 private final String value
	
	 Province(String value){
	  this.value = value;
	 }
	
	 String toString() {
	  value
	 }
	
	 String getKey() {
	  name()
	 }
	
	 static list(){
		 [AB,BC,MB,NB,NF,NT,NS,ON,PE,QC,SK,YT]
	 }
	 
	 // Reverse-lookup map for getting a day from an abbreviation
	 private static final Map<String, Province> lookup = new HashMap<String, Province>();
	 static {
		 for (Province c : Province.values())
			 lookup.put(c.getValue(), c);
	 }

	 public String getValue() {
		 return value;
	 }

	 public static Province get(String value) {
		 return lookup.get(value);
	 }
	 
}
