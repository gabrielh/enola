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

# https://rules-proto-grpc.com/en/latest/lang/java.html#java-proto-library
load("@rules_proto//proto:defs.bzl", "proto_library")
load("@rules_proto_grpc//java:defs.bzl", "java_proto_library")

proto_library(
    name = "validation_proto",
    srcs = ["src/main/proto/validation.proto"],
    deps = [
        "@com_google_protobuf//:any_proto",
    ],
)

java_proto_library(
    name = "validation_proto_java_library",
    protos = [
        "validation_proto",
    ],
    visibility = [
        "//core:__subpackages__",
    ],
)

java_library(
    name = "protobuf",
    srcs = glob(["src/main/java/**/*.java"]),
    visibility = [
        "//connectors:__subpackages__",
        "//core:__subpackages__",
        "//web/ui:__pkg__",
    ],
    deps = [
        ":validation_proto_java_library",
        "//common/common",
        "//common/yamljson",
        "@maven//:com_google_errorprone_error_prone_annotations",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_protobuf_protobuf_java",
        "@maven//:com_google_protobuf_protobuf_java_util",
        "@maven//:junit_junit",
    ],
)

[java_test(
    name = name[:-len(".java")],
    size = "small",
    srcs = glob(["src/test/java/**/*.java"]),
    resources = glob(["src/test/resources/**/*"]),
    deps = [
        ":protobuf",
        "//common/common",
        "@maven//:com_google_protobuf_protobuf_java",
        "@maven//:com_google_truth_truth",
    ],
) for name in glob([
    "src/test/java/**/*Test.java",
])]
