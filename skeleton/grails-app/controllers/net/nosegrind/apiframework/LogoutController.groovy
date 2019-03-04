

import grails.plugin.springsecurity.SpringSecurityUtils



class LogoutController {

	static allowedMethods = [logout: 'POST']

	def index() {
		//println("logout index")
	}

	def logout() {
		//println("logging out...")
		redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/logoff'
	}
}
