import net.nosegrind.apiframework.StatsService
import net.nosegrind.apiframework.Stat
import grails.util.Environment


class StatsSchedJob {

    def statsService

    static triggers = {
		if (Environment.current != Environment.TEST) {
			cron name: 'myTrigger2', cronExpression: "0 */10 * ? * *"
		}
    }

    def execute() {
		def temp = statsService.getStatsCache()

		temp.each(){ it ->
			//int user = it[0]
			//int code = it[1]
			//String uri = it[2]
			//BigInteger timestamp = it[3]
			Stat st = new Stat(user:it[0],code:it[1],uri:"${it[2]}",timestamp:it[3])
			if(!st.save(flush:true,failOnError:true)){
				st.errors.allErrors.each { println(it2) }
			}
		}
		statsService.flushAllStatsCache()
    }

}
