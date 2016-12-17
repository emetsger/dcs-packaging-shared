/*
 * Copyright 2016 Johns Hopkins University
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
package org.dataconservancy.packaging.shared;

import java.io.InputStream;
import java.net.URI;

/**
 * Creates packages according to a set of parameters, with content supplied by the payload provider.  The payload
 * model ({@code T}) describes the content to be packaged, and its semantics are shared between payload providers and
 * packagers.
 *
 * @param <T> the payload model type
 * @author Elliot Metsger (emetsger@jhu.edu)
 */
public interface Packager<T> {

    /**
     * Creates a package from the given content, metadata and generation parameters.  Implementations are responsible
     * for interrogating the payload model returned by the {@code payloadProvider}, and reasoning over its contents
     * to produce the package.  If binary content is to be included in the package, the {@code payloadProvider} is
     * responsible for resolving the bits, and giving them to this {@code Packager} {@link PayloadProvider#resolve(URI)
     * on request}.
     *
     * @param payloadProvider source of the payload model, domain objects, and binary content
     * @param creationPolicy determines whether or not an entity in the package model is to be included in the
     *                       {@code Package}
     * @param packageMetadata metadata describing the package as a whole
     * @param packageParams package generation parameters
     * @return a populated Package
     * @throws RuntimeException if the package cannot be successfully created
     */
    Package buildPackage(PayloadProvider<T> payloadProvider, Policy<T> creationPolicy,
                         InputStream packageMetadata, InputStream packageParams);

}
