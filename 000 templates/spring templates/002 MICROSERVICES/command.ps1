$basePath = Read-Host "Enter the full path where you want the folders created"

$folders = @(
    "001 INTRO & ARCHITECTURE",
    "002 SPRING CLOUD BASICS",
    "003 SERVICE REGISTRY",
    "004 API GATEWAY",
    "005 CONFIG SERVER",
    "006 INTER SERVICE COMM",
    "007 LOAD BALANCING",
    "008 CIRCUIT BREAKER",
    "009 DISTRIBUTED TRACING",
    "010 CENTRALIZED LOGGING",
    "011 MESSAGE DRIVEN",
    "012 SAGA PATTERN",
    "013 API COMPOSITION",
    "014 SECURITY",
    "015 SERVICE MESH",
    "016 CONTAINERIZATION",
    "017 ORCHESTRATION",
    "018 MONITORING",
    "019 EVENT SOURCING",
    "020 TESTING"
)

if (-not (Test-Path $basePath)) {
    New-Item -ItemType Directory -Path $basePath -Force | Out-Null
    Write-Host "  Created base folder: $basePath" -ForegroundColor Yellow
}

foreach ($folder in $folders) {
    $fullPath = Join-Path $basePath $folder
    New-Item -ItemType Directory -Path $fullPath -Force | Out-Null
    Write-Host "  Created: $folder" -ForegroundColor Green
}

Write-Host ""
Write-Host "  All $($folders.Count) microservices folders created at:" -ForegroundColor Magenta
Write-Host "  $basePath" -ForegroundColor Cyan