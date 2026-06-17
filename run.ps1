#Requires -Version 5.1

$ErrorActionPreference = "Stop"

function Confirm-Install {
    param([string]$Name)

    $answer = Read-Host "$Name is missing. Install it? (y/N)"

    if ($answer -notmatch '^[Yy]$') {
        throw "$Name is required."
    }
}

function Ensure-Winget {
    if (-not (Get-Command winget -ErrorAction SilentlyContinue)) {
        throw "winget is not installed. Please install App Installer from the Microsoft Store."
    }
}

function Refresh-Environment {
    $machinePath = [Environment]::GetEnvironmentVariable("Path", "Machine")
    $userPath = [Environment]::GetEnvironmentVariable("Path", "User")

    $env:Path = "$machinePath;$userPath"

    foreach ($scope in "Machine", "User") {
        [Environment]::GetEnvironmentVariables($scope).GetEnumerator() | ForEach-Object {
            try {
                Set-Item -Path "Env:$($_.Key)" -Value $_.Value -ErrorAction SilentlyContinue
            }
            catch {}
        }
    }
}

function Get-JavaMajorVersion {
    try {
        $output = & java -version 2>&1

        foreach ($line in $output) {
            if ($line -match '"(\d+)(\.\d+)?') {
                return [int]$matches[1]
            }
        }

        return 0
    }
    catch {
        return 0
    }
}

function Install-WingetPackage {
    param(
        [string]$PackageId,
        [string]$FriendlyName
    )

    Write-Host "Installing $FriendlyName..."

    winget install `
        --id $PackageId `
        -e `
        --accept-package-agreements `
        --accept-source-agreements

    Refresh-Environment
}

Write-Host "Checking prerequisites..."
Write-Host

Ensure-Winget

# Git

if (-not (Get-Command git -ErrorAction SilentlyContinue)) {

    Confirm-Install "Git"

    Install-WingetPackage `
        -PackageId "Git.Git" `
        -FriendlyName "Git"

    if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
        throw "Git installation failed."
    }
}

Write-Host "✓ Git found"

# Java

$javaVersion = Get-JavaMajorVersion

if ($javaVersion -lt 21) {

    if ($javaVersion -eq 0) {
        Write-Host "JDK not found."
    }
    else {
        Write-Host "Found JDK $javaVersion, but JDK 21+ is required."
    }

    Confirm-Install "JDK 21"

    Install-WingetPackage `
        -PackageId "EclipseAdoptium.Temurin.21.JDK" `
        -FriendlyName "JDK 21"

    $javaVersion = Get-JavaMajorVersion

    if ($javaVersion -lt 21) {
        throw "JDK 21 installation failed."
    }
}

Write-Host "✓ JDK $javaVersion found"

# Maven

if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {

    Confirm-Install "Maven"

    Install-WingetPackage `
        -PackageId "Apache.Maven" `
        -FriendlyName "Maven"

    if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
        throw "Maven installation failed."
    }
}

Write-Host "✓ Maven found"
Write-Host

Write-Host "Running application..."
& mvn clean javafx:run

if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}
