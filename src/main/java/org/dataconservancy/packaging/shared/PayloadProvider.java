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

import org.apache.jena.rdf.model.Model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Content providers are responsible for answering models representing the content to be packaged, and resolving URIs
 * to byte streams.  Content providers and packagers are expected to reason over a common payload model, {@code T}.
 *
 * @param <T> the payload model type
 * @author Elliot Metsger (emetsger@jhu.edu)
 */
public interface PayloadProvider<T> {

    /**
     * Answers a structural view of the content contained in the package.  Packagers are expected to be able to reason
     * over the model returned by this method in order to produce a {@code Package} instance.
     * <p>
     * The model returned by this method is expected to contain not only structural information (e.g. a hierarchical
     * representation of the content), but also metadata about any binary content contained in the package (e.g.
     * checksums, content type, content length).  Entities in the model are expected to be identified by URIs.
     * </p>
     *
     * @return the structural model of the content being packaged
     */
    T getPayloadModel();

    /**
     * Answers a domain view of the content contained in the package.  Models returned by this method are specific to
     * the domain of the content being packaged.  Packagers are not expected to reason about the content of this model,
     * but may include it "as-is" in the contents of a Package.
     *
     * @return the opaque domain model of the content being packaged
     */
    Model getDomainModel();

    /**
     * Resolves a URI to a byte stream.  The URIs supplied to this method are known to the underlying content provider
     * because they would be included in the {@link #getPayloadModel() structural model}.  If a URI is not known to the
     * this content provider, an {@code IllegalArgumentException} may be thrown.  Metadata for the returned stream (e.g.
     * size, checksums, mime type, etc.) are expected to be supplied in the {@link #getPayloadModel() structural model},
     * so the {@code ContentProvider} has no explicit methods for retrieving metadata of binary content.
     *
     * @param contentUri a URI known to the content provider that identifies, or resolves to, a byte stream
     * @return the content referenced by the {@code contentUri}
     * @throws IllegalArgumentException if the {@code contentUri} is unknown to the content provider
     * @throws IOException if the {@code contentUri} is known to the content provider, but cannot be retrieved
     */
    InputStream resolve(URI contentUri) throws IOException;

}