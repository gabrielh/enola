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

load("@rules_java//java:defs.bzl", "java_binary", "java_test")

java_binary(
    name = "demo",
    srcs = glob(["src/main/java/**/*.java"]),
    main_class = "dev.enola.demo.Server",
    visibility = ["//core/impl:__subpackages__"],
    # resources = glob(["src/main/resources/**/*"]),
    deps = [
        "//core/lib:core_java_grpc",
        "//core/lib:core_java_proto",
        "//core/lib:lib_java",
        "@maven//:com_google_protobuf_protobuf_java_util",
    ],
)

[java_test(
    name = name[:-len(".java")],
    size = "small",
    srcs = glob(["src/test/java/**/*.java"]),
    runtime_deps = [
        "@maven//:io_grpc_grpc_netty",
    ],
    # resources = glob(["src/test/resources/**/*"]),
    deps = [
        ":demo",
        "//core/lib:lib_java",
        "//core/lib:core_java_grpc",
        "//core/lib:core_java_proto",
        "@maven//:com_google_truth_truth",
        "@maven//:io_grpc_grpc_api",
        "@maven//:org_slf4j_slf4j_simple",
    ],
) for name in glob([
    "src/test/java/**/*Test.java",
])]
