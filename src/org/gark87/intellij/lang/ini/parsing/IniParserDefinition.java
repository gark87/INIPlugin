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

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.gark87.intellij.lang.ini.psi.impl.IniFileImpl;
import org.jetbrains.annotations.NotNull;

/**
 * @author gark87 <arkady.galyash@gmail.com>
 */
public class IniParserDefinition implements ParserDefinition {
    @NotNull
    public Lexer createLexer(Project project) {
        return new IniLexer();
    }

    public PsiParser createParser(Project project) {
        return new IniParser();
    }

    public IFileElementType getFileNodeType() {
        return IniElementTypes.FILE;
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return IniTokenTypes.WHITESPACES;
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return IniTokenTypes.COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return IniTokenTypes.STRINGS;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        return new ASTWrapperPsiElement(node);
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new IniFileImpl(viewProvider);
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        if (left.getElementType() == IniTokenTypes.END_OF_LINE_COMMENT)
            return SpaceRequirements.MUST_LINE_BREAK;
        return SpaceRequirements.MAY;
    }
}
