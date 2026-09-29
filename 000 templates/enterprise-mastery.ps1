$basePath = Read-Host "Enter the full path where you want the folders created"

$structure = @{
    "001 KAFKA" = @(
        "001 KAFKA BASICS & ARCHITECTURE",
        "002 TOPICS PARTITIONS & OFFSETS",
        "003 PRODUCERS",
        "004 CONSUMERS & CONSUMER GROUPS",
        "005 BROKERS & CLUSTERS",
        "006 ZOOKEEPER & KRAFT",
        "007 KAFKA WITH SPRING BOOT",
        "008 KAFKA STREAMS",
        "009 KAFKA CONNECT",
        "010 SCHEMA REGISTRY & AVRO",
        "011 EXACTLY ONCE SEMANTICS",
        "012 KAFKA SECURITY",
        "013 KAFKA MONITORING",
        "014 KAFKA IN MICROSERVICES",
        "015 INTERVIEW PROBLEMS"
    )
    "002 REDIS DEEP DIVE" = @(
        "001 REDIS BASICS",
        "002 DATA STRUCTURES",
        "003 STRING COMMANDS",
        "004 HASH COMMANDS",
        "005 LIST COMMANDS",
        "006 SET COMMANDS",
        "007 SORTED SETS",
        "008 REDIS WITH SPRING BOOT",
        "009 CACHING STRATEGIES",
        "010 TTL & EXPIRY",
        "011 REDIS PUB SUB",
        "012 REDIS STREAMS",
        "013 REDIS CLUSTER",
        "014 REDIS SENTINEL",
        "015 REDIS PERSISTENCE",
        "016 REDIS SECURITY",
        "017 INTERVIEW PROBLEMS"
    )
    "003 KEYCLOAK & OAUTH2" = @(
        "001 OAUTH2 BASICS",
        "002 OPENID CONNECT",
        "003 JWT DEEP DIVE",
        "004 AUTHORIZATION CODE FLOW",
        "005 CLIENT CREDENTIALS FLOW",
        "006 KEYCLOAK SETUP",
        "007 KEYCLOAK REALMS & CLIENTS",
        "008 KEYCLOAK ROLES & USERS",
        "009 SPRING SECURITY WITH KEYCLOAK",
        "010 SOCIAL LOGIN",
        "011 TOKEN REFRESH & REVOCATION",
        "012 KEYCLOAK CUSTOMIZATION",
        "013 MULTI TENANCY WITH KEYCLOAK",
        "014 INTERVIEW PROBLEMS"
    )
    "004 KUBERNETES" = @(
        "001 KUBERNETES BASICS",
        "002 PODS",
        "003 DEPLOYMENTS",
        "004 SERVICES",
        "005 CONFIGMAPS & SECRETS",
        "006 NAMESPACES",
        "007 INGRESS",
        "008 PERSISTENT VOLUMES",
        "009 STATEFULSETS",
        "010 DAEMONSETS",
        "011 HELM CHARTS",
        "012 KUBERNETES NETWORKING",
        "013 KUBERNETES SECURITY",
        "014 HORIZONTAL POD AUTOSCALING",
        "015 KUBERNETES MONITORING",
        "016 DEPLOYING SPRING BOOT ON K8S",
        "017 INTERVIEW PROBLEMS"
    )
    "005 ELK STACK" = @(
        "001 ELK STACK OVERVIEW",
        "002 ELASTICSEARCH BASICS",
        "003 LOGSTASH BASICS",
        "004 KIBANA BASICS",
        "005 BEATS",
        "006 LOG AGGREGATION",
        "007 INDEX MANAGEMENT",
        "008 SEARCH QUERIES",
        "009 DASHBOARDS & VISUALIZATIONS",
        "010 ELK WITH SPRING BOOT",
        "011 LOG PATTERNS & PARSING",
        "012 ALERTING",
        "013 INTERVIEW PROBLEMS"
    )
    "006 REACT" = @(
        "001 REACT BASICS",
        "002 JSX",
        "003 COMPONENTS",
        "004 PROPS & STATE",
        "005 HOOKS",
        "006 USESTATE & USEEFFECT",
        "007 CONTEXT API",
        "008 REACT ROUTER",
        "009 FORMS & VALIDATION",
        "010 API CALLS WITH AXIOS",
        "011 REDUX BASICS",
        "012 REDUX TOOLKIT",
        "013 REACT QUERY",
        "014 TYPESCRIPT WITH REACT",
        "015 TESTING REACT APPS",
        "016 REACT WITH SPRING BOOT",
        "017 DEPLOYMENT",
        "018 INTERVIEW PROBLEMS"
    )
    "007 ELASTICSEARCH" = @(
        "001 ELASTICSEARCH BASICS",
        "002 INDICES & DOCUMENTS",
        "003 MAPPING & ANALYZERS",
        "004 SEARCH QUERIES",
        "005 FULL TEXT SEARCH",
        "006 AGGREGATIONS",
        "007 FILTERS & SORTING",
        "008 ELASTICSEARCH WITH SPRING BOOT",
        "009 ELASTICSEARCH WITH SPRING DATA",
        "010 PERFORMANCE TUNING",
        "011 CLUSTER MANAGEMENT",
        "012 INTERVIEW PROBLEMS"
    )
    "008 TERRAFORM" = @(
        "001 TERRAFORM BASICS",
        "002 PROVIDERS",
        "003 RESOURCES",
        "004 VARIABLES & OUTPUTS",
        "005 STATE MANAGEMENT",
        "006 MODULES",
        "007 WORKSPACES",
        "008 TERRAFORM WITH AWS",
        "009 TERRAFORM WITH AZURE",
        "010 TERRAFORM WITH GCP",
        "011 REMOTE BACKENDS",
        "012 TERRAFORM BEST PRACTICES",
        "013 INTERVIEW PROBLEMS"
    )
}

if (-not (Test-Path $basePath)) {
    New-Item -ItemType Directory -Path $basePath -Force | Out-Null
    Write-Host "  Created base folder: $basePath" -ForegroundColor Yellow
}

$mainCount = 0
$subCount  = 0

foreach ($mainFolder in $structure.Keys | Sort-Object) {
    $mainPath = Join-Path $basePath $mainFolder
    New-Item -ItemType Directory -Path $mainPath -Force | Out-Null
    Write-Host ""
    Write-Host "  Created: $mainFolder" -ForegroundColor Magenta
    $mainCount++

    foreach ($subFolder in $structure[$mainFolder]) {
        $subPath = Join-Path $mainPath $subFolder
        New-Item -ItemType Directory -Path $subPath -Force | Out-Null
        Write-Host "    + $subFolder" -ForegroundColor Green
        $subCount++
    }
}

Write-Host ""
Write-Host "  ============================================" -ForegroundColor Magenta
Write-Host "   ADVANCED TOPICS STRUCTURE CREATED!" -ForegroundColor Magenta
Write-Host "   Main Folders : $mainCount"           -ForegroundColor Cyan
Write-Host "   Sub  Folders : $subCount"            -ForegroundColor Cyan
Write-Host "   Total        : $($mainCount + $subCount)" -ForegroundColor Cyan
Write-Host "   Location     : $basePath"            -ForegroundColor Cyan
Write-Host "  ============================================" -ForegroundColor Magenta