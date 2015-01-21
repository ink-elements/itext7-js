package com.itextpdf.core.pdf;

import com.itextpdf.basics.PdfException;
import com.itextpdf.basics.io.ByteBuffer;
import com.itextpdf.basics.io.OutputStream;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class PdfName extends PdfPrimitiveObject implements Comparable<PdfName> {

    private static final byte[] space = OutputStream.getIsoBytes("#20");                //  ' '
    private static final byte[] percent = OutputStream.getIsoBytes("#25");              //  '%'
    private static final byte[] leftParenthesis = OutputStream.getIsoBytes("#28");      //  '('
    private static final byte[] rightParenthesis = OutputStream.getIsoBytes("#29");     //  ')'
    private static final byte[] lessThan = OutputStream.getIsoBytes("#3c");             //  '<'
    private static final byte[] greaterThan = OutputStream.getIsoBytes("#3e");          //  '>'
    private static final byte[] leftSquare = OutputStream.getIsoBytes("#5b");           //  '['
    private static final byte[] rightSquare = OutputStream.getIsoBytes("#5d");          //  ']'
    private static final byte[] leftCurlyBracket = OutputStream.getIsoBytes("#7b");     //  '{'
    private static final byte[] rightCurlyBracket = OutputStream.getIsoBytes("#7d");    //  '}'
    private static final byte[] solidus = OutputStream.getIsoBytes("#2f");              //  '/'
    private static final byte[] numberSign = OutputStream.getIsoBytes("#23");           //  '#'

    public static final PdfName A = createDirectName("A");
    public static final PdfName A85 = createDirectName("A85");
    public static final PdfName AbsoluteColorimetric = createDirectName("AbsoluteColorimetric");
    public static final PdfName Action = createDirectName("Action");
    public static final PdfName ActualText = createDirectName("ActualText");
    public static final PdfName Adbe_pkcs7_detached = createDirectName("adbe.pkcs7.detached");
    public static final PdfName Adbe_pkcs7_s4 = createDirectName("adbe.pkcs7.s4");
    public static final PdfName Adbe_pkcs7_s5 = createDirectName("adbe.pkcs7.s5");
    public static final PdfName Adbe_pkcs7_sha1 = createDirectName("adbe.pkcs7.sha1");
    public static final PdfName Adbe_x509_rsa_sha1 = createDirectName("adbe.x509.rsa_sha1");
    public static final PdfName Adobe_PPKLite = createDirectName("Adobe.PPKLite");
    public static final PdfName Adobe_PPKMS = createDirectName("Adobe.PPKMS");
    public static final PdfName Adobe_PubSec = createDirectName("Adobe.PubSec");
    public static final PdfName AESV2 = createDirectName("AESV2");
    public static final PdfName AESV3 = createDirectName("AESV3");
    public static final PdfName AHx = createDirectName("AHx");
    public static final PdfName AIS = createDirectName("AIS");
    public static final PdfName Alt = createDirectName("Alt");
    public static final PdfName Alternate = createDirectName("Alternate");
    public static final PdfName Annot = createDirectName("Annot");
    public static final PdfName Art = createDirectName("Art");
    public static final PdfName Artifact = createDirectName("Artifact");
    public static final PdfName ASCII85Decode = createDirectName("ASCII85Decode");
    public static final PdfName ASCIIHexDecode = createDirectName("ASCIIHexDecode");
    public static final PdfName AuthEvent = createDirectName("AuthEvent");
    public static final PdfName Author = createDirectName("Author");
    public static final PdfName BaseFont = createDirectName("BaseFont");
    public static final PdfName BBox = createDirectName("BBox");
    public static final PdfName BG = createDirectName("BG");
    public static final PdfName BG2 = createDirectName("BG2");
    public static final PdfName BibEntry = createDirectName("BibEntry");
    public static final PdfName BitsPerComponent = createDirectName("BitsPerComponent");
    public static final PdfName BitsPerSample = createDirectName("BitsPerSample");
    public static final PdfName BlackIs1 = createDirectName("BlackIs1");
    public static final PdfName BlackPoint = createDirectName("BlackPoint");
    public static final PdfName BlockQuote = createDirectName("BlockQuote");
    public static final PdfName BM = createDirectName("BM");
    public static final PdfName Bounds = createDirectName("Bounds");
    public static final PdfName C0 = createDirectName("C0");
    public static final PdfName C1 = createDirectName("C1");
    public static final PdfName CA = createDirectName("CA");
    public static final PdfName ca = createDirectName("ca");
    public static final PdfName CalGray = createDirectName("CalGray");
    public static final PdfName CalRGB = createDirectName("CalRGB");
    public static final PdfName Caption = createDirectName("Caption");
    public static final PdfName Catalog = createDirectName("Catalog");
    public static final PdfName CCITTFaxDecode = createDirectName("CCITTFaxDecode");
    public static final PdfName CF = createDirectName("CF");
    public static final PdfName CFM = createDirectName("CFM");
    public static final PdfName Code = createDirectName("Code");
    public static final PdfName Colors = createDirectName("Colors");
    public static final PdfName ColorSpace = createDirectName("ColorSpace");
    public static final PdfName ColorTransform = createDirectName("ColorTransform");
    public static final PdfName Columns = createDirectName("Columns");
    public static final PdfName Contents = createDirectName("Contents");
    public static final PdfName Count = createDirectName("Count");
    public static final PdfName CreationDate = createDirectName("CreationDate");
    public static final PdfName Creator = createDirectName("Creator");
    public static final PdfName CropBox = createDirectName("CropBox");
    public static final PdfName Crypt = createDirectName("Crypt");
    public static final PdfName D = createDirectName("D");
    public static final PdfName DCTDecode = createDirectName("DCTDecode");
    public static final PdfName Decode = createDirectName("Decode");
    public static final PdfName DecodeParms = createDirectName("DecodeParms");
    public static final PdfName DefaultCMYK = createDirectName("DefaultCMYK");
    public static final PdfName DefaultCryptFilter = createDirectName("DefaultCryptFilter");
    public static final PdfName DefaultGray = createDirectName("DefaultGray");
    public static final PdfName DefaultRGB = createDirectName("DefaultRGB");
    public static final PdfName DeviceCMYK = createDirectName("DeviceCMYK");
    public static final PdfName DeviceGray = createDirectName("DeviceGray");
    public static final PdfName DeviceN = createDirectName("DeviceN");
    public static final PdfName DeviceRGB = createDirectName("DeviceRGB");
    public static final PdfName Div = createDirectName("Div");
    public static final PdfName DocOpen = createDirectName("DocOpen");
    public static final PdfName Document = createDirectName("Document");
    public static final PdfName Domain = createDirectName("Domain");
    public static final PdfName DP = createDirectName("DP");
    public static final PdfName E = createDirectName("E");
    public static final PdfName EFF = createDirectName("EFF");
    public static final PdfName EFOpen = createDirectName("EFOpen");
    public static final PdfName Encode = createDirectName("Encode");
    public static final PdfName EncodedByteAlign = createDirectName("EncodedByteAlign");
    public static final PdfName Encoding = createDirectName("Encoding");
    public static final PdfName Encrypt = createDirectName("Encrypt");
    public static final PdfName EncryptMetadata = createDirectName("EncryptMetadata");
    public static final PdfName EndOfBlock = createDirectName("EndOfBlock");
    public static final PdfName EndOfLine = createDirectName("EndOfLine");
    public static final PdfName Extends = createDirectName("Extends");
    public static final PdfName ExtGState = createDirectName("ExtGState");
    public static final PdfName F = createDirectName("F");
    public static final PdfName Figure = createDirectName("Figure");
    public static final PdfName Filter = createDirectName("Filter");
    public static final PdfName First = createDirectName("First");
    public static final PdfName Fit = createDirectName("Fit");
    public static final PdfName FitB = createDirectName("FitB");
    public static final PdfName FitBH = createDirectName("FitBH");
    public static final PdfName FitBV = createDirectName("FitBV");
    public static final PdfName FitH = createDirectName("FitH");
    public static final PdfName FitR = createDirectName("FitR");
    public static final PdfName FitV = createDirectName("FitV");
    public static final PdfName FL = createDirectName("FL");
    public static final PdfName FlateDecode = createDirectName("FlateDecode");
    public static final PdfName Font = createDirectName("Font");
    public static final PdfName Form = createDirectName("Form");
    public static final PdfName Formula = createDirectName("Formula");
    public static final PdfName Functions = createDirectName("Functions");
    public static final PdfName FunctionType = createDirectName("FunctionType");
    public static final PdfName Gamma = createDirectName("Gamma");
    public static final PdfName GoTo = createDirectName("GoTo");
    public static final PdfName GoToR = createDirectName("GoToR");
    public static final PdfName H = createDirectName("H");
    public static final PdfName H1 = createDirectName("H1");
    public static final PdfName H2 = createDirectName("H2");
    public static final PdfName H3 = createDirectName("H3");
    public static final PdfName H4 = createDirectName("H4");
    public static final PdfName H5 = createDirectName("H5");
    public static final PdfName H6 = createDirectName("H6");
    public static final PdfName Height = createDirectName("Height");
    public static final PdfName HT = createDirectName("HT");
    public static final PdfName ICCBased = createDirectName("ICCBased");
    public static final PdfName ID = createDirectName("ID");
    public static final PdfName Identity = createDirectName("Identity");
    public static final PdfName Image = createDirectName("Image");
    public static final PdfName ImageMask = createDirectName("ImageMask");
    public static final PdfName Index = createDirectName("Index");
    public static final PdfName Indexed = createDirectName("Indexed");
    public static final PdfName Info = createDirectName("Info");
    public static final PdfName Interpolate = createDirectName("Interpolate");
    public static final PdfName IsMap = createDirectName("IsMap");
    public static final PdfName JBIG2Decode = createDirectName("JBIG2Decode");
    public static final PdfName JPXDecode = createDirectName("JPXDecode");
    public static final PdfName K = createDirectName("K");
    public static final PdfName Keywords = createDirectName("Keywords");
    public static final PdfName Kids = createDirectName("Kids");
    public static final PdfName L = createDirectName("L");
    public static final PdfName Lab = createDirectName("Lab");
    public static final PdfName Lang = createDirectName("Lang");
    public static final PdfName Lbl = createDirectName("Lbl");
    public static final PdfName LBody = createDirectName("LBody");
    public static final PdfName LC = createDirectName("LC");
    public static final PdfName Length = createDirectName("Length");
    public static final PdfName LI = createDirectName("LI");
    public static final PdfName Link = createDirectName("Link");
    public static final PdfName LJ = createDirectName("LJ");
    public static final PdfName LW = createDirectName("LW");
    public static final PdfName LZWDecode = createDirectName("LZWDecode");
    public static final PdfName Marked = createDirectName("Marked");
    public static final PdfName MarkInfo = createDirectName("MarkInfo");
    public static final PdfName Mask = createDirectName("Mask");
    public static final PdfName Matrix = createDirectName("Matrix");
    public static final PdfName MCID = createDirectName("MCID");
    public static final PdfName MCR = createDirectName("MCR");
    public static final PdfName MediaBox = createDirectName("MediaBox");
    public static final PdfName Metadata = createDirectName("Metadata");
    public static final PdfName ML = createDirectName("ML");
    public static final PdfName ModDate = createDirectName("ModDate");
    public static final PdfName N = createDirectName("N");
    public static final PdfName Name = createDirectName("Name");
    public static final PdfName NewWindow = createDirectName("NewWindow");
    public static final PdfName Next = createDirectName("Next");
    public static final PdfName NonStruct = createDirectName("NonStruct");
    public static final PdfName Note = createDirectName("Note");
    public static final PdfName Nums = createDirectName("Nums");
    public static final PdfName O = createDirectName("O");
    public static final PdfName ObjStm = createDirectName("ObjStm");
    public static final PdfName OE = createDirectName("OE");
    public static final PdfName OP = createDirectName("OP");
    public static final PdfName op = createDirectName("op");
    public static final PdfName OPM = createDirectName("OPM");
    public static final PdfName Order = createDirectName("Order");
    public static final PdfName P = createDirectName("P");
    public static final PdfName Page = createDirectName("Page");
    public static final PdfName Pages = createDirectName("Pages");
    public static final PdfName Parent = createDirectName("Parent");
    public static final PdfName ParentTree = createDirectName("ParentTree");
    public static final PdfName Part = createDirectName("Part");
    public static final PdfName Perceptual = createDirectName("Perceptual");
    public static final PdfName Perms = createDirectName("Perms");
    public static final PdfName Pg = createDirectName("Pg");
    public static final PdfName Predictor = createDirectName("Predictor");
    public static final PdfName Prev = createDirectName("Prev");
    public static final PdfName Private = createDirectName("Private");
    public static final PdfName Producer = createDirectName("Producer");
    public static final PdfName Properties = createDirectName("Properties");
    public static final PdfName Quote = createDirectName("Quote");
    public static final PdfName R = createDirectName("R");
    public static final PdfName Range = createDirectName("Range");
    public static final PdfName RB = createDirectName("RB");
    public static final PdfName Recipients = createDirectName("Recipients");
    public static final PdfName Reference = createDirectName("Reference");
    public static final PdfName RelativeColorimetric = createDirectName("RelativeColorimetric");
    public static final PdfName Resources = createDirectName("Resources");
    public static final PdfName RI = createDirectName("RI");
    public static final PdfName RoleMap = createDirectName("RoleMap");
    public static final PdfName Root = createDirectName("Root");
    public static final PdfName Rows = createDirectName("Rows");
    public static final PdfName RP = createDirectName("RP");
    public static final PdfName RT = createDirectName("RT");
    public static final PdfName Ruby = createDirectName("Ruby");
    public static final PdfName RunLengthDecode = createDirectName("RunLengthDecode");
    public static final PdfName S = createDirectName("S");
    public static final PdfName SA = createDirectName("SA");
    public static final PdfName Saturation = createDirectName("Saturation");
    public static final PdfName Sect = createDirectName("Sect");
    public static final PdfName Separation = createDirectName("Separation");
    public static final PdfName Size = createDirectName("Size");
    public static final PdfName SM = createDirectName("SM");
    public static final PdfName SMask = createDirectName("SMask");
    public static final PdfName Span = createDirectName("Span");
    public static final PdfName Standard = createDirectName("Standard");
    public static final PdfName StdCF = createDirectName("StdCF");
    public static final PdfName StmF = createDirectName("StmF");
    public static final PdfName StrF = createDirectName("StrF");
    public static final PdfName StructElem = createDirectName("StructElem");
    public static final PdfName StructParents = createDirectName("StructParents");
    public static final PdfName StructTreeRoot = createDirectName("StructTreeRoot");
    public static final PdfName SubFilter = createDirectName("SubFilter");
    public static final PdfName Subject = createDirectName("Subject");
    public static final PdfName Subtype = createDirectName("Subtype");
    public static final PdfName Table = createDirectName("Table");
    public static final PdfName TBody = createDirectName("TBody");
    public static final PdfName TD = createDirectName("TD");
    public static final PdfName TFoot = createDirectName("TFoot");
    public static final PdfName TH = createDirectName("TH");
    public static final PdfName THead = createDirectName("THead");
    public static final PdfName Title = createDirectName("Title");
    public static final PdfName TK = createDirectName("TK");
    public static final PdfName TOC = createDirectName("TOC");
    public static final PdfName TOCI = createDirectName("TOCI");
    public static final PdfName TR = createDirectName("TR");
    public static final PdfName TR2 = createDirectName("TR2");
    public static final PdfName Type = createDirectName("Type");
    public static final PdfName Type1 = createDirectName("Type1");
    public static final PdfName U = createDirectName("U");
    public static final PdfName UCR = createDirectName("UCR");
    public static final PdfName UCR2 = createDirectName("UCR2");
    public static final PdfName UE = createDirectName("UE");
    public static final PdfName URI = createDirectName("URI");
    public static final PdfName V = createDirectName("V");
    public static final PdfName V2 = createDirectName("V2");
    public static final PdfName W = createDirectName("W");
    public static final PdfName Warichu = createDirectName("Warichu");
    public static final PdfName WhitePoint = createDirectName("WhitePoint");
    public static final PdfName Width = createDirectName("Width");
    public static final PdfName WinAnsiEncoding = createDirectName("WinAnsiEncoding");
    public static final PdfName WP = createDirectName("WP");
    public static final PdfName WT = createDirectName("WT");
    public static final PdfName XML = createDirectName("XML");
    public static final PdfName XObject = createDirectName("XObject");
    public static final PdfName XRef = createDirectName("XRef");
    public static final PdfName XRefStm = createDirectName("XRefStm");
    public static final PdfName XYZ = createDirectName("XYZ");

    protected String value = null;

    /**
     * map strings to all known static names
     */
    public static Map<String, PdfName> staticNames;

    /**
     * Use reflection to cache all the static public final names so
     * future <code>PdfName</code> additions don't have to be "added twice".
     * A bit less efficient (around 50ms spent here on a 2.2ghz machine),
     *  but Much Less error prone.
     */

    static {
        Field fields[] = PdfName.class.getDeclaredFields();
        staticNames = new HashMap<String, PdfName>(fields.length);
        final int flags = Modifier.STATIC | Modifier.PUBLIC | Modifier.FINAL;
        try {
            for (int fldIdx = 0; fldIdx < fields.length; ++fldIdx) {
                Field curFld = fields[fldIdx];
                if ((curFld.getModifiers() & flags) == flags &&
                        curFld.getType().equals(PdfName.class)) {
                    PdfName name = (PdfName) curFld.get(null);
                    staticNames.put(name.getValue(), name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PdfName createDirectName(String name) {
        return new PdfName(name, true);
    }

    public PdfName(String value) {
        super();
        this.value = value;
    }

    private PdfName(String value, boolean directOnly) {
        super(directOnly);
        this.value = value;
    }

    public PdfName(byte[] content) {
        super(content);
    }

    private PdfName() {
        super();
    }

    @Override
    public byte getType() {
        return PdfObject.Name;
    }

    public String getValue() {
        if (value == null)
            generateValue();
        return value;
    }

    @Override
    public int compareTo(PdfName o) {
        if (value != null && o.value != null) {
            return value.compareTo(o.value);
        } else if (content != null && o.content != null) {
            return compareContent(o);
        } else
            return getValue().compareTo(o.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfName pdfName = (PdfName) o;
        return this.compareTo(pdfName) == 0;
    }

    protected void generateValue() {
        StringBuilder buf = new StringBuilder();
        try {
            for (int k = 0; k < content.length; ++k) {
                char c = (char) content[k];
                if (c == '#') {
                    byte c1 = content[k + 1];
                    byte c2 = content[k + 2];
                    c = (char) ((ByteBuffer.getHex(c1) << 4) + ByteBuffer.getHex(c2));
                    k += 2;
                }
                buf.append(c);
            }
        } catch (IndexOutOfBoundsException e) {
            // empty on purpose
        }
        value = buf.toString();
    }

    @Override
    protected void generateContent() {
        int length = value.length();
        ByteBuffer buf = new ByteBuffer(length + 20);
        char c;
        char chars[] = value.toCharArray();
        for (int k = 0; k < length; k++) {
            c = (char) (chars[k] & 0xff);
            // Escape special characters
            switch (c) {
                case ' ':
                    buf.append(space);
                    break;
                case '%':
                    buf.append(percent);
                    break;
                case '(':
                    buf.append(leftParenthesis);
                    break;
                case ')':
                    buf.append(rightParenthesis);
                    break;
                case '<':
                    buf.append(lessThan);
                    break;
                case '>':
                    buf.append(greaterThan);
                    break;
                case '[':
                    buf.append(leftSquare);
                    break;
                case ']':
                    buf.append(rightSquare);
                    break;
                case '{':
                    buf.append(leftCurlyBracket);
                    break;
                case '}':
                    buf.append(rightCurlyBracket);
                    break;
                case '/':
                    buf.append(solidus);
                    break;
                case '#':
                    buf.append(numberSign);
                    break;
                default:
                    if (c >= 32 && c <= 126)
                        buf.append(c);
                    else {
                        buf.append('#');
                        if (c < 16)
                            buf.append('0');
                        buf.append(Integer.toString(c, 16));
                    }
                    break;
            }
        }
        content = buf.toByteArray();
    }

    @Override
    public String toString() {
        if (content != null)
            return "/" + new String(content);
        else
            return "/" + getValue();
    }

    @Override
    protected PdfName newInstance() {
        return new PdfName();
    }

    @Override
    protected void copyContent(PdfObject from, PdfDocument document) throws PdfException {
        super.copyContent(from, document);
        PdfName name = (PdfName) from;
        value = name.value;
    }
}
