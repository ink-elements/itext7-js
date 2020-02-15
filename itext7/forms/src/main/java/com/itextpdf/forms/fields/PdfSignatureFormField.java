/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: Bruno Lowagie, Paulo Soares, et al.

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
package com.itextpdf.forms.fields;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.annot.PdfWidgetAnnotation;
import com.itextpdf.forms.PdfSigFieldLock;


/**
 * An AcroForm field containing signature data.
 */
public class PdfSignatureFormField extends PdfFormField {

    protected PdfSignatureFormField(PdfDocument pdfDocument) {
        super(pdfDocument);
    }

    protected PdfSignatureFormField(PdfWidgetAnnotation widget, PdfDocument pdfDocument) {
        super(widget, pdfDocument);
    }

    protected PdfSignatureFormField(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    /**
     * Returns <code>Sig</code>, the form type for signature form fields.
     * 
     * @return the form type, as a {@link PdfName}
     */
    @Override
    public PdfName getFormType() {
        return PdfName.Sig;
    }

    /**
     * Adds the signature to the signature field.
     * 
     * @param value the signature to be contained in the signature field, or an indirect reference to it
     * @return the edited field
     */
    public PdfSignatureFormField setValue(PdfObject value) {
        return (PdfSignatureFormField) put(PdfName.V, value);
    }

    /**
     * Gets the {@link PdfSigFieldLock}, which contains fields that
     * must be locked if the document is signed.
     * 
     * @return a dictionary containing locked fields.
     * @see PdfSigFieldLock
     */
    public PdfSigFieldLock getSigFieldLockDictionary() {
        PdfDictionary sigLockDict = (PdfDictionary) getPdfObject().get(PdfName.Lock);
        return sigLockDict == null ? null : new PdfSigFieldLock(sigLockDict);
    }
}