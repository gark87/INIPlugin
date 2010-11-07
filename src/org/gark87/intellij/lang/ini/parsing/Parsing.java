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

import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.IElementType;

/**
 * @author gark87 <arkady.galyash@gmail.com>
 */
public class Parsing {
    private static final Logger LOG = Logger.getInstance("#com.intellij.lang.ini.parsing.Parsing");

    public static PsiBuilder.Marker parseStmt(PsiBuilder builder, PsiBuilder.Marker sectionMarker) {
        IElementType tokenType = skipEOLs(builder);
        if (tokenType == IniTokenTypes.SECTION)
            return parseSectionName(builder, sectionMarker);
        if (tokenType == IniTokenTypes.KEY_CHARACTERS) {
            parseProperty(builder);
            return sectionMarker;
        } else {
            builder.advanceLexer();
            builder.error("TODO! Here should be correct error msg");
        }
        return sectionMarker;
    }

    private static PsiBuilder.Marker parseSectionName(PsiBuilder builder, PsiBuilder.Marker oldMarker) {
        LOG.assertTrue(builder.getTokenType() == IniTokenTypes.SECTION);
        builder.advanceLexer();
        oldMarker.done(IniElementTypes.SECTION);
        lookupEOL(builder);
        return builder.mark();
    }

    private static IElementType skipEOLs(PsiBuilder builder) {
        IElementType tokenType = builder.getTokenType();
        while (!builder.eof() && tokenType == IniTokenTypes.EOL) {
            builder.advanceLexer();
            tokenType = builder.getTokenType();
        }
        return tokenType;
    }

    public static void parseProperty(PsiBuilder builder) {
        IElementType tokenType = builder.getTokenType();
        if (tokenType == IniTokenTypes.KEY_CHARACTERS) {
            final PsiBuilder.Marker prop = builder.mark();

            parseKey(builder);
            if (builder.getTokenType() == IniTokenTypes.KEY_VALUE_SEPARATOR) {
                parseKeyValueSeparator(builder);
                parseValue(builder);
            }
            prop.done(IniElementTypes.PROPERTY);
            lookupEOL(builder);
        } else {
            builder.advanceLexer();
            builder.error("TODO! Here should be correct error msg");
        }
    }

    private static void lookupEOL(PsiBuilder builder) {
        while (!builder.eof() && builder.getTokenType() != IniTokenTypes.EOL) {
            builder.error("TODO! Here should be correct error msg");
            builder.advanceLexer();
        }
        skipEOLs(builder);
    }

    private static void parseKeyValueSeparator(final PsiBuilder builder) {
        LOG.assertTrue(builder.getTokenType() == IniTokenTypes.KEY_VALUE_SEPARATOR);
        builder.advanceLexer();
    }

    private static void parseValue(final PsiBuilder builder) {
        while (!builder.eof() && builder.getTokenType() == IniTokenTypes.VALUE_CHARACTERS
                || builder.getTokenType() == IniTokenTypes.QUOTED_STRING) {
            builder.advanceLexer();
        }
    }

    private static void parseKey(final PsiBuilder builder) {
        LOG.assertTrue(builder.getTokenType() == IniTokenTypes.KEY_CHARACTERS);
        builder.advanceLexer();
    }
}
