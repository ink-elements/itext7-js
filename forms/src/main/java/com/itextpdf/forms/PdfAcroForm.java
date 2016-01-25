package com.itextpdf.forms;

import com.itextpdf.basics.PdfException;
import com.itextpdf.basics.geom.Rectangle;
import com.itextpdf.core.pdf.canvas.PdfCanvas;
import com.itextpdf.core.pdf.PdfArray;
import com.itextpdf.core.pdf.PdfBoolean;
import com.itextpdf.core.pdf.PdfDictionary;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfIndirectReference;
import com.itextpdf.core.pdf.PdfName;
import com.itextpdf.core.pdf.PdfNumber;
import com.itextpdf.core.pdf.PdfObject;
import com.itextpdf.core.pdf.PdfObjectWrapper;
import com.itextpdf.core.pdf.PdfPage;
import com.itextpdf.core.pdf.PdfStream;
import com.itextpdf.core.pdf.PdfString;
import com.itextpdf.core.pdf.annot.PdfAnnotation;
import com.itextpdf.core.pdf.xobject.PdfFormXObject;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.xfa.XfaForm;
import com.itextpdf.forms.xfa.Xml2Som;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;

/**
 * This class represents the static form technology AcroForm on a PDF file.
 */
public class PdfAcroForm extends PdfObjectWrapper<PdfDictionary> {

    /**
     * To be used with {@link #setSignatureFlags}.
     * 
     * <blockquote>
     * If set, the document contains at least one signature field. This flag
     * allows a conforming reader to enable user interface items (such as menu
     * items or pushbuttons) related to signature processing without having to
     * scan the entire document for the presence of signature fields.
     * (ISO 32000-1, section 12.7.2 "Interactive Form Dictionary")
     * </blockquote>
     */
    static public final int SIGNATURE_EXIST = 1;
    /**
     * To be used with {@link #setSignatureFlags}.
     * 
     * <blockquote>
     * If set, the document contains signatures that may be invalidated if the
     * file is saved (written) in a way that alters its previous contents, as
     * opposed to an incremental update. Merely updating the file by appending
     * new information to the end of the previous version is safe. Conforming
     * readers may use this flag to inform a user requesting a full save that
     * signatures will be invalidated and require explicit confirmation before
     * continuing with the operation.
     * (ISO 32000-1, section 12.7.2 "Interactive Form Dictionary")
     * </blockquote>
     */
    static public final int APPEND_ONLY = 2;

    /**
     * Keeps track of whether or not appearances must be generated by the form
     * fields themselves, or by the PDF viewer application. Default is
     * <code>true</code>.
     */
    protected boolean generateAppearance = true;
    
    /**
     * A map of field names and their associated {@link PdfFormField form field}
     * objects.
     */
    protected Map<String, PdfFormField> fields = new LinkedHashMap<>();
    
    /**
     * The PdfDocument to which the PdfAcroForm belongs.
     */
    protected PdfDocument document;

    private static PdfName resourceNames[] = {PdfName.Font, PdfName.XObject, PdfName.ColorSpace, PdfName.Pattern};
    private PdfDictionary defaultResources;
    private Set<PdfFormField> fieldsForFlattening = new LinkedHashSet<>();
    private XfaForm xfaForm;

    /**
     * Creates a PdfAcroForm as a wrapper of a dictionary.
     * Also initializes an XFA form if an <code>/XFA</code> entry is present in
     * the dictionary.
     * 
     * @param pdfObject the PdfDictionary to be wrapped
     */
    public PdfAcroForm(PdfDictionary pdfObject) {
        super(pdfObject);
        getFormFields();
        xfaForm = new XfaForm(pdfObject);
    }

    /**
     * Creates a PdfAcroForm from a {@link PdfArray} of fields.
     * Also initializes an empty XFA form.
     * 
     * @param fields a {@link PdfArray} of {@link PdfDictionary} objects
     */
    public PdfAcroForm(PdfArray fields) {
        super(new PdfDictionary());
        put(PdfName.Fields, fields);
        getFormFields();
        xfaForm = new XfaForm();
    }

    /**
     * Retrieves AcroForm from the document. If there is no AcroForm in the
     * document Catalog and createIfNotExist flag is true then the AcroForm
     * dictionary will be created and added to the document.
     *
     * @param document the document to retrieve the {@link PdfAcroForm} from
     * @param createIfNotExist when <code>true</code>, this method will create a {@link PdfAcroForm} if none exists for this document
     * @return the {@link PdfDocument document}'s AcroForm, or a new one
     */
    public static PdfAcroForm getAcroForm(PdfDocument document, boolean createIfNotExist) {
        PdfDictionary acroFormDictionary = document.getCatalog().getPdfObject().getAsDictionary(PdfName.AcroForm);
        PdfAcroForm acroForm = null;
        if (acroFormDictionary == null) {
            if (createIfNotExist) {
                acroForm = new PdfAcroForm(new PdfArray()).makeIndirect(document);
                document.getCatalog().put(PdfName.AcroForm, acroForm);
                document.getCatalog().setModified();
                acroForm.setDefaultAppearance("/Helv 0 Tf 0 g ");
            }
        } else {
            acroForm = new PdfAcroForm(acroFormDictionary);
        }

        if (acroForm != null) {
            acroForm.defaultResources = acroForm.getDefaultResources();
            if (acroForm.defaultResources == null) {
                acroForm.defaultResources = new PdfDictionary();
            }
            acroForm.document = document;
            acroForm.xfaForm = new XfaForm(document);
        }



        return acroForm;
    }

    /**
     * This method adds the field to the last page in the document.
     * If there's no pages, creates a new one.
     *
     * @param field the {@link PdfFormField} to be added to the form
     */
    public void addField(PdfFormField field) {
        PdfPage page;
        if (document.getNumberOfPages() == 0){
            document.addNewPage();
        }
        page = document.getLastPage();
        addField(field, page);
    }

    /**
     * This method adds the field to a specific page.
     * @param field the {@link PdfFormField} to be added to the form
     * @param page the {@link PdfPage} on which to add the field
     */
    public void addField(PdfFormField field, PdfPage page){
        PdfArray kids = field.getKids();

        PdfDictionary fieldDic = field.getPdfObject();
        if (kids != null){
            processKids(kids, fieldDic, page);
        }

        getFields().add(fieldDic);
        fields.put(field.getFieldName().toUnicodeString(), field);

        if (field.getFormType() != null && field.getFormType().equals(PdfName.Tx)) {
            List<PdfDictionary> resources = getResources(field.getPdfObject());
            for (PdfDictionary resDict : resources) {
                mergeResources(defaultResources, resDict, field);
            }
            if (!defaultResources.isEmpty()) {
                put(PdfName.DR, defaultResources);
            }
        }
        if (fieldDic.containsKey(PdfName.Subtype) && page != null) {
            PdfArray annots = page.getPdfObject().getAsArray(PdfName.Annots);
            if (annots == null) {
                annots = new PdfArray();
                page.getPdfObject().put(PdfName.Annots, annots);
            }
            if (!annots.contains(fieldDic)) {
                annots.add(fieldDic);
            }
        }
    }

    /**
     * This method merges field with its annotation and place it on the given
     * page. This method won't work if the field has no or more than one widget
     * annotations.
     * @param field the {@link PdfFormField} to be added to the form
     * @param page the {@link PdfPage} on which to add the field
     */
    public void addFieldAppearanceToPage(PdfFormField field, PdfPage page) {
        PdfDictionary fieldDict = field.getPdfObject();
        PdfArray kids = field.getKids();
        if (kids == null || kids.size() > 1) {
            return;
        }

        PdfDictionary kidDict = (PdfDictionary) kids.get(0);
        PdfName type = kidDict.getAsName(PdfName.Subtype);
        if (type != null && type.equals(PdfName.Widget)) {
            fieldDict.remove(PdfName.Kids);
            kidDict.remove(PdfName.Parent);
            fieldDict.mergeDifferent(kidDict);
            PdfAnnotation annot = PdfAnnotation.makeAnnotation(fieldDict);
            PdfDictionary pageDic = annot.getPdfObject().getAsDictionary(PdfName.P);
            if (pageDic != null) {
                PdfArray array = pageDic.getAsArray(PdfName.Annots);
                if (array == null) {
                    array = new PdfArray();
                    pageDic.put(PdfName.Annots, array);
                }
                array.add(fieldDict);
            } else {
                page.addAnnotation(annot);
            }
        }
    }

    /**
     * Gets the {@link PdfFormField form field}s as a {@link Map}.
     * 
     * @return a map of field names and their associated {@link PdfFormField form field} objects
     */
    public Map<String, PdfFormField> getFormFields() {
        if (fields.isEmpty()) {
            fields = iterateFields(getFields());
        }
        return fields;
    }

    /**
     * Sets the <code>NeedAppearances</code> boolean property on the AcroForm.
     * 
     * <blockquote>
     * NeedAppearances is a flag specifying whether to construct appearance
     * streams and appearance dictionaries for all widget annotations in the
     * document.
     * (ISO 32000-1, section 12.7.2 "Interactive Form Dictionary")
     * </blockquote>
     * 
     * @param needAppearances a boolean. Default value is <code>false</code>
     * @return current AcroForm.
     */
    public PdfAcroForm setNeedAppearances(boolean needAppearances) {
        return put(PdfName.NeedAppearances, new PdfBoolean(needAppearances));
    }

    /**
     * Gets the <code>NeedAppearances</code> boolean property on the AcroForm.
     * 
     * <blockquote>
     * NeedAppearances is a flag specifying whether to construct appearance
     * streams and appearance dictionaries for all widget annotations in the
     * document.
     * (ISO 32000-1, section 12.7.2 "Interactive Form Dictionary")
     * </blockquote>
     * 
     * @return the <code>NeedAppearances</code> property as a {@link PdfBoolean}. Default value is <code>false</code>
     */
    public PdfBoolean getNeedAppearances() {
        return getPdfObject().getAsBoolean(PdfName.NeedAppearances);
    }

    /**
     * Sets the <code>SigFlags</code> integer property on the AcroForm.
     * 
     * <blockquote>
     * SigFlags is a set of flags specifying various document-level
     * characteristics related to signature fields.
     * (ISO 32000-1, section 12.7.2 "Interactive Form Dictionary")
     * </blockquote>
     * 
     * @param sigFlags an integer. Use {@link #SIGNATURE_EXIST} and/or {@link #APPEND_ONLY}.
     *     Use bitwise OR operator to combine these values. Default value is <code>0</code>
     * @return current AcroForm.
     */
    public PdfAcroForm setSignatureFlags(int sigFlags) {
        return put(PdfName.SigFlags, new PdfNumber(sigFlags));
    }

    /**
     * Changes the <code>SigFlags</code> integer property on the AcroForm.
     * This method allows only to add flags, not to remove them. 
     * 
     * <blockquote>
     * SigFlags is a set of flags specifying various document-level
     * characteristics related to signature fields.
     * (ISO 32000-1, section 12.7.2 "Interactive Form Dictionary")
     * </blockquote>
     * 
     * @param sigFlag an integer. Use {@link #SIGNATURE_EXIST} and/or {@link #APPEND_ONLY}.
     *     Use bitwise OR operator to combine these values. Default is <code>0</code>
     * @return current AcroForm.
     */
    public PdfAcroForm setSignatureFlag(int sigFlag) {
        int flags = getSignatureFlags();
        flags = flags | sigFlag;

        return setSignatureFlags(flags);
    }

    /**
     * Gets the <code>SigFlags</code> integer property on the AcroForm.
     * 
     * <blockquote>
     * SigFlags is a set of flags specifying various document-level
     * characteristics related to signature fields
     * (ISO 32000-1, section 12.7.2 "Interactive Form Dictionary")
     * </blockquote>
     * 
     * @return current value for <code>SigFlags</code>.
     */
    public int getSignatureFlags() {
        PdfNumber f = getPdfObject().getAsNumber(PdfName.SigFlags);
        if (f != null)
            return f.getIntValue();
        else
            return 0;
    }

    /**
     * Sets the <code>CO</code> array property on the AcroForm.
     * 
     * <blockquote>
     * <code>CO</code>, Calculation Order, is an array of indirect references to
     * field dictionaries with calculation actions, defining the calculation
     * order in which their values will be recalculated when the value of any
     * field changes
     * (ISO 32000-1, section 12.7.2 "Interactive Form Dictionary")
     * </blockquote>
     * 
     * @param calculationOrder an array of indirect references
     * @return current AcroForm
     */
    public PdfAcroForm setCalculationOrder(PdfArray calculationOrder) {
        return put(PdfName.CO, calculationOrder);
    }

    /**
     * Gets the <code>CO</code> array property on the AcroForm.
     * 
     * <blockquote>
     * <code>CO</code>, Calculation Order, is an array of indirect references to
     * field dictionaries with calculation actions, defining the calculation
     * order in which their values will be recalculated when the value of any
     * field changes
     * (ISO 32000-1, section 12.7.2 "Interactive Form Dictionary")
     * </blockquote>
     * 
     * @return an array of indirect references
     */
    public PdfArray getCalculationOrder() {
        return getPdfObject().getAsArray(PdfName.CO);
    }

    /**
     * Sets the <code>DR</code> dictionary property on the AcroForm.
     * 
     * <blockquote>
     * <code>DR</code> is a resource dictionary containing default resources
     * (such as fonts, patterns, or colour spaces) that shall be used by form
     * field appearance streams. At a minimum, this dictionary shall contain a
     * Font entry specifying the resource name and font dictionary of the
     * default font for displaying text.
     * (ISO 32000-1, section 12.7.2 "Interactive Form Dictionary")
     * </blockquote>
     * 
     * @param defaultResources a resource dictionary
     * @return current AcroForm
     */
    public PdfAcroForm setDefaultResources(PdfDictionary defaultResources) {
        return put(PdfName.DR, defaultResources);
    }

    /**
     * Gets the <code>DR</code> dictionary property on the AcroForm.
     * 
     * <blockquote>
     * <code>DR</code> is a resource dictionary containing default resources
     * (such as fonts, patterns, or colour spaces) that shall be used by form
     * field appearance streams. At a minimum, this dictionary shall contain a
     * Font entry specifying the resource name and font dictionary of the
     * default font for displaying text.
     * (ISO 32000-1, section 12.7.2 "Interactive Form Dictionary")
     * </blockquote>
     * 
     * @return a resource dictionary
     */
    public PdfDictionary getDefaultResources() {
        return getPdfObject().getAsDictionary(PdfName.DR);
    }

    /**
     * Sets the <code>DA</code> String property on the AcroForm.
     * 
     * This method sets a default (fallback value) for the <code>DA</code>
     * attribute of variable text {@link PdfFormField form field}s.
     * 
     * @param appearance a String containing a sequence of valid PDF syntax
     * @return current AcroForm
     * @see PdfFormField#setDefaultAppearance(java.lang.String) 
     */
    public PdfAcroForm setDefaultAppearance(String appearance) {
        return put(PdfName.DA, new PdfString(appearance));
    }

    /**
     * Gets the <code>DA</code> String property on the AcroForm.
     * 
     * This method returns the default (fallback value) for the <code>DA</code>
     * attribute of variable text {@link PdfFormField form field}s.
     * 
     * @return the form-wide default appearance, as a <code>String</code>
     */
    public PdfString getDefaultAppearance() {
        return getPdfObject().getAsString(PdfName.DA);
    }

    /**
     * Sets the <code>Q</code> integer property on the AcroForm.
     * 
     * This method sets a default (fallback value) for the <code>Q</code>
     * attribute of variable text {@link PdfFormField form field}s.
     * 
     * @param justification an integer representing a justification value
     * @return current AcroForm 
     * @see PdfFormField#setJustification(int) 
     */
    public PdfAcroForm setDefaultJustification(int justification) {
        return put(PdfName.Q, new PdfNumber(justification));
    }

    /**
     * Gets the <code>Q</code> integer property on the AcroForm.
     * 
     * This method gets the default (fallback value) for the <code>Q</code>
     * attribute of variable text {@link PdfFormField form field}s.
     * 
     * @return an integer representing a justification value
     * @see PdfFormField#getJustification() 
     */
    public PdfNumber getDefaultJustification() {
        return getPdfObject().getAsNumber(PdfName.Q);
    }

    /**
     * Sets the <code>XFA</code> property on the AcroForm.
     * 
     * <code>XFA</code> can either be a {@link PdfStream} or a {@link PdfArray}.
     * Its contents must be valid XFA.
     * 
     * @param xfaResource a stream containing the XDP
     * @return current AcroForm
     */
    public PdfAcroForm setXFAResource(PdfStream xfaResource) {
        return put(PdfName.XFA, xfaResource);
    }

    /**
     * Sets the <code>XFA</code> property on the AcroForm.
     * 
     * <code>XFA</code> can either be a {@link PdfStream} or a {@link PdfArray}.
     * Its contents must be valid XFA.
     * 
     * @param xfaResource an array of text string and stream pairs representing
     *     the individual packets comprising the XML Data Package. (ISO 32000-1,
     *     section 12.7.2 "Interactive Form Dictionary")
     * @return current AcroForm
     */
    public PdfAcroForm setXFAResource(PdfArray xfaResource) {
        return put(PdfName.XFA, xfaResource);
    }

    /**
     * Gets the <code>XFA</code> property on the AcroForm.
     * 
     * @return an object representing the entire XDP. It can either be a
     * {@link PdfStream} or a {@link PdfArray}.
     */
    public PdfObject getXFAResource() {
        return getPdfObject().get(PdfName.XFA);
    }

    /**
     * Gets a {@link PdfFormField form field} by its name.
     * 
     * @param fieldName the name of the {@link PdfFormField form field} to retrieve
     * @return the {@link PdfFormField form field}, or <code>null</code> if it
     * isn't present
     */
    public PdfFormField getField(String fieldName) {
        return fields.get(fieldName);
    }

    /**
     * Gets the attribute generateAppearance, which tells {@link #flatFields()}
     * to generate an appearance Stream for all {@link PdfFormField form field}s
     * that don't have one.
     *
     * @return bolean value indicating if the appearances need to be generated
     */
    public boolean isGenerateAppearance() {
        return generateAppearance;
    }

    /**
     * Sets the attribute generateAppearance, which tells {@link #flatFields()}
     * to generate an appearance Stream for all {@link PdfFormField form field}s
     * that don't have one.
     * 
     * Not generating appearances will speed up form flattening but the results
     * can be unexpected in Acrobat. Don't use it unless your environment is
     * well controlled. The default is <CODE>true</CODE>.
     * 
     * If generateAppearance is set to <code>true</code>, then 
     * <code>NeedAppearances</code> is set to <code>false</code>. This does not
     * apply vice versa.
     * 
     * @param generateAppearance a boolean
     */
    public void setGenerateAppearance(boolean generateAppearance) {
        if (generateAppearance) {
            getPdfObject().remove(PdfName.NeedAppearances);
        }
        this.generateAppearance = generateAppearance;
    }

    /**
     * Flattens interactive {@link PdfFormField form field}s in the document. If
     * no fields have been explicitly included via {#link #partialFormFlattening},
     * then all fields are flattened. Otherwise only the included fields are
     * flattened.
     */
    public void flatFields() {
        if (document.isAppendMode()) {
            throw new PdfException(PdfException.FieldFlatteningIsNotSupportedInAppendMode);
        }
        Set<PdfFormField> fields;
        if (fieldsForFlattening.isEmpty()) {
            fields = new LinkedHashSet<>(getFormFields().values());
        } else {
            fields = new LinkedHashSet<>();
            for (PdfFormField field : fieldsForFlattening) {
                fields.addAll(prepareFieldsForFlattening(field));
            }
        }

        PdfPage page;
        for (PdfFormField field : fields) {
            page = getFieldPage(field.getPdfObject());
            if (page == null) {
                continue;
            }

            PdfDictionary appDic = field.getPdfObject().getAsDictionary(PdfName.AP);
            PdfObject asNormal = null;
            if (appDic != null) {
                asNormal = appDic.getAsStream(PdfName.N);
                if (asNormal == null) {
                    asNormal = appDic.getAsDictionary(PdfName.N);
                }
            }
            if (generateAppearance) {
                if (appDic == null || asNormal == null) {
                    field.regenerateField();
                    appDic = field.getPdfObject().getAsDictionary(PdfName.AP);
                }
            }
            if (appDic != null) {
                PdfObject normal = appDic.get(PdfName.N);
                PdfFormXObject xObject = null;
                if (normal.isStream()) {
                    xObject = new PdfFormXObject((PdfStream) normal);
                } else if (normal.isDictionary()) {
                    PdfName as = field.getPdfObject().getAsName(PdfName.AS);
                    if (((PdfDictionary)normal).getAsStream(as) != null) {
                        xObject = new PdfFormXObject(((PdfDictionary)normal).getAsStream(as)).makeIndirect(document);
                    }
                }

                if (xObject != null) {
                    Rectangle box = field.getPdfObject().getAsRectangle(PdfName.Rect);
                    if (page.isFlushed()) {
                        throw new PdfException(PdfException.PageWasAlreadyFlushedUseAddFieldAppearanceToPageMethodInstead);
                    }
                    PdfCanvas canvas = new PdfCanvas(page);
                    canvas.addXObject(xObject, box.getX(), box.getY());
                }
            }

            PdfArray fFields = getFields();
            fFields.remove(field.getPdfObject().getIndirectReference());
            PdfArray annots = page.getPdfObject().getAsArray(PdfName.Annots);
            annots.remove(field.getPdfObject().getIndirectReference());
            if (annots.isEmpty()) {
                page.getPdfObject().remove(PdfName.Annots);
            }
            PdfDictionary parent = field.getPdfObject().getAsDictionary(PdfName.Parent);
            if (parent != null) {
                PdfArray kids = parent.getAsArray(PdfName.Kids);
                kids.remove(field.getPdfObject().getIndirectReference());
                if (kids == null || kids.isEmpty()) {
                    fFields.remove(parent.getIndirectReference());
                }
            }
        }

        getPdfObject().remove(PdfName.NeedAppearances);
        if (fieldsForFlattening.isEmpty()) {
            getFields().clear();
        }
        if (getFields().isEmpty()) {
            document.getCatalog().getPdfObject().remove(PdfName.AcroForm);
        }
    }

    /**
     * Tries to remove the {@link PdfFormField form field} with the specified
     * name from the document.
     * 
     * @param fieldName the name of the {@link PdfFormField form field} to remove
     * @return a boolean representing whether or not the removal succeeded.
     */
    public boolean removeField(String fieldName) {
        PdfFormField field = getField(fieldName);
        if (field == null) {
            return false;
        }

        PdfPage page = getFieldPage(field.getPdfObject());

        if (page != null) {
            PdfArray annots = page.getPdfObject().getAsArray(PdfName.Annots);
            if (annots != null) {
                annots.remove(field.getPdfObject().getIndirectReference());
            }
        }

        PdfDictionary parent = field.getParent();
        if (parent != null) {
            PdfArray kids = parent.getAsArray(PdfName.Kids);
            if (!kids.remove(field.getPdfObject().getIndirectReference())) {
                kids.remove(field.getPdfObject());
            }
            fields.remove(fieldName);
            return true;
        }

        if (getFields().remove(field.getPdfObject().getIndirectReference()) || getFields().remove(field.getPdfObject())) {
            fields.remove(fieldName);
            return true;
        }
        return false;
    }

    /**
     * Adds a {@link PdfFormField form field}, identified by name, to the list of fields to be flattened.
     * Does not perform a flattening operation in itself.
     * 
     * @param fieldName the name of the {@link PdfFormField form field} to be flattened
     */
    public void partialFormFlattening(String fieldName) {
        PdfFormField field = getFormFields().get(fieldName);
        if (field != null) {
            fieldsForFlattening.add(field);
        }
    }

    /**
     * Changes the identifier of a {@link PdfFormField form field}.
     * 
     * @param oldName the current name of the field
     * @param newName the new name of the field. Must not be used currently.
     */
    public void renameField(String oldName, String newName) {
        Map<String, PdfFormField> fields = getFormFields();
        if (fields.containsKey(newName)) {
            return;
        }
        PdfFormField field = fields.get(oldName);
        if (field != null) {
            field.setFieldName(newName);
            fields.remove(oldName);
            fields.put(newName, field);
        }
    }

    /**
     * Creates an in-memory copy of a {@link PdfFormField}. This new field is
     * not added to the document.
     * 
     * @param name the name of the {@link PdfFormField form field} to be copied
     * @return a clone of the original {@link PdfFormField}
     */
    public PdfFormField copyField(String name) {
        PdfFormField oldField = getField(name);
        if (oldField != null) {
            PdfFormField field = new PdfFormField((PdfDictionary) oldField.getPdfObject().clone());
            field.makeIndirect(document);
            return field;
        }

        return null;
    }

    /**
     * Replaces the {@link PdfFormField} of a certain name with another
     * {@link PdfFormField}.
     * 
     * @param name the name of the {@link PdfFormField form field} to be replaced
     * @param field the new {@link PdfFormField}
     */
    public void replaceField(String name, PdfFormField field) {
        removeField(name);
        addField(field);
    }

    /**
     * Gets all AcroForm fields in the document.
     * 
     * @return a {@link PdfArray} of field dictionaries
     */
    protected PdfArray getFields() {
        return getPdfObject().getAsArray(PdfName.Fields);
    }

    private Map<String, PdfFormField> iterateFields(PdfArray array) {
        Map<String, PdfFormField> fields = new LinkedHashMap<>();

        int index = 1;
        for (PdfObject field : array) {
            PdfFormField formField = PdfFormField.makeFormField(field, document);
            PdfString fieldName = formField.getFieldName();
            String name;
            if (fieldName == null) {
                name = formField.getParent().getAsString(PdfName.T).toUnicodeString() + "." + index;
                index++;
            } else {
                name = fieldName.toUnicodeString();
            }
            fields.put(name, formField);
            if (formField.getKids() != null) {
                fields.putAll(iterateFields(formField.getKids()));
            }
        }

        return fields;
    }

    private PdfDictionary processKids(PdfArray kids, PdfDictionary parent, PdfPage page){
        if (kids.size() == 1){
            PdfDictionary dict = (PdfDictionary) kids.get(0);
            PdfName type = dict.getAsName(PdfName.Subtype);
            if (type != null && type.equals(PdfName.Widget)){
                parent.remove(PdfName.Kids);
                dict.remove(PdfName.Parent);
                parent.mergeDifferent(dict);
                PdfAnnotation annot = PdfAnnotation.makeAnnotation(parent);
                PdfDictionary pageDic = annot.getPdfObject().getAsDictionary(PdfName.P);
                if (pageDic != null) {
                    if (pageDic.isFlushed()) {
                        throw new PdfException(PdfException.PageWasAlreadyFlushedUseAddFieldAppearanceToPageMethodInstead);
                    }
                    PdfArray array = pageDic.getAsArray(PdfName.Annots);
                    if (array == null) {
                        array = new PdfArray();
                        pageDic.put(PdfName.Annots, array);
                    }
                    array.add(parent);
                } else {
                    page.addAnnotation(-1, annot, false);
                }
            } else {
                PdfArray otherKids = (dict).getAsArray(PdfName.Kids);
                if (otherKids != null) {
                    processKids(otherKids, dict, page);
                }
            }
        } else {
            for (PdfObject kid : kids){
                if (kid.isIndirectReference()) {
                    kid = ((PdfIndirectReference)kid).getRefersTo();
                }
                PdfArray otherKids = ((PdfDictionary)kid).getAsArray(PdfName.Kids);
                if (otherKids != null) {
                    processKids(otherKids, (PdfDictionary) kid, page);
                }
            }
        }

        return parent;
    }

    private List<PdfDictionary> getResources(PdfDictionary field) {
        List<PdfDictionary> resources = new ArrayList<>();

        PdfDictionary ap = field.getAsDictionary(PdfName.AP);
        if (ap != null) {
            PdfObject normal = ap.get(PdfName.N);
            if (normal != null) {
                if (normal.isDictionary()) {
                    for (PdfName key : ((PdfDictionary)normal).keySet()) {
                        PdfStream appearance = ((PdfDictionary)normal).getAsStream(key);
                        PdfDictionary resDict = appearance.getAsDictionary(PdfName.Resources);
                        if (resDict != null) {
                            resources.add(resDict);
                            break;
                        }
                    }
                } else if (normal.isStream()) {
                    PdfDictionary resDict = ((PdfStream)normal).getAsDictionary(PdfName.Resources);
                    if (resDict != null) {
                        resources.add(resDict);
                    }
                }
            }
        }

        PdfArray kids = field.getAsArray(PdfName.Kids);
        if (kids != null) {
            for (PdfObject kid : kids) {
                resources.addAll(getResources((PdfDictionary) kid));
            }
        }

        return resources;
    }

    /**
     * Merges two dictionaries. When both dictionaries contain the same key,
     * the value from the first dictionary is kept.
     * 
     * @param result the {@link PdfDictionary} which may get extra entries from source
     * @param source the {@link PdfDictionary} whose entries may be merged into result
     * @param field
     */
    // TODO: determine whether we need the parameter called field
    public void mergeResources(PdfDictionary result, PdfDictionary source, PdfFormField field) {
        for (PdfName name : resourceNames) {
            PdfDictionary dic = source.getAsDictionary(name);
            PdfDictionary res = result.getAsDictionary(name);
            if (res == null) {
                res = new PdfDictionary();
            }
            if (dic != null) {
                res.mergeDifferent(dic);
                result.put(name, res);
            }
        }
    }

    /**
     * Determines whether the AcroForm contains XFA data.
     * 
     * @return a boolean 
     */
    public boolean hasXfaForm(){
        return xfaForm != null && xfaForm.isXfaPresent();
    }

    /**
     * Gets the {@link XfaForm} atribute.
     * 
     * @return the XFA form object
     */
    public XfaForm getXfaForm(){
        return xfaForm;
    }

    /**
     * Removes the XFA stream from the document.
     */
    public void removeXfaForm() {
        if(hasXfaForm()) {
            PdfDictionary root = document.getCatalog().getPdfObject();
            PdfDictionary acroform = root.getAsDictionary(PdfName.AcroForm);
            acroform.remove(PdfName.XFA);
            xfaForm = new XfaForm();
        }
    }

    /**
     * Changes a field value in the XFA form.
     * 
     * @param name the name of the field to be changed
     * @param value the new value
     */
    public void setXfaFieldValue(String name, String value) {
        if (hasXfaForm()) {
            name = xfaForm.findFieldName(name, this);
            if (name != null) {
                String shortName = Xml2Som.getShortName(name);
                Node xn = xfaForm.findDatasetsNode(shortName);
                if (xn == null) {
                    xn = xfaForm.getDatasetsSom().insertNode(xfaForm.getDatasetsNode(), shortName);
                }
                xfaForm.setNodeText(xn, value);
            }
        }
    }

    /**
     * Gets the xfa field value.
     * @param name the fully qualified field name
     * @return the field value
     */
    public String getXfaFieldValue(String name) {
        if (xfaForm.isXfaPresent()) {
            name = xfaForm.findFieldName(name, this);
            if (name != null) {

                name = Xml2Som.getShortName(name);
                return XfaForm.getNodeText(xfaForm.findDatasetsNode(name));
            }
        }
        return null;
    }

    private PdfPage getPage(PdfDictionary pageDic) {
        PdfPage page;
        for (int i = 1; i <= document.getNumberOfPages(); i++) {
            page = document.getPage(i);
            if (page.getPdfObject() == pageDic) {
                return page;
            }
        }
        return null;
    }

    private PdfPage getFieldPage(PdfDictionary annotation) {
        PdfDictionary pageDic = annotation.getAsDictionary(PdfName.P);
        if (pageDic != null) {
            return getPage(pageDic);
        }
        for (int i = 1; i <= document.getNumberOfPages(); i++) {
            PdfPage page = document.getPage(i);
            if (!page.isFlushed()) {
                PdfArray annotations = page.getPdfObject().getAsArray(PdfName.Annots);
                if (annotations != null && annotations.contains(annotation.getIndirectReference())){
                    return page;
                }
            }
        }
        return null;
    }

    private Set<PdfFormField> prepareFieldsForFlattening(PdfFormField field) {
        Set<PdfFormField> preparedFields = new LinkedHashSet<>();
        preparedFields.add(field);
        PdfArray kids = field.getKids();
        if (kids != null) {
            for (PdfObject kid : kids) {
                PdfDictionary fieldDict;
                if (kid.isIndirectReference()) {
                    fieldDict = (PdfDictionary) ((PdfIndirectReference)kid).getRefersTo();
                } else {
                    fieldDict = (PdfDictionary) kid;
                }
                PdfFormField kidField = new PdfFormField(fieldDict);
                preparedFields.add(kidField);
                if (kidField.getKids() != null) {
                    preparedFields.addAll(prepareFieldsForFlattening(kidField));
                }
            }
        }
        return preparedFields;
    }
}
