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
        "008 KEYWORDS"
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
        "010 THIS & SUPER KEYWORDS"
    )
    "003 SOLID PRINCIPLES" = @(
        "001 SINGLE RESPONSIBILITY",
        "002 OPEN CLOSED",
        "003 LISKOV SUBSTITUTION",
        "004 INTERFACE SEGREGATION",
        "005 DEPENDENCY INVERSION",
        "006 SOLID IN REAL PROJECTS"
    )
    "004 EXCEPTION HANDLING" = @(
        "001 CHECKED VS UNCHECKED",
        "002 TRY CATCH FINALLY",
        "003 CUSTOM EXCEPTIONS",
        "004 EXCEPTION HIERARCHY",
        "005 MULTI CATCH",
        "006 THROWS & THROW",
        "007 BEST PRACTICES"
    )
    "005 STRING HANDLING" = @(
        "001 STRING IMMUTABILITY",
        "002 STRING POOL",
        "003 STRINGBUILDER VS STRINGBUFFER",
        "004 STRING METHODS",
        "005 STRING FORMATTING",
        "006 REGEX BASICS",
        "007 INTERVIEW PROBLEMS"
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
        "011 COLLECTIONS UTILITY CLASS",
        "012 COMPARABLE VS COMPARATOR",
        "013 FAIL FAST VS FAIL SAFE",
        "014 INTERVIEW PROBLEMS"
    )
    "007 GENERICS" = @(
        "001 GENERIC CLASSES",
        "002 GENERIC METHODS",
        "003 BOUNDED TYPE PARAMETERS",
        "004 WILDCARDS",
        "005 TYPE ERASURE",
        "006 GENERICS WITH COLLECTIONS"
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
        "009 INTERVIEW PROBLEMS"
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
        "013 INTERVIEW PROBLEMS"
    )
    "010 MEMORY MANAGEMENT & GC" = @(
        "001 HEAP VS STACK",
        "002 GARBAGE COLLECTION BASICS",
        "003 GC ALGORITHMS",
        "004 MEMORY LEAKS",
        "005 STRONG WEAK SOFT REFERENCES",
        "006 FINALIZE METHOD",
        "007 JVM MEMORY TUNING"
    )
    "011 JVM INTERNALS" = @(
        "001 JVM ARCHITECTURE",
        "002 CLASS LOADING",
        "003 BYTECODE",
        "004 JIT COMPILER",
        "005 RUNTIME DATA AREAS",
        "006 CLASSLOADER TYPES",
        "007 JUST IN TIME COMPILATION"
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
        "012 TEXT BLOCKS"
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
        "015 PATTERNS IN JAVA APIs"
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
        "013 INTERVIEW PROBLEMS"
    )
    "015 JDBC & JPA BASICS" = @(
        "001 JDBC BASICS",
        "002 JDBC CONNECTION",
        "003 CRUD WITH JDBC",
        "004 PREPARED STATEMENTS",
        "005 JPA BASICS",
        "006 ENTITY MAPPING",
        "007 JPQL",
        "008 NAMED QUERIES"
    )
    "016 TESTING WITH JUNIT" = @(
        "001 JUNIT 5 BASICS",
        "002 ASSERTIONS",
        "003 TEST LIFECYCLE",
        "004 MOCKITO BASICS",
        "005 PARAMETERIZED TESTS",
        "006 EXCEPTION TESTING",
        "007 BEST PRACTICES"
    )
    "017 DATABASE CONNECTIVITY" = @(
        "001 MYSQL WITH JAVA",
        "002 POSTGRESQL WITH JAVA",
        "003 MONGODB WITH JAVA",
        "004 CONNECTION POOLING",
        "005 TRANSACTIONS IN JAVA"
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