$folders = @(
    "001 CONFIGS",
    "002 ENTITIES",
    "003 REPOSITORY",
    "004 SERVICE",
    "005 CONTROLLERS",
    "006 DTOS",
    "007 VALIDATION",
    "008 EXCEPTION HANDLING",
    "009 PAGINATION",
    "010 AUDITING",
    "011 SECURITY",
    "012 FILE HANDLING",
    "013 CACHING",
    "014 SCHEDULING",
    "015 EVENTS",
    "016 MESSAGING",
    "017 TESTING",
    "018 ACTUATOR",
    "019 LOGGING",
    "020 API VERSIONING",
    "021 DOCKER",
    "022 RATE LIMITING",
    "023 DESIGN PATTERNS",
    "024 MICROSERVICES",
    "025 OBSERVABILITY"
)

foreach ($folder in $folders) {
    New-Item -ItemType Directory -Path $folder -Force | Out-Null
    Write-Host "  Created: $folder" -ForegroundColor Green
}

Write-Host ""
Write-Host "  All $($folders.Count) folders created successfully!" -ForegroundColor Magenta