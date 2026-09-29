$basePath = Read-Host "Enter the full path where you want the folders created"

$folders = @(
    "001 JUNIT BASICS",
    "002 MOCKITO",
    "003 MOCKMVC",
    "004 REST ASSURED",
    "005 INTEGRATION TESTING",
    "006 TESTCONTAINERS",
    "007 TEST SLICES",
    "008 EXCEPTION TESTING",
    "009 SECURITY TESTING",
    "010 VALIDATION TESTING",
    "011 CONTRACT TESTING",
    "012 PERFORMANCE TESTING",
    "013 TEST DATA MANAGEMENT",
    "014 MOCKING EXTERNAL APIS",
    "015 TEST REPORTING"
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
Write-Host "  All $($folders.Count) REST API Testing folders created at:" -ForegroundColor Magenta
Write-Host "  $basePath" -ForegroundColor Cyan