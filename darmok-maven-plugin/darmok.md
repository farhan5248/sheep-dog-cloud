# Darmok

## Install

```
cd C:\Users\Farhan\git\sheep-dog-main\sheep-dog-cloud\darmok-maven-plugin
mvn install
```

## Run

```
cd C:\Users\Farhan\git\sheep-dog-main\sheep-dog-local\sheep-dog-test
mvn org.farhan:darmok-maven-plugin:run
```

## Properties

| Property | Default | Description |
|---|---|---|
| specsDir | ../../sheep-dog-qa/sheep-dog-specs | Path to specs project (relative to baseDir) |
| asciidocDir | ${specsDir}/src/test/resources/asciidoc/specs/Ubiquitous Language | Path to asciidoc files |
| scenariosFile | scenarios-list.txt | Scenarios list file in baseDir |
| host | dev.sheepdogdev.io | Server host |
| mavenPlugin | org.farhan:sheep-dog-dev-svc-maven-plugin | Maven plugin for UML conversions |
| model | sonnet | Claude CLI model |
| coAuthor | Claude Sonnet 4.5 <noreply@anthropic.com> | Git co-author for commits |
| maxRetries | 3 | Max retries for Claude CLI errors |
| retryWaitSeconds | 30 | Wait time between retries |
| pipeline | forward | RGR refactor pipeline |

Override properties with -D flags:

```
mvn org.farhan:darmok-maven-plugin:run -Dmodel=opus -DmaxRetries=5
```
