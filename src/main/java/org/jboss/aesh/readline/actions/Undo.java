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
package org.jboss.aesh.readline.actions;

import org.jboss.aesh.readline.InputProcessor;
import org.jboss.aesh.parser.Parser;
import org.jboss.aesh.readline.Action;
import org.jboss.aesh.readline.undo.UndoAction;

/**
 * @author <a href="mailto:stale.pedersen@jboss.org">Ståle W. Pedersen</a>
 */
public class Undo implements Action {

    @Override
    public String name() {
        return "undo";
    }

    @Override
    public void apply(InputProcessor inputProcessor) {
        UndoAction ua = inputProcessor.getBuffer().getUndoManager().getNext();
        if(ua != null) {
            inputProcessor.getBuffer().setBufferLine(Parser.fromCodePoints(ua.getBuffer()));
            inputProcessor.getBuffer().drawLine();
            inputProcessor.getBuffer().moveCursor(ua.getCursorPosition() -
                    inputProcessor.getBuffer().getBuffer().getMultiCursor());
        }
    }
}