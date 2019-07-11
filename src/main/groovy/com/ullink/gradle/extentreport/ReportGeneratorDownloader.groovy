package com.ullink.gradle.extentreport

import de.undercouch.gradle.tasks.download.Download

class ReportGeneratorDownloader extends Download {

    def EXTENT_REPORT_DOWNLOAD_URL = 'https://www.nuget.org/api/v2/package/extent/0.0.3'
    def EXTENT_REPORT_DOWNLOADED_FILE_NAME = 'extentReport.zip'
    def EXTENT_REPORT_EXECUTABLE_PATH = 'tools/extent.exe'


    @Override
    void download() {
        ensureExtentReportIsAvailable()
    }

    void ensureExtentReportIsAvailable() {
        if (!isExtentReportAvailable()) {
            project.logger.info "Downloading extent report..."

            getCacheDirForExtentReport().mkdirs()
            downloadExtentReport()
        } else {
            project.logger.info "Extent Report already available in the local cache : ${getCacheDirForExtentReport()}"
        }
    }

    boolean isExtentReportAvailable() {
        def extentReportCacheDir = getCacheDirForExtentReport()
        if (!extentReportCacheDir.exists()) {
            return false
        }

        def extentReportExecutable = new File(extentReportCacheDir, EXTENT_REPORT_EXECUTABLE_PATH)
        return extentReportExecutable.exists()
    }

    File getCacheDirForExtentReport() {
        new File(new File(project.gradle.gradleUserHomeDir, 'caches'), 'extent-report')
    }

    void downloadExtentReport() {
        def downloadedFile = new File(getTemporaryDir(), EXTENT_REPORT_DOWNLOADED_FILE_NAME)
        def zipOutputDir = getCacheDirForExtentReport()

        project.logger.info "Downloading & Unpacking Extent Report from ${EXTENT_REPORT_DOWNLOAD_URL}"

        src "$EXTENT_REPORT_DOWNLOAD_URL"
        dest downloadedFile
        super.download()

        project.copy {
            from project.zipTree(downloadedFile)
            into zipOutputDir
        }
    }
}
