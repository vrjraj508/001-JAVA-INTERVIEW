$basePath = Read-Host "Enter the full path where you want the folders created"

$structure = @{
    "004 DEVOPS & CICD" = @(
        "001 JENKINS PIPELINES",
        "002 GITHUB ACTIONS",
        "003 SONARQUBE",
        "004 NEXUS REPOSITORY",
        "005 DOCKER COMPOSE",
        "006 BLUE GREEN DEPLOYMENT",
        "007 ROLLING DEPLOYMENT",
        "008 MONITORING & ALERTS"
    )
    "005 DATABASE ADVANCED" = @(
        "001 FLYWAY MIGRATIONS",
        "002 LIQUIBASE",
        "003 QUERY OPTIMIZATION",
        "004 CONNECTION POOLING HIKARICP",
        "005 MULTI TENANCY",
        "006 READ REPLICAS",
        "007 SHARDING",
        "008 REDIS ADVANCED",
        "009 ELASTICSEARCH"
    )
    "006 CLOUD DEPLOYMENT" = @(
        "001 AWS EC2",
        "002 AWS RDS",
        "003 AWS S3",
        "004 AWS ECS",
        "005 AWS ELASTIC BEANSTALK",
        "006 AZURE SPRING APPS",
        "007 GCP CLOUD RUN",
        "008 RAILWAY & RENDER"
    )
    "007 ARCHITECTURE PATTERNS" = @(
        "001 HEXAGONAL ARCHITECTURE",
        "002 DOMAIN DRIVEN DESIGN",
        "003 CQRS",
        "004 EVENT DRIVEN ARCHITECTURE",
        "005 CLEAN ARCHITECTURE",
        "006 REPOSITORY PATTERN",
        "007 SAGA PATTERN",
        "008 OUTBOX PATTERN",
        "009 STRANGLER FIG PATTERN"
    )
    "008 REACTIVE PROGRAMMING" = @(
        "001 SPRING WEBFLUX BASICS",
        "002 PROJECT REACTOR",
        "003 MONO AND FLUX",
        "004 REACTIVE MONGODB",
        "005 REACTIVE REDIS",
        "006 BACKPRESSURE",
        "007 REACTIVE SECURITY",
        "008 REACTIVE TESTING"
    )
}

if (-not (Test-Path $basePath)) {
    New-Item -ItemType Directory -Path $basePath -Force | Out-Null
    Write-Host "  Created base folder: $basePath" -ForegroundColor Yellow
}

foreach ($mainFolder in $structure.Keys | Sort-Object) {
    $mainPath = Join-Path $basePath $mainFolder
    New-Item -ItemType Directory -Path $mainPath -Force | Out-Null
    Write-Host "" 
    Write-Host "  Created: $mainFolder" -ForegroundColor Magenta

    foreach ($subFolder in $structure[$mainFolder]) {
        $subPath = Join-Path $mainPath $subFolder
        New-Item -ItemType Directory -Path $subPath -Force | Out-Null
        Write-Host "    + $subFolder" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "  ============================================" -ForegroundColor Magenta
Write-Host "   All folders and subfolders created!" -ForegroundColor Magenta
Write-Host "   Location: $basePath" -ForegroundColor Cyan
Write-Host "  ============================================" -ForegroundColor Magenta