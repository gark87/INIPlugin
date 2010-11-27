/*
 * Copyright 2010 gark87
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gark87.intellij.lang.ini.parsing.test;

import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IElementType;
import org.gark87.intellij.lang.ini.parsing.IniLexer;
import org.jetbrains.annotations.NonNls;
import org.junit.Test;

/**
 * @author gark87 <arkady.galyash@gmail.com>
 */
public class IniLexerTest extends com.intellij.testFramework.UsefulTestCase {

    public IniLexerTest() {
    }

    private static void doTest(@NonNls String text, @NonNls String... expectedTokens) {
        Lexer lexer = new IniLexer();
        doTest(text, expectedTokens, lexer);
    }

    private static void doTest(String text, String[] expectedTokens, Lexer lexer) {
        lexer.start(text);
        int idx = 0;
        IElementType type;
        while ((type = lexer.getTokenType()) != null) {
            if (idx >= expectedTokens.length)
                fail("Too many tokens");
            String tokenName = type.toString();
            String expectedTokenType = expectedTokens[idx++];
            String expectedTokenText = expectedTokens[idx++];
            assertEquals("wrong type @" + idx / 2, expectedTokenType, tokenName);
            String tokenText = lexer.getBufferSequence().subSequence(lexer.getTokenStart(), lexer.getTokenEnd()).toString();
            assertEquals("wrong text @" + idx / 2, expectedTokenText, tokenText);
            lexer.advance();
        }
        if (idx < expectedTokens.length)
            fail("Not enough tokens");
    }

    @Test
    public void testSimpleProperty() throws Exception {
        doTest("xxx=yyy",
                "Ini:KEY_CHARACTERS", "xxx",
                "Ini:KEY_VALUE_SEPARATOR", "=",
                "Ini:VALUE_CHARACTERS", "yyy");

        doTest(" xxx = yyy ",
                "WHITE_SPACE", " ",
                "Ini:KEY_CHARACTERS", "xxx",
                "Ini:KEY_VALUE_SEPARATOR", " = ",
                "Ini:VALUE_CHARACTERS", "yyy ");
    }


    @Test
    public void testSpacedValue() throws Exception {
        doTest("xxx=yyy zzz",
                "Ini:KEY_CHARACTERS", "xxx",
                "Ini:KEY_VALUE_SEPARATOR", "=",
                "Ini:VALUE_CHARACTERS", "yyy zzz");
    }

    @Test
    public void testQuotedString() throws Exception {
        doTest("xxx=\"y;y\"",
                "Ini:KEY_CHARACTERS", "xxx",
                "Ini:KEY_VALUE_SEPARATOR", "=",
                "Ini:QUOTED_STRING", "\"y;y\"");

        doTest(" xxx = \" y#y \" ",
                "WHITE_SPACE", " ",
                "Ini:KEY_CHARACTERS", "xxx",
                "Ini:KEY_VALUE_SEPARATOR", " = ",
                "Ini:QUOTED_STRING", "\" y#y \"",
                "Ini:VALUE_CHARACTERS", " ");

        doTest(" xxx = \" \\\" \\t\" ",
                "WHITE_SPACE", " ",
                "Ini:KEY_CHARACTERS", "xxx",
                "Ini:KEY_VALUE_SEPARATOR", " = ",
                "Ini:QUOTED_STRING", "\" \\\" \\t\"",
                "Ini:VALUE_CHARACTERS", " ");

        doTest(" xxx = A \" y[y \" B ",
                "WHITE_SPACE", " ",
                "Ini:KEY_CHARACTERS", "xxx",
                "Ini:KEY_VALUE_SEPARATOR", " = ",
                "Ini:VALUE_CHARACTERS", "A ",
                "Ini:QUOTED_STRING", "\" y[y \"",
                "Ini:VALUE_CHARACTERS", " B ");

        doTest("xxx=A\"y]y\"B",
                "Ini:KEY_CHARACTERS", "xxx",
                "Ini:KEY_VALUE_SEPARATOR", "=",
                "Ini:VALUE_CHARACTERS", "A",
                "Ini:QUOTED_STRING", "\"y]y\"",
                "Ini:VALUE_CHARACTERS", "B");
    }

    @Test
    public void testIncompleteProperty() throws Exception {
        doTest("a", "Ini:KEY_CHARACTERS", "a");

        doTest("a.2=", "Ini:KEY_CHARACTERS", "a.2",
                "Ini:KEY_VALUE_SEPARATOR", "=");
    }

    @Test
    public void testIncompleteSection() throws Exception {
        doTest("[", "Ini:LBRACKET", "[");

        doTest("[ a",
                "Ini:LBRACKET", "[",
                "WHITE_SPACE", " ",
                "Ini:SECTION", "a");
    }

    @Test
    public void testEscaping() throws Exception {
        doTest("sdlfkjsd\\l\\\\\\:\\=gk   =   s\\nsssd",
                "Ini:KEY_CHARACTERS", "sdlfkjsd\\l\\\\\\:\\=gk",
                "Ini:KEY_VALUE_SEPARATOR", "   =   ",
                "Ini:VALUE_CHARACTERS", "s\\nsssd");
    }

    @Test
    public void testCRLFEscaping() throws Exception {
        doTest("sdlfkjsdsssd=a\\\nb",
                "Ini:KEY_CHARACTERS", "sdlfkjsdsssd",
                "Ini:KEY_VALUE_SEPARATOR", "=",
                "Ini:VALUE_CHARACTERS", "a\\\nb");
    }

    @Test
    public void testCRLFEscapingKey() throws Exception {
        doTest("x\\\ny:z",
                "Ini:KEY_CHARACTERS", "x\\\ny",
                "Ini:KEY_VALUE_SEPARATOR", ":",
                "Ini:VALUE_CHARACTERS", "z");
    }

    @Test
    public void testWhitespace() throws Exception {
        doTest("x y",
                "Ini:KEY_CHARACTERS", "x",
                "WHITE_SPACE", " ",
                "BAD_CHARACTER", "y");
    }

    @Test
    public void testComments() throws Exception {
        doTest("#test comment \n" +
                "\n" +
                ";test comment 2\r\n" +
                ";last comment 3 ",

                "Ini:END_OF_LINE_COMMENT", "#test comment \n",
                "Ini:EOL", "\n",
                "Ini:END_OF_LINE_COMMENT", ";test comment 2\r\n",
                "Ini:END_OF_LINE_COMMENT", ";last comment 3 ");
    }

    @Test
    public void testCommentsAndProperties() throws Exception {
        doTest("name=value ;comment\n",

                "Ini:KEY_CHARACTERS", "name",
                "Ini:KEY_VALUE_SEPARATOR", "=",
                "Ini:VALUE_CHARACTERS", "value ",
                "Ini:END_OF_LINE_COMMENT", ";comment\n");

        doTest("name=value#comment",

                "Ini:KEY_CHARACTERS", "name",
                "Ini:KEY_VALUE_SEPARATOR", "=",
                "Ini:VALUE_CHARACTERS", "value",
                "Ini:END_OF_LINE_COMMENT", "#comment");

        doTest(";comment\n" +
                "#comment\n" +
                "name=value",

                "Ini:END_OF_LINE_COMMENT", ";comment\n",
                "Ini:END_OF_LINE_COMMENT", "#comment\n",
                "Ini:KEY_CHARACTERS", "name",
                "Ini:KEY_VALUE_SEPARATOR", "=",
                "Ini:VALUE_CHARACTERS", "value");

        doTest("name ;comment\n" +
                " #comment\n" +
                "  = value",

                "Ini:KEY_CHARACTERS", "name",
                "WHITE_SPACE", " ",
                "Ini:END_OF_LINE_COMMENT", ";comment\n ",
                "Ini:END_OF_LINE_COMMENT", "#comment\n  ",
                "Ini:KEY_VALUE_SEPARATOR", "= ",
                "Ini:VALUE_CHARACTERS", "value");

        doTest("name = ;comment\n" +
                " #comment\n" +
                "  value",

                "Ini:KEY_CHARACTERS", "name",
                "Ini:KEY_VALUE_SEPARATOR", " = ",
                "Ini:END_OF_LINE_COMMENT", ";comment\n ",
                "Ini:END_OF_LINE_COMMENT", "#comment\n  ",
                "Ini:VALUE_CHARACTERS", "value");
    }

    @Test
    public void testCommentsAndSections() throws Exception {
        doTest("  [section;test  ]",

                "WHITE_SPACE", "  ",
                "Ini:LBRACKET", "[",
                "Ini:SECTION", "section",
                "Ini:END_OF_LINE_COMMENT", ";test  ]");

        doTest("  [  ;test  ]",

                "WHITE_SPACE", "  ",
                "Ini:LBRACKET", "[",
                "WHITE_SPACE", "  ",
                "Ini:END_OF_LINE_COMMENT", ";test  ]");

        doTest("  [section] ;comment",

                "WHITE_SPACE", "  ",
                "Ini:LBRACKET", "[",
                "Ini:SECTION", "section",
                "Ini:RBRACKET", "]",
                "WHITE_SPACE", " ",
                "Ini:END_OF_LINE_COMMENT", ";comment");
    }

    @Test
    public void testTabs() throws Exception {
        doTest("install/htdocs/imcms/html/link_editor.jsp/1002 = URL\\n\\\n" +
                "\t\\t\\teller meta_id:",
                "Ini:KEY_CHARACTERS", "install/htdocs/imcms/html/link_editor.jsp/1002",
                "Ini:KEY_VALUE_SEPARATOR", " = ",
                "Ini:VALUE_CHARACTERS", "URL\\n\\\n" + "\t\\t\\teller meta_id:");
    }

    @Test
    public void testIndentedComments() throws Exception {
        doTest("   #comm1\n#comm2=n\n\t#comm3",
                "WHITE_SPACE", "   ",
                "Ini:END_OF_LINE_COMMENT", "#comm1\n",
                "Ini:END_OF_LINE_COMMENT", "#comm2=n\n\t",
                "Ini:END_OF_LINE_COMMENT", "#comm3");
    }

    @Test
    public void testSection() throws Exception {
        doTest("  [section]",
                "WHITE_SPACE", "  ",
                "Ini:LBRACKET", "[",
                "Ini:SECTION", "section",
                "Ini:RBRACKET", "]");

        doTest("  [[test  ]",
                "WHITE_SPACE", "  ",
                "Ini:LBRACKET", "[",
                "Ini:SECTION", "[test",
                "WHITE_SPACE", "  ",
                "Ini:RBRACKET", "]");

        doTest("[  a=b]",
                "Ini:LBRACKET", "[",
                "WHITE_SPACE", "  ",
                "Ini:SECTION", "a=b",
                "Ini:RBRACKET", "]");

        doTest("[\"section\"]",
                "Ini:LBRACKET", "[",
                "Ini:SECTION", "\"section\"",
                "Ini:RBRACKET", "]");
    }

    @Test
    public void testExample() throws Exception {
        doTest("[staging : production]\n" +
                "; PHP settings we want to initialize\n" +
                "phpSettings.display_errors = 0\n" +
                "includePaths.library = APPLICATION_PATH \"/../library\"",

                "Ini:LBRACKET", "[",
                "Ini:SECTION", "staging",
                "Ini:SECTION_SEPARATOR", " : ",
                "Ini:SECTION", "production",
                "Ini:RBRACKET", "]",
                "Ini:EOL", "\n",
                "Ini:END_OF_LINE_COMMENT", "; PHP settings we want to initialize\n",
                "Ini:KEY_CHARACTERS", "phpSettings.display_errors",
                "Ini:KEY_VALUE_SEPARATOR", " = ",
                "Ini:VALUE_CHARACTERS", "0",
                "Ini:EOL", "\n",
                "Ini:KEY_CHARACTERS", "includePaths.library",
                "Ini:KEY_VALUE_SEPARATOR", " = ",
                "Ini:VALUE_CHARACTERS", "APPLICATION_PATH ",
                "Ini:QUOTED_STRING", "\"/../library\"");
    }

}