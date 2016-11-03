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
package org.jboss.aesh.readline.alias;

import org.jboss.aesh.readline.Prompt;
import org.jboss.aesh.readline.Readline;
import org.jboss.aesh.tty.terminal.TerminalConnection;
import org.jboss.aesh.util.Config;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:stale.pedersen@jboss.org">Ståle W. Pedersen</a>
 */
public class ReadlineAliasTest {
    @Test
    public void alias() throws Exception {
        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(outputStream);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TerminalConnection connection = new TerminalConnection(pipedInputStream, out);
        Readline readline = new Readline();

        File aliasFile = Config.isOSPOSIXCompatible() ?
                new File("src/test/resources/alias1") : new File("src\\test\\resources\\alias1");
        AliasManager aliasManager = new AliasManager(aliasFile, false);
        AliasPreProcessor aliasPreProcessor = new AliasPreProcessor(aliasManager);
        List<Function<String, Optional<String>>> preProcessors = new ArrayList<>();
        preProcessors.add(aliasPreProcessor);

        final String[] line = new String[1];
        readline.readline(connection, new Prompt(""), s -> {
            line[0] = s;
        }, null, preProcessors);

        connection.openNonBlocking();
        outputStream.write(("ll"+Config.getLineSeparator()).getBytes());
        outputStream.flush();
        Thread.sleep(150);
        assertEquals("ls -alF", line[0]);

        readline.readline(connection, new Prompt(""), s -> {
            line[0] = s;
        }, null, preProcessors);

        connection.openNonBlocking();
        outputStream.write(("grep -l"+Config.getLineSeparator()).getBytes());
        outputStream.flush();
        Thread.sleep(150);
        assertEquals("grep --color=auto -l", line[0]);
    }
}