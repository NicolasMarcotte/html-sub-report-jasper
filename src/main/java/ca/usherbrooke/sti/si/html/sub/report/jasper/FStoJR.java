/**
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * @author Nicolas Marcotte
 */
package ca.usherbrooke.sti.si.html.sub.report.jasper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextAttribute;
import java.util.HashSet;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.base.JRBoxPen;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import org.xhtmlrenderer.css.constants.IdentValue;
import org.xhtmlrenderer.css.parser.FSColor;
import org.xhtmlrenderer.css.parser.FSRGBColor;
import org.xhtmlrenderer.css.style.derived.BorderPropertySet;
import org.xhtmlrenderer.render.FSFont;
import org.xhtmlrenderer.swing.AWTFSFont;

/**
 *
 * @author marn2402
 */
public class FStoJR {

    public static Font toAwtFont(FSFont fsFont) {
        return ((AWTFSFont) fsFont).getAWTFont();
    }

    private final JRDesignSection details;
    private final JRDesignExpression ALWAYS = new JRDesignExpression("true");
    private final JRDesingStaticTextFactory staticTextFactory = new JRDesingStaticTextFactory(this);
    private final JRDesingShapeFactory shapeFactory = new JRDesingShapeFactory(this);

    private Color currentBackgroundColor = Color.WHITE;
    private Color currentCurrentForgroundColor = Color.BLACK;
    private Font currentFont = new Font("Arial", Font.PLAIN, 11);

    private BorderPropertySet currentBorder;
    private final HashSet<String> styles = new HashSet<>();

    private int currentHeight = -1;

    private final JRDesignBand bands = new JRDesignBand();

    private String currentTextAling = "justify";
    private final JasperDesign jasperDesign;

    private int pageWidth = 572;
    private Color pageBackGroungColor = Color.WHITE;

    public FStoJR(int width) {
        this();
        this.pageWidth = width;
    }

    public FStoJR() {
        this.jasperDesign = new JasperDesign();
        jasperDesign.setName("NoXmlDesignReport");

        jasperDesign.setPageWidth(pageWidth);
        jasperDesign.setColumnSpacing(0);
        jasperDesign.setLeftMargin(0);
        jasperDesign.setRightMargin(0);
        jasperDesign.setTopMargin(0);
        jasperDesign.setBottomMargin(0);
        jasperDesign.setBackground(new JRDesignBand());
        details = (JRDesignSection) jasperDesign.getDetailSection();
        currentBorder = new BorderPropertySet(0, 0, 0, 0);
        bands.setSplitType(SplitTypeEnum.IMMEDIATE);
    }

    public JasperDesign build() {

        jasperDesign.setPageWidth(pageWidth);
        jasperDesign.setColumnWidth(pageWidth);
        jasperDesign.setLeftMargin(0);
        jasperDesign.setRightMargin(0);
        jasperDesign.setTopMargin(0);
        jasperDesign.setBottomMargin(0);

        jasperDesign.setPageHeight(currentHeight);
        getDetails().addBand(getBand());

        return jasperDesign;

    }

    protected void addElementToPage(JRDesignElement element) {

        JRDesignBand frame = getBand();
        final int y = element.getY();
        element.setY(y);

        element.setPositionType(PositionTypeEnum.FIX_RELATIVE_TO_TOP);
        final int currentMaxY = y + element.getHeight();
        if (currentMaxY >= currentHeight) {
            currentHeight = currentMaxY;
            frame.setHeight(currentHeight);
        }

        frame.addElement(element);

    }

    public void setCurrentBackgroundColor(Color toColor) {
        this.currentBackgroundColor = toColor;

    }

    public void setCurrentForgroundColor(Color toColor) {
        this.currentCurrentForgroundColor = toColor;
    }

    public void setCurrentFont(Font awtFont) {
        this.currentFont = awtFont;

    }

    /**
     * @return the details
     */
    public JRDesignSection getDetails() {
        return details;
    }

    /**
     * @return the ALWAYS
     */
    public JRDesignExpression getALWAYS() {
        return ALWAYS;
    }

    /**
     * @return the staticTextFactory
     */
    public JRDesingStaticTextFactory getStaticTextFactory() {
        return staticTextFactory;
    }

    /**
     * @return the currentCurrentForgroundColor
     */
    public Color getCurrentCurrentForgroundColor() {
        return currentCurrentForgroundColor;
    }

    /**
     * @return the currentFont
     */
    public Font getCurrentFont() {
        return currentFont;
    }

    /**
     * @return the bands
     */
    public JRDesignBand getBand() {
        return bands;
    }

    /**
     * @return the jasperDesign
     */
    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    /**
     * @return the pageWidth
     */
    public int getPageWidth() {
        return pageWidth;
    }

    /**
     * @return the currentBackgroundColor
     */
    public Color getCurrentBackgroundColor() {
        return currentBackgroundColor;
    }

    public void addRectangle(int x, int y, int width, int height, boolean transparent) {
        JRDesignRectangle rect = shapeFactory.createJRDesingRectangle(x, y, width, height, transparent);
        addElementToPage(rect);
    }

    public void addStaticText(String text, int x, int y, int width, int height) {

        JRDesignStaticText createStaticText = getStaticTextFactory().createStaticText(text, x, y, width, height);

        addElementToPage(createStaticText);

    }

    public void setBackgroundColor(Color backgroundColor) {
        this.currentBackgroundColor = backgroundColor;
    }

    void addLine(int x1, int y1, int x2, int y2) {
        final JRDesignLine createJRDesingLine = shapeFactory.createJRDesingLine(x1, y1, x2, y2);
        if (!(createJRDesingLine.getWidth() == 1 && createJRDesingLine.getHeight() == 1)) {
            addElementToPage(createJRDesingLine);
        }

    }

    public void setCurrentBorder(BorderPropertySet border) {
        this.currentBorder = border;

    }

    public BorderPropertySet getCurrentBorder() {
        return currentBorder;
    }

    public String getCurrentBorderString() {
        if (currentBorder == null) {
            return "noBorder";
        }
        StringBuilder sb = new StringBuilder();
        if (!currentBorder.noBottom()) {
            sb.append("bottom");
            sb.append(currentBorder.bottomStyle());
        }
        if (!currentBorder.noTop()) {
            sb.append("top");
            sb.append(currentBorder.bottomStyle());
        }
        if (!currentBorder.noLeft()) {
            sb.append("left");
            sb.append(currentBorder.bottomStyle());
        }
        if (!currentBorder.noRight()) {
            sb.append("right");
            sb.append(currentBorder.bottomStyle());
        }

        return sb.toString();
    }

    public void setCurrentTextAling(String currentTextAling) {
        this.currentTextAling = currentTextAling;
    }

    public String getCurrentTextAling() {
        return currentTextAling;
    }

    public String getCurrentStyleName() {
        StringBuffer sb = new StringBuffer();
        appendFontInfo(sb);
        sb.append('-');
        appendColors(sb);
        sb.append('-');
        appendBorderStyleName(sb);
        return sb.toString();
    }

    public String getCurrentBorderStyleName() {
        StringBuffer sb = new StringBuffer();
        appendBorderStyleName(sb);
        return sb.toString();
    }

    public void appendColors(StringBuffer sb) {

        sb.append(this.getCurrentBackgroundColor().getRGB());
        sb.append('-');
        sb.append(this.getCurrentCurrentForgroundColor().getRGB());
    }

    private void appendFontInfo(StringBuffer sb) {
        sb.append(getCurrentFont().getName());
        sb.append('-');
        sb.append(getCurrentFont().getSize());
        sb.append('-');
        sb.append(getCurrentFont().isBold());
        sb.append('-');
        sb.append(getCurrentFont().isItalic());
    }

    private void appendBorderStyleName(StringBuffer sb) {
        if (getCurrentBorder().noBottom() && getCurrentBorder().noTop() && getCurrentBorder().noLeft() && getCurrentBorder().noRight()) {
            sb.append("noborder");
            return;
        }

        if (!getCurrentBorder().noBottom()) {
            sb.append("botom");
            sb.append('-');
            sb.append(getCurrentBorder().bottomStyle().asString());
            sb.append('-');
            sb.append(toColor(getCurrentBorder().bottomColor()).getRGB());
        }

        if (!getCurrentBorder().noTop()) {
            sb.append("top");
            sb.append('-');
            sb.append(getCurrentBorder().topStyle().asString());
            sb.append('-');
            sb.append(toColor(getCurrentBorder().topColor()).getRGB());
        }
        if (!getCurrentBorder().noLeft()) {
            sb.append("left");
            sb.append('-');
            sb.append(getCurrentBorder().leftStyle().asString());
            sb.append('-');
            sb.append(toColor(getCurrentBorder().leftColor()).getRGB());
        }
        if (!getCurrentBorder().noRight()) {
            sb.append("right");
            sb.append('-');
            sb.append(getCurrentBorder().rightStyle().asString());
            sb.append('-');
            sb.append(toColor(getCurrentBorder().rightColor()).getRGB());
        }
    }

    public static Color toColor(FSColor color) {
        if (color instanceof FSRGBColor) {
            FSRGBColor rgb = (FSRGBColor) color;
            return new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
        } else {
            throw new RuntimeException("internal error: unsupported color class " + color.getClass().getName());
        }
    }

    public void configureFont(JRDesignStyle style) {
        style.setFontName(getCurrentFont().getFamily());
        style.setForecolor(getCurrentCurrentForgroundColor());
        style.setFontSize(getCurrentFont().getSize());
        style.setBold(getCurrentFont().isBold());
        style.setItalic(getCurrentFont().isItalic());
        style.setStrikeThrough((Boolean) getCurrentFont().getAttributes().get(TextAttribute.STRIKETHROUGH));
        style.setUnderline((Boolean) getCurrentFont().getAttributes().get(TextAttribute.UNDERLINE));
    }

    public void configureBorder(JRDesignStyle style) {
        if (getCurrentBorder() != null) {
            if (!getCurrentBorder().noBottom()) {
                configureBorderPen(style.getLineBox().getBottomPen(), getCurrentBorder().bottomStyle(), toColor(getCurrentBorder().bottomColor()), getCurrentBorder().bottom());
                style.getLineBox().getBottomPen().setLineStyle(convertBorderEnum(getCurrentBorder().bottomStyle()));

            }

            if (!getCurrentBorder().noTop()) {
                configureBorderPen(style.getLineBox().getTopPen(), getCurrentBorder().topStyle(), toColor(getCurrentBorder().topColor()), getCurrentBorder().top());
                style.getLineBox().getTopPen().setLineStyle(convertBorderEnum(getCurrentBorder().topStyle()));

            }
            if (!getCurrentBorder().noLeft()) {
                style.getLineBox().getLeftPen().setLineStyle(convertBorderEnum(getCurrentBorder().leftStyle()));
                configureBorderPen(style.getLineBox().getLeftPen(), getCurrentBorder().topStyle(), toColor(getCurrentBorder().leftColor()), getCurrentBorder().left());
                style.getLineBox().setLeftPadding(1);
            }
            if (!getCurrentBorder().noRight()) {
                configureBorderPen(style.getLineBox().getRightPen(), getCurrentBorder().topStyle(), toColor(getCurrentBorder().rightColor()), getCurrentBorder().right());
                style.getLineBox().getRightPen().setLineStyle(convertBorderEnum(getCurrentBorder().rightStyle()));

                style.getLineBox().setRightPadding(1);

            }
        }

    }

    private LineStyleEnum convertBorderEnum(IdentValue bottomStyle) {
        switch (bottomStyle.asString()) {

            case "dotted":
                return LineStyleEnum.DOTTED;
            case "dashed":
                return LineStyleEnum.DASHED;
            case "double":
                return LineStyleEnum.DOUBLE;
            default:
            case "solid":
                return LineStyleEnum.SOLID;
        }

    }

    private void configureBorderPen(JRBoxPen bottomPen, IdentValue bottomStyle, Color toColor, float width) {
        bottomPen.setLineColor(toColor);
        bottomPen.setLineWidth(width);
    }

    /**
     * @return the styles
     */
    public HashSet<String> getStyles() {
        return styles;
    }

    void addStyle(JRDesignStyle style) {
        try {
            styles.add(style.getName());
            jasperDesign.addStyle(style);
        } catch (JRException ex) {
            throw new IllegalStateException(ex);

        }
    }

    public void addLineBreak(int y) {
        addElementToPage(shapeFactory.createBreak(y));
    }

    void setpageBackGroungColor(FSColor backgroundColor) {
        this.pageBackGroungColor = toColor(backgroundColor);
    }

    public Color getPageBackGroungColor() {
        return pageBackGroungColor;
    }

}
