package com.ullink.gradle.extentreport

import com.ullink.gradle.nunit.NUnit
import com.ullink.gradle.opencover.OpenCover
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class ExtentReportDotnetPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        Task extentReportDownloaderTask = project.task('reportDownload', type: ReportGeneratorDownloader)
        Task reportingTask = project.task('nunitReport', type: ReportGenerator)

        reportingTask.dependsOn extentReportDownloaderTask

        project.afterEvaluate {
            project.tasks.withType(OpenCover).each { task ->
                task.finalizedBy(reportingTask)
            }
            project.tasks.withType(NUnit).each { task ->
                task.finalizedBy(reportingTask)
            }
        }
    }
}