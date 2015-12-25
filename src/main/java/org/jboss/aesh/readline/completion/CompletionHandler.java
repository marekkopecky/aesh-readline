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
package org.jboss.aesh.readline.completion;

import org.jboss.aesh.readline.Buffer;
import org.jboss.aesh.readline.InputProcessor;

import java.util.List;
import java.util.function.Function;

/**
 * @author <a href="mailto:stale.pedersen@jboss.org">Ståle W. Pedersen</a>
 */
public interface CompletionHandler {

    void addCompletion(Completion completion);

    void removeCompletion(Completion completion);

    void clear();

    void setAskDisplayCompletion(boolean askDisplayCompletion);

    boolean doAskDisplayCompletion();

    void setAskCompletionSize(int size);

    int getAskCompletionSize();

    void complete(InputProcessor inputProcessor);

    void setAliasHandler(Function<Buffer, CompleteOperation> aliasHandler);

    void addCompletions(List<Completion> completions);
}