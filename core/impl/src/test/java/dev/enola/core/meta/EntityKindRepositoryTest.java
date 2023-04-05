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
package dev.enola.core.meta;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import dev.enola.common.io.resource.ClasspathResource;
import dev.enola.common.protobuf.ValidationException;
import dev.enola.core.IDs;
import dev.enola.core.meta.proto.EntityKind;
import dev.enola.core.proto.ID;
import java.io.IOException;
import org.junit.Test;

public class EntityKindRepositoryTest {

  EntityKindRepository r = new EntityKindRepository();

  @Test
  public void testEmptyRepository() throws ValidationException {
    assertThat(r.list()).isEmpty();
    assertThat(r.listID()).isEmpty();

    var id = ID.newBuilder().setEntity("non-existant").build();
    assertThrows(IllegalArgumentException.class, () -> r.get(id));
  }

  @Test
  public void testBasics() throws ValidationException {
    var id = ID.newBuilder().setNs("testNS").setEntity("testEntity").build();
    var ek = EntityKind.newBuilder().setId(id).build();

    r.put(ek);
    assertThat(r.get(id)).isEqualTo(ek);
    assertThat(r.listID()).containsExactly(id);
    assertThat(r.list()).containsExactly(ek);
  }

  @Test
  public void testEmptyNamespace() throws ValidationException {
    var id = ID.newBuilder().setEntity("testEntity").build();
    var ek = EntityKind.newBuilder().setId(id).build();

    r.put(ek);
    assertThat(r.get(id)).isEqualTo(ek);
    assertThat(r.listID()).containsExactly(id);
    assertThat(r.list()).containsExactly(ek);
  }

  @Test
  public void testEmptyName() {
    var id = ID.newBuilder().build();
    var ek = EntityKind.newBuilder().setId(id).build();

    assertThrows(IllegalArgumentException.class, () -> r.get(id));
    assertThrows(ValidationException.class, () -> r.put(ek));
  }

  @Test
  public void testLoad() throws ValidationException, IOException {
    r.load(new ClasspathResource("demo-model.textproto"));
    assertThat(r.listID())
        .containsExactly(
            IDs.parse("demo.foo/name"), IDs.parse("demo.bar/foo/name"), IDs.parse("demo.baz/uuid"));
    assertThat(r.list()).hasSize(3);
  }
}
