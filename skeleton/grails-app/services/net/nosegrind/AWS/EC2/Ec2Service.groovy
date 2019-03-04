package net.nosegrind.AWS.EC2

import groovy.json.JsonSlurper

class Ec2Service {

    def grailsApplication

    static transactional = false

    int stopInstance() {
        // wait til StoppingInstances.CurrentState.Code == 80
        String awsId = grailsApplication.config.perf.awsId
        println("awsid :"+awsId)
        int code = 0

        while(code!=80) {
            println("code not equal to 80")
            String command = "aws ec2 stop-instances --instance-ids ${awsId}"
            def proc = ['bash', '-c', command].execute()
            proc.waitFor()
            def info = new JsonSlurper().parseText(proc.text)
            code = info.StoppingInstances[0].CurrentState.Code as int
            Thread.sleep(10 * 1000)
        }

        return code
    }

    int startInstance() {
        // wait til StartingInstances.CurrentState.Code == 16
        String awsId = grailsApplication.config.perf.awsId
        int code

        while(code!=16) {
            String command = "aws ec2 start-instances --instance-ids ${awsId}"
            def proc = ['bash', '-c', command].execute()
            proc.waitFor()
            def info = new JsonSlurper().parseText(proc.text)
            code = info.StartingInstances[0].CurrentState.Code as int
            Thread.sleep(10 * 1000)
        }

        return code
    }

    // INSTANCES
    void describeInstanceAttribute(){}
    void describeInstanceStatus(){}
    void describeInstances(){}
    void associateIamInstanceProfile(){}
    void bundleInstance(){}
    void confirmProductInstance(){}
    void disassociateIamInstanceProfile(){}
    void unmonitorInstances(){}
    void runInstances(){}
    void terminateInstances(){}
    void reportInstanceStatus(){}
    void replaceIamInstanceProfileAssociation(){}
    void modifyInstanceAttribute(){}
    void modifyInstancePlacement(){}
    void monitorInstances(){}
    void rebootInstances(){}
    void resetInstanceAttribute(){}
    void describeIamInstanceProfileAssociations(){}


    // SCHEDULED INSTANCE
    void describeScheduledInstanceAvailability(){}
    void describeScheduledInstances(){}
    void runScheduledInstances(){}
    void purchaseScheduledInstances(){}

    // RESERVED INSTANCES
    void describeReservedInstances(){}
    void describeReservedInstancesListings(){}
    void createReservedInstancesListing(){}
    void cancelReservedInstancesListing(){}
    void acceptReservedInstancesExchangeQuote(){}
    void getReservedInstancesExchangeQuote(){}
    void describeReservedInstancesModifications(){}
    void describeReservedInstancesOfferings(){}
    void purchaseReservedInstancesOffering(){}
    void modifyReservedInstances(){}

    // SPOT INSTANCES
    void createSpotDatafeedSubscription(){}
    void deleteSpotDatafeedSubscription(){}
    void describeSpotDatafeedSubscription(){}
    void describeSpotFleetRequestHistory(){}
    void describeSpotFleetRequests(){}
    void describeSpotPriceHistory(){}
    void modifySpotFleetRequest(){}
    void cancelSpotFleetRequests(){}
    void describeSpotFleetInstances(){}
    void requestSpotFleet(){}
    void requestSpotInstances(){}
    void cancelSpotInstanceRequests(){}
    void describeSpotInstanceRequests(){}

}
