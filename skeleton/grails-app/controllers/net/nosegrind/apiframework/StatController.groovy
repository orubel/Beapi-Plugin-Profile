package net.nosegrind.apiframework

import java.sql.Timestamp
import java.text.SimpleDateFormat
import org.grails.core.DefaultGrailsDomainClass
import org.grails.core.artefact.DomainClassArtefactHandler

class StatController {
	
	def springSecurityService
	def statsService

	HashMap show() {
		Integer time = System.currentTimeMillis()
		Integer day = (Integer) time/((1000*60*60*24)+1)
		switch(params.type){
			case 3:
				// 12 months
				def year = getStatsByYear()
				println("thisyear")
				return [stats:year]
				break
			case 2:
				// 4 weeks / 1 month
				def week = getStatsByMonth()
				println("thismonth")
				return [stats:month]
				break
			case 1:
				// 7 days / 1 week
				def week = getStatsByWeek()
				println("thisweek")
				return [stats:week]
				break
			case 0:
			default:
				// 1 day
				def today = getStatsByDay()
				List hrs = ['1AM','2AM','3AM','4AM','5AM','6AM','7AM','8AM','9AM','10AM','11AM','12AM','1PM','2PM','3PM','4PM','5PM','6PM','7PM','8PM','9PM','10PM','11PM','12PM']
				LinkedHashMap apiTotals = ['1AM':0,'2AM':0,'3AM':0,'4AM':0,'5AM':0,'6AM':0,'7AM':0,'8AM':0,'9AM':0,'10AM':0,'11AM':0,'12AM':0,'1PM':0,'2PM':0,'3PM':0,'4PM':0,'5PM':0,'6PM':0,'7PM':0,'8PM':0,'9PM':0,'10PM':0,'11PM':0,'12PM':0]
				LinkedHashMap codeTotals = [:]
				LinkedHashMap userTotals = [:]

				today.each() { it ->
					def thisStat = formatDomainObject(it)

					Timestamp tstamp = new Timestamp(thisStat.timestamp)
					Date date = new Date(tstamp.getTime())
					SimpleDateFormat sdf = new SimpleDateFormat("kk")
					Integer hr = sdf.format(date).toInteger()

					String authority = springSecurityService.principal.authorities*.authority[0]

					if(apiTotals["${hrs[hr]}"]==null) {
						apiTotals["${hrs[hr]}"] = 1
					}else{
						apiTotals["${hrs[hr]}"] += 1
					}

					if(codeTotals["${thisStat.code}"]==null){
						codeTotals["${thisStat.code}"] = 1
					}else{
						codeTotals["${thisStat.code}"] += 1
					}

					Person user = Person.get(thisStat.user.toLong())
					if(userTotals["${user.username}"]==null){
						userTotals["${user.username}"] = 1
					}else{
						userTotals["${user.username}"] += 1
					}
				}

				return [stat:['apiTotals':apiTotals,'codeTotals':codeTotals,'userTotals':userTotals]]
		}
	}

	// First Day Of Week
	public long getFDOW() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
		c.set(Calendar.HOUR_OF_DAY, 0)
		c.set(Calendar.MINUTE, 0)
		c.set(Calendar.SECOND, 0)
		c.set(Calendar.MILLISECOND, 0)
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek())
		long fdow = c.getTimeInMillis() / 1000
		return fdow
	}

	// Last Day Of Week
	public long getLDOW() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
		c.set(Calendar.HOUR_OF_DAY, 0)
		c.set(Calendar.MINUTE, 0)
		c.set(Calendar.SECOND, 0)
		c.set(Calendar.MILLISECOND, 0)
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek())
		c.add(Calendar.DATE, +7)
		long ldow = c.getTimeInMillis() / 1000
		return ldow
	}

	public long getYesterday() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
		c.set(Calendar.HOUR_OF_DAY, 0)
		c.set(Calendar.MINUTE, 0)
		c.set(Calendar.SECOND, 0)
		c.set(Calendar.MILLISECOND, 0)
		long yesterday = c.getTimeInMillis() / 1000
		return yesterday
	}
	LinkedHashMap getStatsByWeek(){
		long fdow = getFDOW()
		long ldow = getLDOW()
		List stats = Stat.findByTimestampBetween(fdow, ldow)
		return stats
	}

	List getStatsByDay(){
		long yesterday = getYesterday()
		long today = yesterday - (1000*60*60*24)
		List stats = Stat.findAllByTimestampGreaterThanEquals(yesterday)
		return stats
	}

	LinkedHashMap formatDomainObject(Object data){
		LinkedHashMap newMap = [:]

		newMap.put('id', data?.id)
		newMap.put('version', data?.version)

		//DefaultGrailsDomainClass d = new DefaultGrailsDomainClass(data.class)

		DefaultGrailsDomainClass d = grailsApplication?.getArtefact(DomainClassArtefactHandler.TYPE, data.class.getName())

		if (d!=null) {
			// println("PP:"+d.persistentProperties)

				d?.persistentProperties?.each() { it2 ->
					if (it2?.name) {
						if (DomainClassArtefactHandler.isDomainClass(data[it2.name].getClass())) {
							newMap["${it2.name}Id"] = data[it2.name].id
						} else {
							newMap[it2.name] = data[it2.name]
						}
					}
				}

		}
		return newMap
	}
}
