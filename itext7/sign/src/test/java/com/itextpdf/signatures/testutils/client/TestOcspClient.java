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
package com.itextpdf.signatures.testutils.client;

import com.itextpdf.signatures.IOcspClient;
import com.itextpdf.signatures.testutils.SignTestPortUtil;
import com.itextpdf.signatures.testutils.builder.TestOcspResponseBuilder;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bouncycastle.cert.ocsp.CertificateID;

public class TestOcspClient implements IOcspClient {

    private final Map<String, TestOcspResponseBuilder> issuerIdToResponseBuilder = new LinkedHashMap<>();

    public TestOcspClient addBuilderForCertIssuer(X509Certificate cert, PrivateKey privateKey) throws CertificateEncodingException {
        issuerIdToResponseBuilder.put(cert.getSerialNumber().toString(16), new TestOcspResponseBuilder(cert, privateKey));
        return this;
    }

    public TestOcspClient addBuilderForCertIssuer(X509Certificate cert, TestOcspResponseBuilder builder) throws CertificateEncodingException {
        issuerIdToResponseBuilder.put(cert.getSerialNumber().toString(16), builder);
        return this;
    }

    @Override
    public byte[] getEncoded(X509Certificate checkCert, X509Certificate issuerCert, String url) {
        byte[] bytes = null;
        try {
            CertificateID id = SignTestPortUtil.generateCertificateId(issuerCert, checkCert.getSerialNumber(), CertificateID.HASH_SHA1);
            TestOcspResponseBuilder builder = issuerIdToResponseBuilder.get(issuerCert.getSerialNumber().toString(16));
            if (builder == null) {
                throw new IllegalArgumentException("This TestOcspClient instance is not capable of providing OCSP response for the given issuerCert:" + issuerCert.getSubjectDN().toString());
            }
            bytes = builder.makeOcspResponse(SignTestPortUtil.generateOcspRequestWithNonce(id).getEncoded());
        } catch (Exception ignored) {
            if (ignored instanceof RuntimeException) {
                throw (RuntimeException) ignored;
            }
        }

        return bytes;
    }
}
