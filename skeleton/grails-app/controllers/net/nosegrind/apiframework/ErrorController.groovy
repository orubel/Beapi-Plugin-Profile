package net.nosegrind.apiframework



class ErrorController {

    def index() {
        def error = request.exception?.cause?.message
        def status = (response?.status)? response?.status: id
        response.status = status
        render( status: status, text: "${error}")
    }
}
