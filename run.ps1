# Entry point script for development convenience
# This is a wrapper to run common Gradle commands

Write-Host "Negative Space Puzzler - Development Scripts"
Write-Host "============================================"
Write-Host ""

param(
    [Parameter(Position=0)]
    [ValidateSet("build", "test", "coverage", "lint", "apk", "clean", "install", "help")]
    [string]$Command = "help"
)

$GradleWrapper = if (Test-Path "./gradlew.bat") { "./gradlew.bat" } else { "./gradlew" }

switch ($Command) {
    "build" {
        Write-Host "Building project..."
        & $GradleWrapper build --no-daemon
    }
    "test" {
        Write-Host "Running unit tests..."
        & $GradleWrapper testDebugUnitTest --no-daemon
    }
    "coverage" {
        Write-Host "Generating coverage report..."
        & $GradleWrapper jacocoTestReport --no-daemon
        Write-Host "Report: app/build/reports/jacoco/jacocoTestReport/html/index.html"
    }
    "lint" {
        Write-Host "Running lint..."
        & $GradleWrapper lint --no-daemon
    }
    "apk" {
        Write-Host "Building debug APK..."
        & $GradleWrapper assembleDebug --no-daemon
        Write-Host "APK: app/build/outputs/apk/debug/app-debug.apk"
    }
    "clean" {
        Write-Host "Cleaning project..."
        & $GradleWrapper clean --no-daemon
    }
    "install" {
        Write-Host "Installing on connected device..."
        & $GradleWrapper installDebug --no-daemon
    }
    "help" {
        Write-Host "Available commands:"
        Write-Host "  build    - Build the project"
        Write-Host "  test     - Run unit tests"
        Write-Host "  coverage - Generate test coverage report"
        Write-Host "  lint     - Run lint checks"
        Write-Host "  apk      - Build debug APK"
        Write-Host "  clean    - Clean build artifacts"
        Write-Host "  install  - Install on connected device"
        Write-Host ""
        Write-Host "Usage: ./run.ps1 <command>"
    }
}
