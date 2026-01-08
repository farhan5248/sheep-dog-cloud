# MyAsciiDocLexer

## MyAsciiDocLexer extends InternalAsciiDocLexer

Custom lexer that overrides the generated lexer to handle context-sensitive tokenization.

**Examples**

- ```java
  // MyAsciiDocLexer.java
  public class MyAsciiDocLexer extends InternalAsciiDocLexer {
  ```

## MyAsciiDocLexer has nextTokenIsWORD flag for table cell context

After encountering `|`, the next token should be treated as WORD even if it matches a keyword. See [impl-xtext-grammar.md](../../../impl-xtext-grammar.md#class-structure) for pattern.

## MyAsciiDocLexer has useDefaultLexer flag for step keyword context

After step keywords (Given:/When:/Then:/And:), delegate to the generated lexer for the remainder of the line. See [impl-xtext-grammar.md](../../../impl-xtext-grammar.md#class-structure) for pattern.

## MyAsciiDocLexer has three constructors

Default constructor, CharStream constructor, and CharStream with RecognizerSharedState constructor. See [impl-xtext-grammar.md](../../../impl-xtext-grammar.md#class-structure) for pattern.

## isKeyword method uses input.LA() for character lookahead

Checks if the upcoming characters match the given keyword without consuming tokens. See [impl-xtext-grammar.md](../../../impl-xtext-grammar.md#lookahead-pattern) for pattern.

## printNextToken method provides debug output

Outputs the next token text for debugging purposes. See [impl-xtext-grammar.md](../../../impl-xtext-grammar.md#debug-logging-method) for pattern.

## mTokens method checks keywords in priority order

Overrides generated mTokens to control token recognition with state management. See [impl-xtext-grammar.md](../../../impl-xtext-grammar.md#mtokens-override-pattern) for pattern.
