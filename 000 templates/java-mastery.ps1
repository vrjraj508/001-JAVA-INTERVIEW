$basePath = Read-Host "Enter the full path where you want the folders created"

$structure = @{
    "001 JAVA BASICS" = @(
        "001 DATA TYPES & VARIABLES",
        "002 OPERATORS",
        "003 CONTROL FLOW",
        "004 ARRAYS",
        "005 METHODS",
        "006 STRINGS BASICS",
        "007 TYPE CASTING",
        "008 KEYWORDS",
        "009 INPUT & OUTPUT",
        "010 VARARGS"
    )
    "002 OOP CONCEPTS" = @(
        "001 CLASSES & OBJECTS",
        "002 CONSTRUCTORS",
        "003 INHERITANCE",
        "004 POLYMORPHISM",
        "005 ABSTRACTION",
        "006 ENCAPSULATION",
        "007 INTERFACES",
        "008 ABSTRACT CLASSES",
        "009 STATIC & INSTANCE",
        "010 THIS & SUPER KEYWORDS",
        "011 INNER CLASSES",
        "012 ANONYMOUS CLASSES",
        "013 ENUM CLASSES",
        "014 RECORD CLASSES",
        "015 SEALED CLASSES"
    )
    "003 SOLID PRINCIPLES" = @(
        "001 SINGLE RESPONSIBILITY",
        "002 OPEN CLOSED",
        "003 LISKOV SUBSTITUTION",
        "004 INTERFACE SEGREGATION",
        "005 DEPENDENCY INVERSION",
        "006 SOLID IN REAL PROJECTS",
        "007 SOLID VIOLATIONS & FIXES",
        "008 SOLID INTERVIEW QUESTIONS"
    )
    "004 EXCEPTION HANDLING" = @(
        "001 CHECKED VS UNCHECKED",
        "002 TRY CATCH FINALLY",
        "003 CUSTOM EXCEPTIONS",
        "004 EXCEPTION HIERARCHY",
        "005 MULTI CATCH",
        "006 THROWS & THROW",
        "007 TRY WITH RESOURCES",
        "008 EXCEPTION CHAINING",
        "009 BEST PRACTICES",
        "010 INTERVIEW PROBLEMS"
    )
    "005 STRING HANDLING" = @(
        "001 STRING IMMUTABILITY",
        "002 STRING POOL",
        "003 STRINGBUILDER VS STRINGBUFFER",
        "004 STRING METHODS",
        "005 STRING FORMATTING",
        "006 REGEX BASICS",
        "007 STRING COMPARISON",
        "008 STRING CONVERSION",
        "009 INTERVIEW PROBLEMS"
    )
    "006 COLLECTIONS FRAMEWORK" = @(
        "001 COLLECTION HIERARCHY",
        "002 ARRAYLIST",
        "003 LINKEDLIST",
        "004 HASHSET & LINKEDHASHSET",
        "005 TREESET",
        "006 HASHMAP",
        "007 LINKEDHASHMAP",
        "008 TREEMAP",
        "009 QUEUE & DEQUE",
        "010 STACK",
        "011 PRIORITY QUEUE",
        "012 ENUMSET & ENUMMAP",
        "013 COLLECTIONS UTILITY CLASS",
        "014 COMPARABLE VS COMPARATOR",
        "015 FAIL FAST VS FAIL SAFE",
        "016 ITERATOR PATTERN",
        "017 UNMODIFIABLE COLLECTIONS",
        "018 INTERVIEW PROBLEMS"
    )
    "007 GENERICS" = @(
        "001 GENERIC CLASSES",
        "002 GENERIC METHODS",
        "003 BOUNDED TYPE PARAMETERS",
        "004 WILDCARDS",
        "005 TYPE ERASURE",
        "006 GENERICS WITH COLLECTIONS",
        "007 GENERIC INTERFACES",
        "008 INTERVIEW PROBLEMS"
    )
    "008 FUNCTIONAL PROGRAMMING" = @(
        "001 LAMBDA EXPRESSIONS",
        "002 FUNCTIONAL INTERFACES",
        "003 STREAMS API",
        "004 METHOD REFERENCES",
        "005 OPTIONAL CLASS",
        "006 PREDICATE FUNCTION CONSUMER",
        "007 COLLECTORS",
        "008 PARALLEL STREAMS",
        "009 FLATMAP & MAP",
        "010 REDUCE OPERATIONS",
        "011 STREAM PIPELINES",
        "012 INTERVIEW PROBLEMS"
    )
    "009 MULTITHREADING & CONCURRENCY" = @(
        "001 THREAD BASICS",
        "002 THREAD LIFECYCLE",
        "003 RUNNABLE VS CALLABLE",
        "004 SYNCHRONIZATION",
        "005 VOLATILE KEYWORD",
        "006 WAIT NOTIFY NOTIFYALL",
        "007 EXECUTOR FRAMEWORK",
        "008 COMPLETABLEFUTURE",
        "009 LOCKS & REENTRANTLOCK",
        "010 CONCURRENT COLLECTIONS",
        "011 DEADLOCK & PREVENTION",
        "012 THREAD POOL",
        "013 FORK JOIN FRAMEWORK",
        "014 ATOMIC CLASSES",
        "015 SEMAPHORE & COUNTDOWNLATCH",
        "016 CYCLICBARRIER & PHASER",
        "017 THREADLOCAL",
        "018 INTERVIEW PROBLEMS"
    )
    "010 MEMORY MANAGEMENT & GC" = @(
        "001 HEAP VS STACK",
        "002 GARBAGE COLLECTION BASICS",
        "003 GC ALGORITHMS",
        "004 MEMORY LEAKS",
        "005 STRONG WEAK SOFT REFERENCES",
        "006 FINALIZE METHOD",
        "007 JVM MEMORY TUNING",
        "008 MEMORY PROFILING",
        "009 OUTOFMEMORY ERRORS",
        "010 INTERVIEW PROBLEMS"
    )
    "011 JVM INTERNALS" = @(
        "001 JVM ARCHITECTURE",
        "002 CLASS LOADING",
        "003 BYTECODE",
        "004 JIT COMPILER",
        "005 RUNTIME DATA AREAS",
        "006 CLASSLOADER TYPES",
        "007 JUST IN TIME COMPILATION",
        "008 JVM FLAGS & TUNING",
        "009 INTERVIEW PROBLEMS"
    )
    "012 JAVA 8 TO 21 FEATURES" = @(
        "001 JAVA 8 FEATURES",
        "002 JAVA 9 MODULES & JSHELL",
        "003 JAVA 10 VAR KEYWORD",
        "004 JAVA 11 FEATURES",
        "005 JAVA 14 RECORDS PREVIEW",
        "006 JAVA 15 SEALED CLASSES",
        "007 JAVA 16 RECORDS FINAL",
        "008 JAVA 17 FEATURES LTS",
        "009 JAVA 21 FEATURES LTS",
        "010 PATTERN MATCHING",
        "011 SWITCH EXPRESSIONS",
        "012 TEXT BLOCKS",
        "013 VIRTUAL THREADS",
        "014 STRUCTURED CONCURRENCY"
    )
    "013 DESIGN PATTERNS" = @(
        "001 SINGLETON",
        "002 FACTORY",
        "003 ABSTRACT FACTORY",
        "004 BUILDER",
        "005 PROTOTYPE",
        "006 ADAPTER",
        "007 DECORATOR",
        "008 FACADE",
        "009 PROXY",
        "010 OBSERVER",
        "011 STRATEGY",
        "012 TEMPLATE METHOD",
        "013 COMMAND",
        "014 ITERATOR",
        "015 CHAIN OF RESPONSIBILITY",
        "016 STATE PATTERN",
        "017 MEDIATOR PATTERN",
        "018 PATTERNS IN JAVA APIs",
        "019 INTERVIEW PROBLEMS"
    )
    "014 DATA STRUCTURES & ALGORITHMS" = @(
        "001 ARRAYS & STRINGS",
        "002 LINKED LIST",
        "003 STACK & QUEUE",
        "004 TREES & BST",
        "005 GRAPHS",
        "006 HASHING",
        "007 SORTING ALGORITHMS",
        "008 SEARCHING ALGORITHMS",
        "009 RECURSION & BACKTRACKING",
        "010 DYNAMIC PROGRAMMING",
        "011 GREEDY ALGORITHMS",
        "012 BIG O NOTATION",
        "013 TWO POINTERS",
        "014 SLIDING WINDOW",
        "015 BINARY SEARCH",
        "016 HEAP & PRIORITY QUEUE",
        "017 TRIE",
        "018 INTERVIEW PROBLEMS"
    )
    "015 JDBC & JPA BASICS" = @(
        "001 JDBC BASICS",
        "002 JDBC CONNECTION",
        "003 CRUD WITH JDBC",
        "004 PREPARED STATEMENTS",
        "005 JPA BASICS",
        "006 ENTITY MAPPING",
        "007 JPQL",
        "008 NAMED QUERIES",
        "009 TRANSACTIONS",
        "010 INTERVIEW PROBLEMS"
    )
    "016 HIBERNATE ADVANCED" = @(
        "001 HIBERNATE BASICS",
        "002 SESSION & SESSION FACTORY",
        "003 LAZY VS EAGER LOADING",
        "004 CACHING L1 & L2",
        "005 HQL",
        "006 CRITERIA API",
        "007 N+1 PROBLEM & FIX",
        "008 ENTITY RELATIONSHIPS",
        "009 INHERITANCE MAPPING",
        "010 INTERVIEW PROBLEMS"
    )
    "017 TESTING WITH JUNIT" = @(
        "001 JUNIT 5 BASICS",
        "002 ASSERTIONS",
        "003 TEST LIFECYCLE",
        "004 MOCKITO BASICS",
        "005 PARAMETERIZED TESTS",
        "006 EXCEPTION TESTING",
        "007 INTEGRATION TESTING",
        "008 CODE COVERAGE",
        "009 BEST PRACTICES",
        "010 INTERVIEW PROBLEMS"
    )
    "018 SQL & DATABASE FUNDAMENTALS" = @(
        "001 SQL BASICS",
        "002 DDL DML DCL",
        "003 JOINS",
        "004 SUBQUERIES",
        "005 INDEXES",
        "006 NORMALIZATION",
        "007 STORED PROCEDURES",
        "008 TRIGGERS",
        "009 TRANSACTIONS & ACID",
        "010 VIEWS",
        "011 AGGREGATE FUNCTIONS",
        "012 WINDOW FUNCTIONS",
        "013 QUERY OPTIMIZATION",
        "014 INTERVIEW PROBLEMS"
    )
    "019 REST API CONCEPTS" = @(
        "001 HTTP BASICS",
        "002 REST PRINCIPLES",
        "003 HTTP METHODS",
        "004 STATUS CODES",
        "005 REQUEST & RESPONSE",
        "006 HEADERS & COOKIES",
        "007 AUTHENTICATION BASICS",
        "008 POSTMAN",
        "009 API VERSIONING",
        "010 SWAGGER & OPENAPI",
        "011 INTERVIEW PROBLEMS"
    )
    "020 GIT & VERSION CONTROL" = @(
        "001 GIT BASICS",
        "002 BRANCHING & MERGING",
        "003 REBASE",
        "004 PULL REQUESTS",
        "005 GIT FLOW",
        "006 CONFLICT RESOLUTION",
        "007 GIT STASH & CHERRY PICK",
        "008 TAGGING & RELEASES",
        "009 GITHUB ACTIONS BASICS",
        "010 INTERVIEW PROBLEMS"
    )
    "021 LINUX BASICS" = @(
        "001 LINUX COMMANDS",
        "002 FILE PERMISSIONS",
        "003 FILE SYSTEM",
        "004 PROCESS MANAGEMENT",
        "005 NETWORKING COMMANDS",
        "006 SHELL SCRIPTING BASICS",
        "007 ENVIRONMENT VARIABLES",
        "008 LOGS & MONITORING",
        "009 INTERVIEW PROBLEMS"
    )
    "022 DOCKER BASICS" = @(
        "001 DOCKER CONCEPTS",
        "002 DOCKERFILE",
        "003 DOCKER IMAGES",
        "004 DOCKER CONTAINERS",
        "005 DOCKER COMPOSE",
        "006 DOCKER NETWORKING",
        "007 DOCKER VOLUMES",
        "008 DOCKERIZING JAVA APPS",
        "009 INTERVIEW PROBLEMS"
    )
    "023 SYSTEM DESIGN" = @(
        "001 SYSTEM DESIGN BASICS",
        "002 SCALABILITY",
        "003 LOAD BALANCING",
        "004 CACHING STRATEGIES",
        "005 DATABASE DESIGN",
        "006 API DESIGN",
        "007 MESSAGE QUEUES",
        "008 CAP THEOREM",
        "009 CONSISTENT HASHING",
        "010 RATE LIMITING",
        "011 DESIGN URL SHORTENER",
        "012 DESIGN TWITTER",
        "013 DESIGN UBER",
        "014 DESIGN WHATSAPP",
        "015 INTERVIEW PROBLEMS"
    )
    "024 DATABASE CONNECTIVITY" = @(
        "001 MYSQL WITH JAVA",
        "002 POSTGRESQL WITH JAVA",
        "003 MONGODB WITH JAVA",
        "004 CONNECTION POOLING",
        "005 TRANSACTIONS IN JAVA",
        "006 REDIS WITH JAVA",
        "007 ELASTICSEARCH WITH JAVA"
    )
    "025 INTERVIEW PREPARATION" = @(
        "001 TOP 100 JAVA QUESTIONS",
        "002 OOP INTERVIEW QUESTIONS",
        "003 COLLECTIONS QUESTIONS",
        "004 MULTITHREADING QUESTIONS",
        "005 JAVA 8 QUESTIONS",
        "006 DESIGN PATTERN QUESTIONS",
        "007 SQL INTERVIEW QUESTIONS",
        "008 SYSTEM DESIGN QUESTIONS",
        "009 CODING CHALLENGES",
        "010 RESUME PROJECTS",
        "011 MOCK INTERVIEWS",
        "012 BEHAVIORAL QUESTIONS"
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
Write-Host "   JAVA LEARNING STRUCTURE CREATED!" -ForegroundColor Magenta
Write-Host "   Main Folders : $mainCount" -ForegroundColor Cyan
Write-Host "   Sub  Folders : $subCount" -ForegroundColor Cyan
Write-Host "   Total        : $($mainCount + $subCount)" -ForegroundColor Cyan
Write-Host "   Location     : $basePath" -ForegroundColor Cyan
Write-Host "  ============================================" -ForegroundColor Magenta