/**
 * It's an automatically generated code. Do not modify it.
 * ATTENTION! File _IniLexer.java was generated by JFlex patched by the JetBrains guys.
 *
 *   # git clone git://git.jetbrains.org/idea/community.git $IDEADIR
 *   # $IDEADIR/tools/lexer/jflex-1.4/bin/jflex --skel $IDEADIR/tools/lexer/idea-flex.skeleton \
 *        --charat -d . ./_IniLexer.flex
 *
 */
package org.gark87.intellij.lang.ini.parsing;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

%%

%class _IniLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

%{
public void reset(CharSequence charSequence, int i) {
  throw new IllegalStateException("Older versions of Idea use this deprecated method");
}
%}

CRLF= \n | \r\n?
WHITE_SPACE_CHAR=[\ \t\f]
QUOTED_STRING=\"([^\"\\\r\n] | "\\"[^\r\n])*\"
COMMENT=[;#][^\r\n]* ({CRLF} [\ \t\f]*)?
KEY_SEPARATOR=[\ \t]*[:=][\ \t]*
KEY_CHARACTER=[^:=\[\]#;\ \n\r\t\f\\] | "\\"{CRLF} | "\\".
VALUE_CHARACTER=[^#;\"\n\r\f\\] | "\\"{CRLF} | "\\".
SECTION_CHARACTER=[^ \t\f\]:;#\r\n]
SECTION_SEPARATOR=[\ \t]*":"[\ \t]*
LBRACKET="["
RBRACKET="]"

%state IN_VALUE
%state IN_SECTION
%state IN_KEY_VALUE_SEPARATOR

%%

<YYINITIAL> {LBRACKET}                   { yybegin(IN_SECTION); return IniTokenTypes.LBRACKET;}
<YYINITIAL> {KEY_CHARACTER}+             { yybegin(IN_KEY_VALUE_SEPARATOR); return IniTokenTypes.KEY_CHARACTERS; }
<IN_SECTION> {
  {RBRACKET}                             { return IniTokenTypes.RBRACKET; }
  {SECTION_SEPARATOR}                    { return IniTokenTypes.SECTION_SEPARATOR; }
  {SECTION_CHARACTER}+                   { return IniTokenTypes.SECTION; }
}
<IN_KEY_VALUE_SEPARATOR> {KEY_SEPARATOR} { yybegin(IN_VALUE); return IniTokenTypes.KEY_VALUE_SEPARATOR; }
<IN_VALUE> {VALUE_CHARACTER}+            { return IniTokenTypes.VALUE_CHARACTERS; }
<IN_VALUE> {QUOTED_STRING}               { return IniTokenTypes.QUOTED_STRING; }
{COMMENT}                                { return IniTokenTypes.END_OF_LINE_COMMENT; }
{CRLF}                                   { yybegin(YYINITIAL); return IniTokenTypes.EOL; }
{WHITE_SPACE_CHAR}+                      { return IniTokenTypes.WHITE_SPACE; }
.                                        { return IniTokenTypes.BAD_CHARACTER; }
