Vala Language plugin for Intellij
====================

Still under constructor, not even Alpha yet.

I would appreciate any help on that.

About language itself: https://wiki.gnome.org/Projects/Vala


How to build
------------------

  1. Install Grammar-Kit plugin for Intellij
  2. Import Vala plugin sources as new Intellij project (in other words, create new project from sources). When you do this, choose Intellij plugin project type.
  3. Mark src/main/java as sources directory, src/main/resources as resources directory
  4. Mark src/test/java and src/test/resources as test sources directory
  5. Open org.intellij.vala.parser.Vala.bnf file in Intellij and press Ctrl+Shift+G to generate parser code
  6. Still having the file open right click somewhere in it and invoke Generate JFlex Lexer. Choose org.intellij.vala.lexer._ValaLexer.flex as output file
  7. Open the generated .flex file and run JFlex generator from it (Ctrl + Shift + G)
  8. Now you should have a gen directory in the project root dir. Mark it as Generated Sources Root.

Now the whole code should compile. If it does not, invoke point 5. again. There is possible that there are some cyclic
dependencies between generated sources and normal ones.
