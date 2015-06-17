package com.itextpdf.core.pdf.formfield;


import com.itextpdf.core.pdf.*;

import java.util.ArrayList;

public class PdfAcroForm extends PdfObjectWrapper<PdfDictionary> {

    /**
     * Signature flags
     */
    static public final int SIGNATURE_EXIST = 1;
    static public final int APPEND_ONLY = 2;


    public PdfAcroForm(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    public PdfAcroForm(PdfDictionary pdfObject, PdfDocument pdfDocument) {
        super(pdfObject, pdfDocument);
    }

    public PdfAcroForm(PdfDocument document, PdfArray fields){
        this(new PdfDictionary(), document);
        put(PdfName.Fields, fields);
    }

    public ArrayList<PdfFormField> getFormFields(){
        ArrayList<PdfFormField> fields = new ArrayList<>();

        PdfArray array = getPdfObject().getAsArray(PdfName.Fields);

        for(PdfObject field : array){
            PdfFormField formField = PdfFormField.makeFormField(field, getDocument());
            fields.add(formField);
        }

        return fields;
    }

    public PdfAcroForm setNeedAppearances(boolean needAppearances){
        return put(PdfName.NeedAppearances, new PdfBoolean(needAppearances));
    }

    public PdfBoolean getNeedAppearances(){
        return getPdfObject().getAsBoolean(PdfName.NeedAppearances);
    }

    public PdfAcroForm setSignatureFlags(int sigFlags){
        return put(PdfName.SigFlags, new PdfNumber(sigFlags));
    }

    public PdfAcroForm setSignatureFlag(int sigFlag){
        int flags = getSignatureFlags();
        flags = flags | sigFlag;

        return setSignatureFlags(flags);
    }

    public int getSignatureFlags(){
        PdfNumber f = getPdfObject().getAsNumber(PdfName.SigFlags);
        if (f != null)
            return f.getIntValue();
        else
            return 0;
    }

    public PdfAcroForm setCalculationOrder(PdfArray calculationOrder){
        return put(PdfName.CO, calculationOrder);
    }

    public PdfArray getCalculationOrder(){
        return getPdfObject().getAsArray(PdfName.CO);
    }

    public PdfAcroForm setDefaultResources(PdfDictionary defaultResources){
        return put(PdfName.DR, defaultResources);
    }

    public PdfDictionary getDefaultResources(){
        return getPdfObject().getAsDictionary(PdfName.DR);
    }

    public PdfAcroForm setDefaultAppearance(String appearance){
        return put(PdfName.DA, new PdfString(appearance));
    }

    public PdfString getDefaultAppearance(){
        return getPdfObject().getAsString(PdfName.DA);
    }

    public PdfAcroForm setDefaultQuadding(int quadding){
        return put(PdfName.Q, new PdfNumber(quadding));
    }

    public PdfNumber getDefaultQuadding(){
        return getPdfObject().getAsNumber(PdfName.Q);
    }

    public PdfAcroForm setXFAResource(PdfStream xfaResource){
        return put(PdfName.XFA, xfaResource);
    }

    public PdfAcroForm setXFAResource(PdfArray xfaResource){
        return put(PdfName.XFA, xfaResource);
    }

    public PdfObject getXFAResource(){
        return getPdfObject().get(PdfName.XFA);
    }

    public PdfArray getFields(){
        return getPdfObject().getAsArray(PdfName.Fields);
    }
}
