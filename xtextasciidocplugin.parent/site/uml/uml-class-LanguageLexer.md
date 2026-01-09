# AsciiDocLexer

## AsciiDocLexer extends InternalAsciiDocLexer

Custom lexer that overrides the generated lexer to handle context-sensitive tokenization.

**Examples**

- ```java
  // AsciiDocLexer.java
  public class AsciiDocLexer extends InternalAsciiDocLexer {
  ```

## AsciiDocLexer has hasNoDelimiter flag for no-delimiter keywords

After keywords like `Test-Suite:`, `Step-Object:`, treat following tokens as WORD. See [impl-xtext-grammar.md](../../../impl-xtext-grammar.md#state-flags) for pattern.

## AsciiDocLexer has hasConstantDelimiter flag for table cell context

After encountering `|`, check for same delimiter or treat as WORD. See [impl-xtext-grammar.md](../../../impl-xtext-grammar.md#state-flags) for pattern.

## AsciiDocLexer has hasVariableDelimiter flag for step keyword context

After step keywords (Given:/When:/Then:/And:), delegate to `super.mTokens()` for expression parsing. See [impl-xtext-grammar.md](../../../impl-xtext-grammar.md#state-flags) for pattern.

## AsciiDocLexer has three constructors

Default constructor, CharStream constructor, and CharStream with RecognizerSharedState constructor. See [impl-xtext-grammar.md](../../../impl-xtext-grammar.md#class-structure) for pattern.

## isKeyword method uses input.LA() for character lookahead

Checks if the upcoming characters match the given keyword without consuming tokens. See [impl-xtext-grammar.md](../../../impl-xtext-grammar.md#lookahead-pattern) for pattern.

## printNextToken method provides debug output

Outputs the next token text for debugging using `logger.debug()`. See [impl-xtext-grammar.md](../../../impl-xtext-grammar.md#debug-logging-method) for pattern.

## mTokens method checks keywords by category

Overrides generated mTokens to control token recognition with delimiter-based state management. Categories: whitespace, multi-line, delimiter reset, greedy collection, single-line tokens. See [impl-xtext-grammar.md](../../../impl-xtext-grammar.md#mtokens-override-pattern) for pattern.
