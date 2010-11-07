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

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

/**
 * @author gark87 <arkady.galyash@gmail.com>
 */
public class IniLexer extends FlexAdapter {
    public IniLexer() {
        super(new _IniLexer((Reader) null));
    }
}