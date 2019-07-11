package com.ullink.gradle.extentreport

import com.ullink.gradle.nunit.NUnit
import org.gradle.api.tasks.Exec

class ReportGenerator extends Exec {

    def ExtentReportType = 'v3html'
    def ExtentReportExecutablePath = 'tools/extent.exe'

    @Override
    protected void exec() {
        if (isTestResultFileAvailable()) {
            project.logger.info("Generating the report for the NUnit Test Results..")

            generateReports()
        } else {
            project.logger.info("There is no available Test Result file based on which the report should be generated located at ${project.tasks.nunit.getTestReportPath()}")
        }
    }

    boolean isTestResultFileAvailable() {
        return project.tasks.nunit.getTestReportPath().exists()
    }

    File getCacheDirForExtentReport() {
        new File(new File(project.gradle.gradleUserHomeDir, 'caches'), 'extent-report')
    }

    private def generateReports() {
        NUnit nunit = project.tasks.nunit

        project.exec {
            commandLine = buildCommandForExtentReport(nunit.getTestReportPath(), nunit.getReportFolder())
        }

        def resultFile = new File(nunit.getReportFolder(), "index.html")
        resultFile.renameTo new File(nunit.getReportFolder(), 'TestResult.html')
    }

    def buildCommandForExtentReport(def testResultPath, def outputFolder) {
        return [getExtentReportExeFile().absolutePath, "-i", testResultPath, "-o", outputFolder, "-r", ExtentReportType]
    }

    File getExtentReportExeFile() {
        def extentReportFolder = getCacheDirForExtentReport()
        return new File(extentReportFolder, ExtentReportExecutablePath)
    }
}
