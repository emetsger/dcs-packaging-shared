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

import org.dataconservancy.packaging.tool.model.PackageGenerationParameters;
import org.dataconservancy.packaging.tool.model.ParametersBuildException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

import static org.dataconservancy.packaging.tool.api.PackagingFormat.BOREM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Various unit tests documenting IpmPackager behavior.
 *
 * @author Elliot Metsger (emetsger@jhu.edu)
 */
public class IpmPackagerTest {

    /**
     * Insures {@link IpmPackager#getGenerationParameters(java.io.InputStream)} behaves as documented when a
     * {@code null} parameters stream is supplied.
     *
     * @throws ParametersBuildException if parameters cannot be generated
     */
    @Test
    public void testNullGenerationParameters() throws ParametersBuildException {
        final IpmPackager underTest = new IpmPackager();
        final PackageGenerationParameters result = underTest.getGenerationParameters(null);
        assertNotNull(result);

        assertTrue(result.getParam("Package-Location") != null && result.getParam("Package-Location").size() == 1);
        assertTrue(result.getParam("Package-Name") != null && result.getParam("Package-Name").size() == 1);
        assertTrue(result.getParam("Package-Format-Id") != null && result.getParam("Package-Format-Id").size() == 1);
        assertTrue(result.getParam("BagIt-Profile-Identifier") != null &&
                result.getParam("BagIt-Profile-Identifier").size() == 1);
    }

    /**
     * Document the default values for 'Package-Name', 'Package-Location', 'BagIt-Profile-Identifier', and
     * 'Package-Format-Id'.
     *
     * @throws ParametersBuildException if parameters cannot be generated
     */
    @Test
    public void testDefaultGenerationParameters() throws ParametersBuildException {
        final IpmPackager underTest = new IpmPackager();
        final String name = "MyPackage";
        final String location = System.getProperty("java.io.tmpdir");
        final String format = BOREM.name();
        final String profile = "http://dataconservancy.org/formats/data-conservancy-pkg-1.0";
        underTest.setPackageName(name);
        underTest.setPackageLocation(location);
        final PackageGenerationParameters result = underTest.getGenerationParameters(null);
        assertNotNull(result);

        assertEquals(location, result.getParam("Package-Location").get(0));
        assertEquals(name, result.getParam("Package-Name").get(0));
        assertEquals(format, result.getParam("Package-Format-Id").get(0));
        assertEquals(profile, result.getParam("BagIt-Profile-Identifier").get(0));
    }

    /**
     * Insure supplied values for 'Package-Name' and 'Package-Location' are used when a {@code null} parameters stream
     * is supplied.
     *
     * @throws ParametersBuildException if parameters cannot be generated
     */
    @Test
    public void testSuppliedGenerationParameters() throws Exception {
        final String name = "FooName";
        final String location = "FooLocation";
        final IpmPackager underTest = new IpmPackager();
        underTest.setPackageName(name);
        underTest.setPackageLocation(location);
        final PackageGenerationParameters result = underTest.getGenerationParameters(null);
        assertNotNull(result);

        assertEquals(location, result.getParam("Package-Location").get(0));
        assertEquals(name, result.getParam("Package-Name").get(0));
    }

    /**
     * Insure the values for 'Package-Name' and 'Package-Location' from the parameters stream override parameters
     * supplied by the setters on {@code IpmPackager}
     *
     * @throws ParametersBuildException if parameters cannot be generated
     */
    @Test
    public void testSuppliedGenerationParametersOverridesStream() throws Exception {
        final String name = "BarName";
        final String location = "BarLocation";
        final IpmPackager underTest = new IpmPackager();

        underTest.setPackageName("FooName");
        underTest.setPackageLocation("FooLocation");

        final ByteArrayOutputStream storedParams = new ByteArrayOutputStream();
        final Properties params = new Properties();
        params.setProperty("Package-Location", location);
        params.setProperty("Package-Name", name);
        params.store(storedParams, null);

        final PackageGenerationParameters result = underTest.getGenerationParameters(
                new ByteArrayInputStream(storedParams.toByteArray()));
        assertNotNull(result);

        assertEquals(location, result.getParam("Package-Location").get(0));
        assertEquals(name, result.getParam("Package-Name").get(0));
    }

}