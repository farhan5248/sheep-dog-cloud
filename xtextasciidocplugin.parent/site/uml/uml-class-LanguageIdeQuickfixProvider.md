# {Language}IdeQuickfixProvider

## {Language}IdeQuickfixProvider has fix{Issue} methods for each check{Issue}

There doesn't need to be a fix{Issue} method for every check{Issue} method.

If a fix{Issue} method exists, there must be a corresponding check{Issue} method.

**Examples**

```java
@Fix({Language}Validator.{TYPE}_{ASPECT}_{ISSUE})
public void fix{Type}{Aspect}{Issue}(final Issue issue, IssueResolutionAcceptor acceptor)
```

## All @Fix annotations in IdeQuickfixProvider exist in {Language}Validator

There can be multiple @Fix annotations for one fix{Issue} method.

Each of those annotations must exist in {Language}Validator.

Method names follow the convention fix{CamelCase} corresponding to @Fix annotation constants in {UPPER_SNAKE_CASE} format.

**Examples**

```java
@Fix({Language}Validator.{TYPE}_{ASPECT}_{ISSUE})
public void fix{Type}{Aspect}{Issue}(final Issue issue, IssueResolutionAcceptor acceptor)
```

## fix{Issue} methods invoke {Type}IssueResolver.correct{Issue} methods

Each fix method delegates to the appropriate IssueResolver.correct* method to generate proposals.

**Examples**

```java
public void fix{Type}{Aspect}{Issue}(final Issue issue, IssueResolutionAcceptor acceptor)
// Calls: {Type}IssueResolver.correct{Aspect}{Issue}(new {Type}Impl(({Type}) getEObject(issue)))
```
