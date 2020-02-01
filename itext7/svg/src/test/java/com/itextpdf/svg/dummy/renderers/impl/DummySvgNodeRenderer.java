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
package com.itextpdf.svg.dummy.renderers.impl;

import com.itextpdf.svg.SvgConstants;
import com.itextpdf.svg.renderers.ISvgNodeRenderer;
import com.itextpdf.svg.renderers.SvgDrawContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A dummy implementation of {@link ISvgNodeRenderer} for testing purposes
 */
public class DummySvgNodeRenderer implements ISvgNodeRenderer {

    Map<String, String> attributes;
    ISvgNodeRenderer parent;
    String name;
    boolean drawn = false;

    public DummySvgNodeRenderer() {
        this("dummy");
    }

    public DummySvgNodeRenderer(String name) {
        this(name, new HashMap<String, String>());
    }

    public DummySvgNodeRenderer(String name, Map<String, String> attr) {
        this.name = name;
        this.attributes = attr;
    }

    @Override
    public void setParent(ISvgNodeRenderer parent) {
        this.parent = parent;
    }

    @Override
    public ISvgNodeRenderer getParent() {
        return parent;
    }


    @Override
    public void draw(SvgDrawContext context) {
        System.out.println(name + ": Drawing in dummy node");
        this.drawn = true;
    }

    @Override
    public void setAttributesAndStyles(Map<String, String> attributesAndStyles) {
        this.attributes = attributesAndStyles;
    }

    @Override
    public String getAttribute(String key) {
        if (SvgConstants.Attributes.WIDTH.equalsIgnoreCase(key) ||
            SvgConstants.Attributes.HEIGHT.equalsIgnoreCase(key)) {
            return "10";
        }
        return this.attributes.get(key);
    }

    @Override
    public void setAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    @Override
    public Map<String, String> getAttributeMapCopy() {
        return null;
    }

    @Override
    public ISvgNodeRenderer createDeepCopy() {
        return new DummySvgNodeRenderer(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DummySvgNodeRenderer)) {
            return false;
        }
        //Name
        DummySvgNodeRenderer otherDummy = (DummySvgNodeRenderer) o;
        return this.name.equals(otherDummy.name);
    }

    public boolean isDrawn() {
        return this.drawn;
    }

}
