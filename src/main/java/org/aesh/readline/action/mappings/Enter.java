/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aesh.readline.action.mappings;

import org.aesh.util.Config;
import org.aesh.util.Parser;
import org.aesh.readline.ConsoleBuffer;
import org.aesh.readline.InputProcessor;
import org.aesh.readline.action.Action;

/**
 * @author <a href="mailto:stale.pedersen@jboss.org">Ståle W. Pedersen</a>
 */
public class Enter implements Action {

    private static final String ENDS_WITH_BACKSLASH = "\\";
    private static final String HASHTAG = "#";

    @Override
    public String name() {
        return "accept-line";
    }

    @Override
    public void accept(InputProcessor inputProcessor) {
        ConsoleBuffer consoleBuffer = inputProcessor.getBuffer();
        consoleBuffer.getUndoManager().clear();
        boolean isCurrentLineEnding = true;
        if(!consoleBuffer.getBuffer().isMasking()) {// dont push to history if masking
            //dont push lines that end with \ to history
            String buffer = consoleBuffer.getBuffer().asString().trim();
            //lines starting with a hashtag is treated as a comment
            if(buffer.startsWith(HASHTAG)) {
                consoleBuffer.getBuffer().reset();
                inputProcessor.getBuffer().writeOut(Config.CR);
                isCurrentLineEnding = false;
            }
            else if(buffer.endsWith(ENDS_WITH_BACKSLASH)) {
                consoleBuffer.getBuffer().setMultiLine(true);
                consoleBuffer.getBuffer().updateMultiLineBuffer();
                inputProcessor.getBuffer().writeOut(Config.CR);
                isCurrentLineEnding = false;
            }
            else if(Parser.doesStringContainOpenQuote(buffer)) {
                consoleBuffer.getBuffer().setMultiLine(true);
                consoleBuffer.getBuffer().updateMultiLineBuffer();
                inputProcessor.getBuffer().writeOut(Config.CR);
                isCurrentLineEnding = false;
            }
            else if( inputProcessor.getBuffer().getHistory().isEnabled()) {
                if(consoleBuffer.getBuffer().isMultiLine())
                   inputProcessor.getBuffer().getHistory().push(consoleBuffer.getBuffer().getMultiLine());
                else
                    inputProcessor.getBuffer().getHistory().push(consoleBuffer.getBuffer().getMultiLine());
            }
        }

        if(isCurrentLineEnding)
            consoleBuffer.moveCursor(consoleBuffer.getBuffer().length());

        if(isCurrentLineEnding) {
            inputProcessor.setReturnValue(consoleBuffer.getBuffer().getMultiLine());
            consoleBuffer.getBuffer().reset();
        }
        else
            consoleBuffer.drawLine();
    }
}
