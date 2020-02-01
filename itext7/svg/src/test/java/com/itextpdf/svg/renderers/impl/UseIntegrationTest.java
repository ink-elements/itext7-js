/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/

    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.

    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.

    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.

    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.svg.renderers.impl;

import com.itextpdf.svg.processors.ISvgConverterProperties;
import com.itextpdf.svg.processors.impl.SvgConverterProperties;
import com.itextpdf.svg.renderers.SvgIntegrationTest;
import com.itextpdf.test.ITextTest;
import com.itextpdf.test.annotations.type.IntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;

@Category(IntegrationTest.class)
public class UseIntegrationTest extends SvgIntegrationTest {

    private static final String SOURCE_FOLDER = "./src/test/resources/com/itextpdf/svg/renderers/impl/UseIntegrationTest/";
    private static final String DESTINATION_FOLDER = "./target/test/com/itextpdf/svg/renderers/impl/UseIntegrationTest/";

    private ISvgConverterProperties properties;

    @BeforeClass
    public static void beforeClass() {
        ITextTest.createDestinationFolder(DESTINATION_FOLDER);
    }

    @Before
    public void before() {
        properties = new SvgConverterProperties().setBaseUri(SOURCE_FOLDER);
    }

    @Test
    public void singleUseTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "singleUse");
    }

    @Test
    public void singleUseFillTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "singleUseFill");
    }

    @Test
    public void doubleNestedUseFillTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "doubleNestedUseFill");
    }

    @Test
    public void singleUseStrokeTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "singleUseStroke");
    }

    @Test
    public void doubleNestedUseStrokeTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "doubleNestedUseStroke");
    }

    @Test
    public void translateUseTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "translateUse");
    }

    @Test
    public void multipleTransformationsUseTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "multipleTransformationsUse");
    }

    @Test
    public void coordinatesUseTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "coordinatesUse");
    }

    @Test
    public void imageUseTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "imageUse", properties);
    }

    @Test
    public void svgUseTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "svgUse", properties);
    }

    @Test
    public void complexUseTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "complexUse", properties);
    }

    @Test
    public void UseWithoutDefsTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "useWithoutDefs", properties);
    }

    @Test
    public void UseWithoutDefsUsedElementAfterUseTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "useWithoutDefsUsedElementAfterUse", properties);
    }

    @Test
    public void simpleRectReuseTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "simpleRectReuse", properties);
    }

    @Test
    public void transitiveTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "transitive", properties);
    }

    @Test
    public void circularTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "circular", properties);
    }

    @Test
    public void complexReferencesTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "complexReferences", properties);
    }

    @Test
    public void transformationsOnTransformationsTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "transformationsOnTransformations", properties);
    }

    @Test
    public void reuseLinesTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "reuseLines", properties);
    }

    @Test
    public void missingHashtagTest() throws IOException,InterruptedException {
        convertAndCompareSinglePage(SOURCE_FOLDER, DESTINATION_FOLDER, "missingHashtag", properties);
    }

    @Test
    public void useInDifferentFilesExampleTest() throws IOException, InterruptedException {
        //TODO: update when DEVSIX-2252 fixed
        convertAndCompare(SOURCE_FOLDER, DESTINATION_FOLDER, "useInDifferentFilesExampleTest");
    }
}
