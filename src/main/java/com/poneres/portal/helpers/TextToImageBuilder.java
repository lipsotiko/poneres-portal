package com.poneres.portal.helpers;

import com.poneres.portal.signatures.SignatureFont;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;

@Service
public class TextToImageBuilder {

    private final Map<SignatureFont, Integer> fontSizes = new HashMap<>() {
        {
            put(SignatureFont.BERLIMAN, 14);
            put(SignatureFont.CEDARVILLE_CURSIVE, 30);
            put(SignatureFont.DANCING_SCRIPT, 34);
        }
    };

    private final Map<SignatureFont, Integer> heightAdjustments = new HashMap<>() {
        {
            put(SignatureFont.BERLIMAN, 42);
            put(SignatureFont.CEDARVILLE_CURSIVE, 0);
            put(SignatureFont.DANCING_SCRIPT, 0);
        }
    };

    public byte[] convertToPng(String signature, SignatureFont signatureFont) {
        return convertToImage(signature, signatureFont);
    }

    private int computeImageHeight(AttributedString attrString, int maxWidth) {
        BufferedImage buffRenderImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = buffRenderImage.createGraphics();
        AttributedCharacterIterator paragraph = attrString.getIterator();
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        FontRenderContext frc = g2d.getFontRenderContext();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

        float drawPosY = 0;

        // Set position to the index of the first character in the paragraph.
        lineMeasurer.setPosition(paragraphStart);

        // Get lines until the entire paragraph has been displayed.
        while (lineMeasurer.getPosition() < paragraphEnd) {

            // Retrieve next layout. A cleverer program would also cache
            // these layouts until the component is re-sized.
            TextLayout layout = lineMeasurer.nextLayout(maxWidth);
            // Compute pen x position. If the paragraph is right-to-left we
            // will align the TextLayouts to the right edge of the panel.
            // Note: this won't occur for the English text in this sample.
            // Note: drawPosX is always where the LEFT of the text is placed.
            //            float drawPosX = layout.isLeftToRight()
            //                    ? 0 : breakWidth - layout.getAdvance();

            // Move y-coordinate by the ascent of the layout.
            drawPosY += layout.getAscent();

            // Draw the TextLayout at (drawPosX, drawPosY).

            // Move y-coordinate in preparation for next layout.
            drawPosY += layout.getDescent() + layout.getLeading();
        }

        g2d.dispose();
        return (int) Math.ceil(drawPosY);
    }

    private byte[] convertToImage(String signature, SignatureFont signatureFont) {
        Font font = new Font(signatureFont.getInternalName(), Font.PLAIN, fontSizes.getOrDefault(signatureFont, 22));
        int maxWidth = 400;
        int widthAdjustment = 12;
        int heightAdjustment = heightAdjustments.getOrDefault(signatureFont, 12);

        final Map<TextAttribute, Object> map = new HashMap<>();
        map.put(TextAttribute.FONT, font);
        map.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        map.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);
        map.put(TextAttribute.JUSTIFICATION, TextAttribute.JUSTIFICATION_FULL);
        AttributedString attrString = new AttributedString(signature, map);
        int width = maxWidth + widthAdjustment;
        int height = computeImageHeight(attrString, maxWidth) + heightAdjustment;
        BufferedImage buffRenderImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = buffRenderImage.createGraphics();
        AttributedCharacterIterator paragraph = attrString.getIterator();
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        FontRenderContext frc = g2d.getFontRenderContext();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(Color.black);

        // Set break width to width of maxWidth.
        float drawPosY = 0;

        // Set position to the index of the first character in the paragraph.
        lineMeasurer.setPosition(paragraphStart);
        //took from Oracle's LineBreakSample.java
        // Get lines until the entire paragraph has been displayed.
        while (lineMeasurer.getPosition() < paragraphEnd) {

            // Retrieve next layout.
            TextLayout layout = lineMeasurer.nextLayout((float) maxWidth);
            // Compute pen x position. If the paragraph is right-to-left we
            // will align the TextLayouts to the right edge of the panel.
            float drawPosX = layout.isLeftToRight() ? 0 : (float) maxWidth - layout.getAdvance();

            // Move y-coordinate by the ascent of the layout.
            drawPosY += layout.getAscent();

            // Draw the TextLayout at (drawPosX, drawPosY).
            layout.draw(g2d, drawPosX + (widthAdjustment / 2), drawPosY + (heightAdjustment / 2));

            // Move y-coordinate in preparation for next layout.
            drawPosY += layout.getDescent() + layout.getLeading();
        }
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(buffRenderImage, "png", baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return baos.toByteArray();
    }
}
