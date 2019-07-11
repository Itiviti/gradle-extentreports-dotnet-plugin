package com.ullink

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class ExtentReportDotnetPluginTest extends Specification{
    @Rule TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
            plugins {
                id 'base'
                id 'nunit'
                id 'com.ullink.opencover-nunit'
                id 'com.ullink.extentreports-dotnet'
            }
        """
    }

    def "nunitReport task is invoked once nunit task is finished"() {
        given:
            buildFile << """
                    nunit {
                        testAssemblies = ['-help']
                        nunitVersion = '3.9.0'
                    }
                """
        when:
            def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments( 'nunitReport')
                .withPluginClasspath()
                .withDebug(true)
                .build()
        then:
            def output = result.output
            result.output.contains('nunitReport')
    }

    def "nunitReport task is invoked once opencover task is finished"() {
        when:
             def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments( 'opencover')
                .withPluginClasspath()
                .withDebug(true)
                .build()
        then:
            result.output.contains('nunitReport')
    }
}
