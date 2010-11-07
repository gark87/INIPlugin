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

package org.gark87.intellij.lang.ini;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author gark87 <arkady.galyash@gmail.com>
 */
public class IniFileType extends LanguageFileType {
    private static final Icon ICON = IconLoader.getIcon("/icons/ini.png");

    public IniFileType() {
        super(new org.gark87.intellij.lang.ini.IniLanguage());
    }

    @NotNull
    public String getName() {
        return "INI";
    }

    @NotNull
    public String getDescription() {
        return "INI";
    }

    @NotNull
    public String getDefaultExtension() {
        return "ini";
    }

    public Icon getIcon() {
        return ICON;
    }
}
