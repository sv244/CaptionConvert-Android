/*
 * Copyright (C) 2015-2016 Sébastiaan (github.com/se-bastiaan)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.se_bastiaan.captionconvert;

import java.util.Hashtable;
import java.util.TreeMap;

/**
 * These objects can (should) only be created through the implementations of parseFile() in the {@link TimedTextFileFormat} interface
 * They are an object representation of a subtitle file and contain all the captions and associated styles.
 * <br><br>
 * Copyright (c) 2012 J. David Requejo <br>
 * j[dot]david[dot]requejo[at] Gmail
 * <br><br>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 * <br><br>
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 * <br><br>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 * @author J. David Requejo
 */
public class TimedTextObject {

    public String title = "";
    public String description = "";
    public String copyright = "";
    public String author = "";
    public String fileName = "";
    public String language = "";

    public Hashtable<String, Style> styling;

    public TreeMap<Integer, Caption> captions;

    public String warnings;

    public boolean useASSInsteadOfSSA = true;
    public boolean built = false;
    public int offset = 0;

    /**
     * Protected constructor so it can't be created from outside
     */
    public TimedTextObject() {
        styling = new Hashtable<>();
        captions = new TreeMap<>();

        warnings = "List of non fatal errors produced during parsing:\n\n";
    }

    /**
     * Method to generate the .SRT file
     *
     * @return an array of strings where each String represents a line
     */
    public String[] toSRT() {
        return new FormatSRT().toFile(this);
    }


    /**
     * Method to generate the .ASS file
     *
     * @return an array of strings where each String represents a line
     */
    public String[] toASS() {
        return new FormatASS().toFile(this);
    }

    /**
     * Method to generate the .VTT file
     *
     * @return an array of strings where each String represents a line
     */
    public String[] toVTT() {
        return new FormatVTT().toFile(this);
    }

    /**
     * This method simply checks the style list and eliminate any style not referenced by any caption
     * This might come useful when default styles get created and cover too much.
     * It require a unique iteration through all captions.
     */
    protected void cleanUnusedStyles() {
        //here all used styles will be stored
        Hashtable<String, Style> usedStyles = new Hashtable<String, Style>();
        //we iterate over the captions
        for (Caption current : captions.values()) {
            //new caption
            //if it has a style
            if (current.style != null) {
                String iD = current.style.id;
                //if we haven't saved it yet
                if (!usedStyles.containsKey(iD))
                    usedStyles.put(iD, current.style);
            }
        }
        //we saved the used styles
        this.styling = usedStyles;
    }

}