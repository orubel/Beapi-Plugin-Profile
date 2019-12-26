#!/usr/bin/env groovy

Properties props = new Properties()
def propsFile = new File("/var/lib/jenkins/workspace/api-framework/gradle.properties")
props.load(propsFile.newDataInputStream())
def buildVersion = props.getProperty('buildVersion')
def patchVersion = props.getProperty('patchVersion')
def profileVersion = "${buildVersion}.${patchVersion}"

Properties props2 = new Properties()
def propsFile2 = new File("/var/lib/jenkins/workspace/beapi-profile/gradle.properties")
props2.load(propsFile2.newDataInputStream())
props2.setProperty('version', profileVersion)
props2.store(propsFile2.newWriter(), null)