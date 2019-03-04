package main.groovy.net.nosegrind.enumerator

public enum State{

AL("Alabama"),
AK("Alaska"),
AS("American Samoa"),
AZ("Arizona"),
AR("Arkansas"),
CA("California"),
CO("Colorado"),
CT("Connecticut"),
DE("Delaware"),
DC("District of Columbia"),
FM("Fed. States of Micronesia"),
FL("Florida"),
GA("Georgia"),
GU("Guam"),
HI("Hawaii"),
ID("Idaho"),
IL("Illinois"),
IN("Indiana"),
IA("Iowa"),
KS("Kansas"),
KY("Kentucky"),
LA("Louisiana"),
ME("Maine"),
MH("Marshall Islands"),
MD("Maryland"),
MA("Massachusetts"),
MI("Michigan"),
MN("Minnesota"),
MS("Mississippi"),
MO("Missouri"),
MT("Montana"),
NE("Nebraska"),
NV("Nevada"),
NH("New Hampshire"),
NJ("New Jersey"),
NM("New Mexico"),
NY("New York"),
NC("North Carolina"),
ND("North Dakota"),
MP("Northern Mariana Is."),
OH("Ohio"),
OK("Oklahoma"),
OR("Oregon"),
PW("Palau"),
PA("Pennsylvania"),
PR("Puerto Rico"),
RI("Rhode Island"),
SC("South Carolina"),
SD("South Dakota"),
TN("Tennessee"),
TX("Texas"),
UT("Utah"),
VT("Vermont"),
VA("Virginia"),
VI("Virgin Islands"),
WA("Washington"),
WV("West Virginia"),
WI("Wisconsin"),
WY("Wyoming")
	
	 private final String value
	
	 State(String value){
	  this.value = value;
	 }
	
	 String toString() {
	  value
	 }
	
	 String getKey() {
	  name()
	 }
	
	 static list(){
		 [AL,AK,AS,AZ,AR,CA,CO,CT,DE,DC,FM,FL,GA,GU,HI,ID,IL,IN,IA,KS,KY,LA,ME,MH,MD,MA,MI,MN,MS,MO,MT,NE,NV,NH,NJ,NM,NY,NC,ND,MP,OH,OK,OR,PW,PA,PR,RI,SC,SD,TN,TX,UT,VT,VA,VI,WA,WV,WI,WY]
	 }
	 
	 // Reverse-lookup map for getting a day from an abbreviation
	 private static final Map<String, State> lookup = new HashMap<String, State>();
	 static {
		 for (State s : State.values()){
			 lookup.put(s.getValue(), s)
		 }
	 }

	 public String getValue() {
		 return value;
	 }

	 public static State get(String value) {
		 return lookup.get(value);
	 }
	 
}
