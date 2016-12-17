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

/**
 * Placeholder interface which influences the serialization of a package.  Examples are:
 * <ol>
 *     <li>Limit the size of the package (implies that content beyond this limit would included in a fetch file, or
 *          perhaps multiple bags would be produced as a result)</li>
 *     <li>Include files over a certain size by reference (implies that content beyond this limit would be included in
 *          a fetch file)</li>
 *     <li>The more general case of including or excluding content in the package according to some arbitrary
 *          criteria</li>
 *     <li>Split a logical package into physical packages, e.g. BagIt groups</li>
 * </ol>
 *
 * It is likely that policy implementations will also have to share the payload model, so it can reason about what
 * content to include or exclude based on information in the model.
 *
 * @param <T> the payload model type
 * @author Elliot Metsger (emetsger@jhu.edu)
 */
public interface Policy<T> {

    // TODO

}
