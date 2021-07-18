# Certification Service

## Tested with Jenkins


There are several things tested:
* Anchore (on Docker image)
* Secrets in Git (Trufflehog which also checks Git commits)
* Owasp Dependency Check
* Checkstyles (check styles on java e.g lines not too long and more readability)
* sonar scanning
* mvn test + jacoco (unit Test coverage) --> jacoco plugin is needed
* spotbugs (own maven plugin)


## Logging

The logging is done with the default logging of spring boot (logback-spring).
A logback-spring.xml is added and a new environment variable (CERT_LOG_LEVEL)

### Log Levels
Set this variable to see different loggings:
The hierarchy is as follows:
OFF
FATAL
ERROR
WARN
INFO
DEBUG
TRACE


This means that every line log all things from the levels above

### Log Fields:

* timestamp
* level (message)
* thread
* message
* logger
* mdc
  * SYSTEM_LOG_LEVEL
  * REQUEST_ID