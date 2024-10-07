package io.meraklis.icare.pdfs;

import org.apache.fontbox.afm.CharMetric;
import org.apache.fontbox.afm.FontMetrics;
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDAppearanceContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.encoding.GlyphList;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.*;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class DocumentHelper {

    public static void processField(PDField field, String sLevel, String sParent) {
        String partialName = field.getPartialName();

        if (field instanceof PDNonTerminalField) {
            if (!sParent.equals(field.getPartialName()) && partialName != null) {
                sParent = sParent + "." + partialName;
            }
            System.out.println(sLevel + sParent);

            for (PDField child : ((PDNonTerminalField) field).getChildren()) {
                processField(child, "|  " + sLevel, sParent);
            }
        } else {
            String fieldValue = field.getValueAsString();
            StringBuilder outputString = new StringBuilder(sLevel);
            outputString.append(sParent);
            if (partialName != null) {
                outputString.append(".").append(partialName);
            }
            outputString.append(" = ").append(fieldValue);
            outputString.append(",  type=").append(field.getClass().getName());
            System.out.println(outputString);
        }
    }



    public static byte[] docToBytes(PDDocument document) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            document.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void applyCheckboxAppearance(PDDocument pdfDocument, PDCheckBox checkbox) throws IOException {
        PDAnnotationWidget widget = checkbox.getWidgets().get(0);
        PDAppearanceCharacteristicsDictionary appearanceCharacteristics = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
        appearanceCharacteristics.setBorderColour(new PDColor(new float[]{1, 0, 0}, PDDeviceRGB.INSTANCE));
        appearanceCharacteristics.setBackground(new PDColor(new float[]{1, 1, 0}, PDDeviceRGB.INSTANCE));
        // 8 = cross; 4 = checkmark; H = star; u = diamond; n = square, l = dot
        appearanceCharacteristics.setNormalCaption("8");
        widget.setAppearanceCharacteristics(appearanceCharacteristics);

        PDBorderStyleDictionary borderStyleDictionary = new PDBorderStyleDictionary();
        borderStyleDictionary.setWidth(1);
        borderStyleDictionary.setStyle(PDBorderStyleDictionary.STYLE_SOLID);
        widget.setBorderStyle(borderStyleDictionary);

        PDAppearanceDictionary ap = new PDAppearanceDictionary();
        widget.setAppearance(ap);
        PDAppearanceEntry normalAppearance = ap.getNormalAppearance();

        COSDictionary normalAppearanceDict = normalAppearance.getCOSObject();
        PDFont zapfDingbats = new PDType1Font(Standard14Fonts.FontName.ZAPF_DINGBATS);
        normalAppearanceDict.setItem(COSName.Off, createAppearanceStream(pdfDocument, widget, false, zapfDingbats));
        normalAppearanceDict.setItem(COSName.YES, createAppearanceStream(pdfDocument, widget, true, zapfDingbats));
    }

    private static COSObjectable createAppearanceStream(PDDocument document, PDAnnotationWidget widget, boolean on, PDFont font) throws IOException {
        PDRectangle rect = widget.getRectangle();
        PDAppearanceCharacteristicsDictionary appearanceCharacteristics;
        PDAppearanceStream yesAP = new PDAppearanceStream(document);
        yesAP.setBBox(new PDRectangle(rect.getWidth(), rect.getHeight()));
        yesAP.setResources(new PDResources());
        try (PDAppearanceContentStream yesAPCS = new PDAppearanceContentStream(yesAP)) {
            appearanceCharacteristics = widget.getAppearanceCharacteristics();
            PDColor backgroundColor = appearanceCharacteristics.getBackground();
            PDColor borderColor = appearanceCharacteristics.getBorderColour();
            float lineWidth = getLineWidth(widget);
            yesAPCS.setBorderLine(lineWidth, widget.getBorderStyle(), widget.getBorder());
            yesAPCS.setNonStrokingColor(backgroundColor);
            yesAPCS.addRect(0, 0, rect.getWidth(), rect.getHeight());
            yesAPCS.fill();
            yesAPCS.setStrokingColor(borderColor);
            yesAPCS.addRect(lineWidth / 2, lineWidth / 2, rect.getWidth() - lineWidth, rect.getHeight() - lineWidth);
            yesAPCS.stroke();
            if (!on) {
                return yesAP;
            }

            yesAPCS.addRect(lineWidth, lineWidth, rect.getWidth() - lineWidth * 2, rect.getHeight() - lineWidth * 2);
            yesAPCS.clip();

            String normalCaption = appearanceCharacteristics.getNormalCaption();
            if (normalCaption == null) {
                normalCaption = "4"; // Adobe behaviour
            }
            if ("8".equals(normalCaption)) {
                // Adobe paints a cross instead of using the Zapf Dingbats cross symbol
                yesAPCS.setStrokingColor(0f);
                yesAPCS.moveTo(lineWidth * 2, rect.getHeight() - lineWidth * 2);
                yesAPCS.lineTo(rect.getWidth() - lineWidth * 2, lineWidth * 2);
                yesAPCS.moveTo(rect.getWidth() - lineWidth * 2, rect.getHeight() - lineWidth * 2);
                yesAPCS.lineTo(lineWidth * 2, lineWidth * 2);
                yesAPCS.stroke();
            } else {
                Rectangle2D bounds = new Rectangle2D.Float();
                String unicode = null;

                // ZapfDingbats font may be missing or substituted, let's use AFM resources instead.
                FontMetrics metric = Standard14Fonts.getAFM(Standard14Fonts.FontName.ZAPF_DINGBATS.getName());
                for (CharMetric cm : metric.getCharMetrics()) {
                    // The caption is not unicode, but the Zapf Dingbats code in the PDF.
                    // Assume that only the first character is used.
                    if (normalCaption.codePointAt(0) == cm.getCharacterCode()) {
                        BoundingBox bb = cm.getBoundingBox();
                        bounds = new Rectangle2D.Float(bb.getLowerLeftX(), bb.getLowerLeftY(),
                                bb.getWidth(), bb.getHeight());
                        unicode = GlyphList.getZapfDingbats().toUnicode(cm.getName());
                        break;
                    }
                }
                if (bounds.isEmpty()) {
                    throw new IOException("Bounds rectangle for chosen glyph is empty");
                }
                float size = (float) Math.min(bounds.getWidth(), bounds.getHeight()) / 1000;
                // assume that checkmark has square size
                // the calculations approximate what Adobe is doing, i.e. put the glyph in the middle
                float fontSize = (rect.getWidth() - lineWidth * 2) / size * 0.6666f;
                float xOffset = (float) (rect.getWidth() - (bounds.getWidth()) / 1000 * fontSize) / 2;
                xOffset -= bounds.getX() / 1000 * fontSize;
                float yOffset = (float) (rect.getHeight() - (bounds.getHeight()) / 1000 * fontSize) / 2;
                yOffset -= bounds.getY() / 1000 * fontSize;
                yesAPCS.setNonStrokingColor(0f);
                yesAPCS.beginText();
                yesAPCS.setFont(font, fontSize);
                yesAPCS.newLineAtOffset(xOffset, yOffset);
                yesAPCS.showText(unicode);
                yesAPCS.endText();
            }
        }
        return yesAP;
    }

    private static float getLineWidth(PDAnnotationWidget widget) {
        PDBorderStyleDictionary bs = widget.getBorderStyle();
        if (bs != null) {
            return bs.getWidth();
        }
        return 1;
    }
}
