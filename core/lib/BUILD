# SPDX-License-Identifier: Apache-2.0
#
# Copyright 2023 The Enola <https://enola.dev> Authors
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

load("@rules_java//java:defs.bzl", "java_library", "java_test")
load("@rules_proto//proto:defs.bzl", "proto_library")

# https://rules-proto-grpc.com/en/latest/lang/buf.html#buf-proto-lint-test
load("@rules_proto_grpc//buf:defs.bzl", "buf_proto_lint_test")

# https://rules-proto-grpc.com/en/latest/lang/doc.html#doc-markdown-compile
load("@rules_proto_grpc//doc:defs.bzl", "doc_markdown_compile")

# https://rules-proto-grpc.com/en/latest/lang/java.html#java-grpc-library
# https://rules-proto-grpc.com/en/latest/lang/java.html#java-proto-library
load("@rules_proto_grpc//java:defs.bzl", "java_grpc_library", "java_proto_library")

# https://rules-proto-grpc.com/en/latest/example.html#step-3-write-a-build-file
proto_library(
    name = "core_proto",
    srcs = ["src/main/java/dev/enola/core/enola_core.proto"],
    deps = [
        "@com_google_protobuf//:any_proto",
        "@com_google_protobuf//:struct_proto",
        "@com_google_protobuf//:timestamp_proto",
    ],
)

proto_library(
    name = "connector_proto",
    srcs = ["src/main/java/dev/enola/core/connector/enola_connector.proto"],
    deps = [":core_proto"],
)

proto_library(
    name = "meta_proto",
    srcs = ["src/main/java/dev/enola/core/meta/enola_meta.proto"],
    # TODO ??? strip_import_prefix = "core/lib/src/main/java/",
    deps = [":core_proto"],
)

proto_library(
    name = "util_proto",
    srcs = ["src/main/java/dev/enola/core/util/enola_util.proto"],
    deps = [
        "@com_google_protobuf//:timestamp_proto",
    ],
)

# proto_library(
#     name = "view_proto",
#     srcs = ["src/main/java/dev/enola/core/enola_view.proto"],
#     deps = [
#         "@com_google_protobuf//:any_proto",
#         "@com_google_protobuf//:struct_proto",
#         "@com_google_protobuf//:timestamp_proto",
#     ],
# )

java_proto_library(
    name = "core_java_proto",
    protos = [
        "connector_proto",
        "core_proto",
        "meta_proto",
        "util_proto",
    ],
    visibility = [
        "//cli:__subpackages__",
        "//connectors:__subpackages__",
        "//core:__subpackages__",
        "//web/ui:__pkg__",
        "//web/view:__pkg__",
    ],
)

java_grpc_library(
    name = "core_java_grpc",
    protos = [
        "connector_proto",
        "core_proto",
    ],
    visibility = [
        "//connectors:__subpackages__",
        "//core:__subpackages__",
        "//web/ui:__pkg__",
        "//web/view:__pkg__",
    ],
)

doc_markdown_compile(
    name = "core_proto_doc",
    protos = [
        "connector_proto",
        "core_proto",
        "meta_proto",
        "util_proto",
    ],
)

buf_proto_lint_test(
    name = "core_buf_proto_lint",
    size = "small",
    except_rules = [
        "PACKAGE_VERSION_SUFFIX",
        "PACKAGE_DIRECTORY_MATCH",
    ],
    protos = [
        "connector_proto",
        "core_proto",
        "meta_proto",
        "util_proto",
    ],
    use_rules = [
        "DEFAULT",
        "COMMENTS",
    ],
)

java_library(
    name = "lib_java",
    srcs = glob(["src/main/java/**/*.java"]),
    visibility = [
        "//cli:__subpackages__",
        "//connectors:__subpackages__",
        "//core:__subpackages__",
        "//web/ui:__pkg__",
        "//web/view:__pkg__",
    ],
    deps = [
        ":core_java_proto",
        "@maven//:com_google_guava_guava",
    ],
)

[java_test(
    name = name[:-len(".java")],
    size = "small",
    srcs = glob(["src/test/java/**/*.java"]),
    resources = glob(["src/test/resources/**/*"]),
    deps = [
        ":core_java_proto",
        ":lib_java",
        "@maven//:com_google_truth_truth",
    ],
) for name in glob([
    "src/test/java/**/*Test.java",
])]
