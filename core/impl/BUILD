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

java_library(
    name = "impl",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/resources/**/*"]),
    visibility = [
        "//cli:__subpackages__",
        "//web/ui:__pkg__",
        "//web/view:__pkg__",
    ],
    deps = [
        "//common/common",
        "//common/protobuf",
        "//common/protobuf:validation_proto_java_library",
        "//common/yamljson",
        "//core/lib:core_java_grpc",
        "//core/lib:core_java_proto",
        "//core/lib:lib_java",
        "@maven//:com_github_java_json_tools_uri_template",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_protobuf_protobuf_java_util",
        "@maven//:com_google_template_soy",
        "@maven//:io_grpc_grpc_api",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

# TODO load("@io_bazel_rules_closure//closure:defs.bzl", "closure_java_template_library")
# TODO This is only useful after https://github.com/bazelbuild/rules_closure/issues/225
# closure_java_template_library(
#    name = "soy",
#    srcs = glob(["src/main/resources/**/*.soy"]),
#    java_package = "dev.enola.core.meta.docgen",
#    proto_deps = ["//core/lib:core_java_proto"],
# )

[java_test(
    name = name[:-len(".java")],
    size = "small",
    srcs = glob(["src/test/java/**/*.java"]),
    resources = glob(["src/test/resources/**/*"]),
    runtime_deps = [
        "@maven//:io_grpc_grpc_netty",
    ],
    deps = [
        ":impl",
        "//common/common",
        "//common/protobuf",
        "//common/yamljson",  # TODO Remove this once dependencies are cleaned up
        "//connectors/demo",
        "//core/lib:core_java_proto",
        "//core/lib:lib_java",
        "@maven//:com_google_truth_truth",
    ],
) for name in glob([
    "src/test/java/**/*Test.java",
])]
