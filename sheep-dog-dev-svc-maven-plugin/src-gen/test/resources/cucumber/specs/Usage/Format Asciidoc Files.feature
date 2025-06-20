@sheep-dog-dev @round-trip
Feature: Format Asciidoc Files

  \@sheep-dog-dev
  \@round-trip
  TODO Make more changes to the source file to demonstrate all the formatting changes.
  One of the more important uses of going from asciidoc to uml and back to asciidoc is to update the files after changing the grammar.
  I'd first convert all the asciidoc files to a uml model using the previous version.
  Then I'd convert the uml model back to asciidoc files using the current/latest version.
  An example of this is when I converted steps from section to lists.
  In my QA team, as the language evolved over the 3 years, we had to keep updating all previous test cases.
  First we were using robot framework and csv lists of value in a step like "do the thing with field1 is value, field2 is value".
  When we moved to Cucumber, the steps were preserved.
  However, we then moved the "field1 is value, field2 is value" to a data table and that was done by converting feature to uml and back to feature files.

  Scenario: Format an asciidoc file

    The table formatting is off, this is intentional to check that the formatting works.

    Given The spec-prj project, src/test/resources/asciidoc/specs/app/Process.asciidoc file is created as follows
          """
          = Test-Suite: Process
          
          Desc 1
          
          == Test-Case: Story One
          
          @tag2
          Desc 2
          
          * Given: The blah application, Object page is valid
          
          * Then: The blah application, Object page is created as follows
          +
          ----
            text1
          
            text2
          ----
          
          == Test-Case: Story Two
          
          @tag3
          Desc 3
          
          * Given: The blah application, Object page is invalid
          
          * When: The blah application, Object page is created as follows
          +
          |===
          | grp | ins
          | 8 | {ins}
          |===
          
          * Test-Data: Some data
          +
          |===
          | ins
          | 4
          |===
          
          * Test-Data: Dataset 2
          +
          |===
          | ins
          | 5
          | 6
          |===
          """
      And The spec-prj project, src/test/resources/asciidoc/stepdefs/blah application/Object page.asciidoc file is created as follows
          """
          = Step-Object: Object page
          
          == Step-Definition: is valid
          
          == Step-Definition: is invalid
          
          == Step-Definition: is created as follows
          
          * Step-Parameters: 1
          +
          |===
          | grp | ins
          |===
          
          * Step-Parameters: 2
          +
          |===
          | Content
          |===
          """
     When The maven plugin, asciidoctor-to-uml goal is executed
      And The maven plugin, uml-to-asciidoctor goal is executed
     Then The spec-prj project, src/test/resources/asciidoc/specs/app/Process.asciidoc file will be created as follows
          """
          = Test-Suite: Process
          
          Desc 1
          
          == Test-Case: Story One
          
          @tag2
          Desc 2
          
          * Given: The blah application, Object page is valid
          
          * Then: The blah application, Object page is created as follows
          +
          ----
            text1
          
            text2
          ----
          
          == Test-Case: Story Two
          
          @tag3
          Desc 3
          
          * Given: The blah application, Object page is invalid
          
          * When: The blah application, Object page is created as follows
          +
          |===
          | grp | ins  
          | 8   | {ins}
          |===
          
          * Test-Data: Some data
          +
          |===
          | ins
          | 4  
          |===
          
          * Test-Data: Dataset 2
          +
          |===
          | ins
          | 5  
          | 6  
          |===
          """
      And The spec-prj project, src/test/resources/asciidoc/stepdefs/blah application/Object page.asciidoc file will be created as follows
          """
          = Step-Object: Object page
          
          == Step-Definition: is created as follows
          
          * Step-Parameters: 1
          +
          |===
          | grp | ins
          |===
          
          * Step-Parameters: 2
          +
          |===
          | Content
          |===
          
          == Step-Definition: is invalid
          
          == Step-Definition: is valid
          """

