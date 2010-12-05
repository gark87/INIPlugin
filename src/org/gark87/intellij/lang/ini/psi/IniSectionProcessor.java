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

package org.gark87.intellij.lang.ini.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.tree.TokenSet;
import org.gark87.intellij.lang.ini.parsing.IniTokenTypes;

/**
 * @author gark87 <arkady.galyash@gmail.com>
 */
public class IniSectionProcessor implements PsiElementProcessor {
    private final SectionImpl[] needle;
    private SectionImpl result;

    public IniSectionProcessor(SectionImpl element) {
        this.needle = element.getSubSections();
    }

    public boolean execute(PsiElement element) {
        if (element instanceof Section) {
            Section name = (Section) element;
            SectionImpl[] a = name.getSubSections();
            if (a == null)
                return true;
            if (a.length != needle.length)
                return true;
            for (int i = 0; i < a.length; i++) {
                if (!a[i].getText().equals(needle[i].getText()))
                    return true;
            }
            result = a[0];
            return false;
        }
        return true;
    }

    public SectionImpl getResult() {
        return result;
    }
}
