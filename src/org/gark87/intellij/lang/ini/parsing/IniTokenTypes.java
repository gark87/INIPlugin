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

package org.gark87.intellij.lang.ini.parsing;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author gark87 <arkady.galyash@gmail.com>
 */
public interface IniTokenTypes {
    IElementType WHITE_SPACE = TokenType.WHITE_SPACE;
    IElementType BAD_CHARACTER = TokenType.BAD_CHARACTER;

    IElementType END_OF_LINE_COMMENT = new IniElementType("END_OF_LINE_COMMENT");
    IElementType KEY_CHARACTERS = new IniElementType("KEY_CHARACTERS");
    IElementType VALUE_CHARACTERS = new IniElementType("VALUE_CHARACTERS");
    IElementType KEY_VALUE_SEPARATOR = new IniElementType("KEY_VALUE_SEPARATOR");
    IElementType EOL = new IniElementType("EOL");
    IElementType SECTION = new IniElementType("SECTION");
    IElementType QUOTED_STRING = new IniElementType("QUOTED_STRING");
    IElementType RBRACKET = new IniElementType("RBRACKET");
    IElementType LBRACKET = new IniElementType("LBRACKET");
    IElementType SECTION_SEPARATOR = new IniElementType("SECTION_SEPARATOR");

    TokenSet COMMENTS = TokenSet.create(END_OF_LINE_COMMENT);
    TokenSet STRINGS = TokenSet.create(IniTokenTypes.QUOTED_STRING);
    TokenSet WHITESPACES = TokenSet.create(WHITE_SPACE);
}
