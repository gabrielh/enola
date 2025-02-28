/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2023 The Enola <https://enola.dev> Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.enola.cli;

import dev.enola.common.markdown.exec.ExecMD;

import picocli.CommandLine;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "execmd",
        description = "Execute Commands in Markdown",
        showDefaultValues = true)
public class ExecMdCommand implements Callable<Integer> {

    @CommandLine.Parameters(
            index = "0..*",
            arity = "1..*",
            description = "Markdown (MD) files to process")
    List<File> files;

    @CommandLine.Option(
            names = {"--in-place", "-i"},
            defaultValue = "false",
            description = "Edit MD files in place")
    boolean inplace;

    @Override
    public Integer call() throws Exception {
        return new ExecMD().process(files, inplace);
    }
}
