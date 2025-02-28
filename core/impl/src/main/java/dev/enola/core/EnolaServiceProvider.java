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
package dev.enola.core;

import dev.enola.common.protobuf.ValidationException;
import dev.enola.core.aspects.*;
import dev.enola.core.meta.EntityKindRepository;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class EnolaServiceProvider {

    public EnolaService get(EntityKindRepository ekr) throws ValidationException {
        var r = new EnolaServiceRegistry();
        for (var ek : ekr.list()) {
            var s = new EntityAspectService(ek);

            // The order in which we add Aspects (AKA Connectors) here matters, a lot!
            // First come the end users custom (and possibly remove...) connectors.
            // Then come our hard-coded fix internal ones - in a particular order.

            for (var c : ek.getConnectorsList()) {
                switch (c.getTypeCase()) {
                    case ERROR:
                        s.add(new ErrorTestAspect(c.getError()));
                        break;

                    case JAVA_CLASS:
                        var className = c.getJavaClass();
                        try {
                            var clazz = Class.forName(className);
                            var object = clazz.getDeclaredConstructor().newInstance();
                            EntityAspect connector = (EntityAspect) object;
                            s.add(connector);
                            break;

                        } catch (ClassNotFoundException
                                | NoSuchMethodException
                                | InstantiationException
                                | IllegalAccessException
                                | InvocationTargetException e) {
                            // TODO Full ValidationException instead of IllegalArgumentException
                            throw new IllegalArgumentException(
                                    "Java Class Connector failure for EntityKind: " + ek.getId(),
                                    e);
                        }

                        // TODO JAVA_GUICE Registry lookup?

                    case FS:
                        var fs = c.getFs();
                        s.add(new FilestoreRepositoryAspect(Path.of(fs.getPath()), fs.getFormat()));
                        break;

                    case GRPC:
                        s.add(new GrpcAspect(c.getGrpc()));
                        break;

                    case TYPE_NOT_SET:
                        // TODO Full ValidationException instead of IllegalArgumentException
                        throw new IllegalArgumentException(
                                "Connector Type not set in EntityKind: " + ek.getId());
                }
            }

            s.add(new UriTemplateAspect(ek));
            s.add(new TimestampAspect());
            s.add(new ValidationAspect());

            r.register(ek.getId(), s);
        }
        return r;
    }
}
