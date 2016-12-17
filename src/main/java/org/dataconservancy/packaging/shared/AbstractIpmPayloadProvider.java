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

import org.dataconservancy.packaging.tool.model.ipm.Node;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An IPM payload provider which provides a general mechanism for walking the IPM model, evaluating potential nodes as
 * containing bytestreams, and delegating the resolution of those bytestreams to subclasses.
 * <p>
 * Sub-classes of this payload provider and its associated {@code Packager} share the IPM model as a common semantic.
 * Implementations are knowledgeable of IPM semantics and can reason over an instance of an IPM model to implement their
 * responsibilities.
 * </p>
 *
 * @author Elliot Metsger (emetsger@jhu.edu)
 */
public abstract class AbstractIpmPayloadProvider implements PayloadProvider<Node> {

    /**
     * Represents the root {@link Node} of the content being packaged.  Must not be {@code null}.
     */
    protected Node rootNode;

    /**
     * {@inheritDoc}
     * <p>
     * Implementation notes: walks the IPM tree for {@code FileInfo} objects whose location equals the
     * {@code payloadUri}, or whose parent {@code Node} equals the {@code payloadUri}, or whose domain object equals the
     * {@code payloadUri}.  Delegates the resolution of the URI to {@link #resolveLocation(URI)}.
     * </p>
     *
     * @param payloadUri {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws IllegalStateException if the IPM root node is {@code null}
     */
    @Override
    public InputStream resolve(final URI payloadUri) throws IOException {
        if (rootNode == null) {
            throw new IllegalStateException("'rootNode' must not be null.");
        }

        final AtomicReference<Optional<InputStream>> content = new AtomicReference<>();
        final AtomicReference<Optional<IOException>> ex = new AtomicReference<>();

        rootNode.walk(n -> {
            if (n.getIdentifier() == payloadUri ||
                    n.getFileInfo() != null && n.getFileInfo().getLocation().equals(payloadUri) ||
                    n.getDomainObject().equals(payloadUri)) {
                try {
                    content.set(Optional.ofNullable(resolveLocation(n.getFileInfo().getLocation())));
                } catch (IOException e) {
                    ex.set(Optional.of(e));
                }
            }
        });

        if (ex.get().isPresent()) {
            throw ex.get().get();
        }

        return content.get()
                .orElseThrow(() ->
                        new IllegalArgumentException("No bytestream could be resolved for " + payloadUri.toString()));
    }

    /**
     * Resolves an IPM {@link org.dataconservancy.packaging.tool.model.ipm.FileInfo#getLocation()} to a bytestream.
     *
     * @param location a URI corresponding to a location of a bytestream
     * @return the bytes, or {@code null} if the URI is unknown to the content provider
     * @throws IOException if the URI is known to the content provider, but the bytes cannot be retrieved
     */
    protected abstract InputStream resolveLocation(URI location) throws IOException;

}
