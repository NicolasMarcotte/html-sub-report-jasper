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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.xhtmlrenderer.css.constants.CSSName;
import org.xhtmlrenderer.css.constants.IdentValue;
import org.xhtmlrenderer.css.parser.FSRGBColor;
import org.xhtmlrenderer.css.style.CalculatedStyle;
import org.xhtmlrenderer.css.style.derived.BorderPropertySet;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.render.AnonymousBlockBox;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.render.Box;
import org.xhtmlrenderer.render.FSFont;
import org.xhtmlrenderer.render.InlineLayoutBox;
import org.xhtmlrenderer.render.InlineText;
import org.xhtmlrenderer.swing.AWTFSFont;

/**
 *
 * @author marn2402
 */
class BoxVisitor {

    protected static final Set<String> inlineStyleNodeName = new HashSet<>(Arrays.asList(new String[]{"a", "sub", "sup"}));
    protected final FStoJR fStoJR;
    protected final LayoutContext layoutContext;
    protected BorderPropertySet currentBorderOffset = new BorderPropertySet(0, 0, 0, 0);

    BoxVisitor(FStoJR fStoJR, LayoutContext ctx) {
        layoutContext = ctx;
        this.fStoJR = fStoJR;

    }

    protected void convertTree(Box node, int depth) {
//        String space = "";
//        for (int i = 0; i < depth; i++) {
//            space += " ";
//        }

        if (node instanceof BlockBox) {
            final BlockBox blocBox = (BlockBox) node;
            processBlock(blocBox);

        }
        if (node.getElement() != null && "body".equals(node.getElement().getTagName())&&node.getStyle().getBackgroundColor()!=null) {
            fStoJR.setpageBackGroungColor(node.getStyle().getBackgroundColor());
        }
        BorderPropertySet border = node.getStyle().getBorder(layoutContext);

        List<Box> children = node.getChildren();

        for (Box child : children) {

            convertTree(child, depth + 1);
        }

    }

    protected void processBlock(BlockBox node) {

        BorderPropertySet border = node.getStyle().getBorder(layoutContext);

        if (node.getStyle().getBackgroundColor() != null) {
            fStoJR.setCurrentBackgroundColor(FStoJR.toColor((node.getStyle().getBackgroundColor())));
        } else {
            fStoJR.setCurrentBackgroundColor(Color.WHITE);
        }
        if (node.getStyle().getColor() != null) {
            fStoJR.setCurrentForgroundColor(FStoJR.toColor(node.getStyle().getColor()));
        }
        int topMargin = 0;
        int bottomMargin = 0;
        int leftMargin = 0;
        int rightMargin = 0;

        if (node.getMargin(layoutContext) != null) {
            topMargin = (int) Math.ceil(node.getMargin(layoutContext).top());
            rightMargin = (int) Math.ceil(node.getMargin(layoutContext).right());
            leftMargin = (int) Math.ceil(node.getMargin(layoutContext).left());
            bottomMargin = (int) Math.ceil(node.getMargin(layoutContext).bottom());
        }
        fStoJR.setCurrentBorder(border);

        if (node.getStyle().isForcePageBreakAfter()) {
            fStoJR.addLineBreak(node.getHeight() + node.getAbsY());
        }
        if (node.getStyle().isForcePageBreakBefore()) {
            fStoJR.addLineBreak(node.getAbsY());
        }
        if (node.getStyle().getFSFont(layoutContext) != null) {
            fStoJR.setCurrentFont(FStoJR.toAwtFont(node.getStyle().getFSFont(layoutContext)));
        }

        int xPosition = (int) Math.ceil(node.getAbsX() + leftMargin);
        int yPosition = (int) Math.ceil(node.getAbsY() + topMargin);
        int width = (int) Math.ceil(node.getWidth() - (leftMargin + rightMargin));
        int height = (int) Math.ceil(node.getHeight() - (topMargin + bottomMargin));

        switch (node.getChildrenContentType()) {

            case BlockBox.CONTENT_INLINE:

                String content = extractText(node);
                if (node.getMarkerData() != null) {
                    if (node.getMarkerData().getTextMarker() != null) {
                        content = node.getMarkerData().getTextMarker().getText() + " " + content;
                    } else if (node.getMarkerData().getGlyphMarker() != null) {
                        String glyph = getGlyph(node.getStyle().getIdent(CSSName.LIST_STYLE_TYPE));
                        content = glyph + " " + content;

                    }
                }
                if (node instanceof AnonymousBlockBox) {
                    if (node.getParent() instanceof BlockBox) {
                        final BlockBox parent = (BlockBox) node.getParent();
                        if (parent.getMarkerData() != null) {
                            if (parent.getMarkerData().getTextMarker() != null) {
                                content = parent.getMarkerData().getTextMarker().getText() + " " + content;
                            } else if (node.getMarkerData().getGlyphMarker() != null) {
                                String glyph = getGlyph(node.getStyle().getIdent(CSSName.LIST_STYLE_TYPE));
                                content = glyph + " " + content;

                            }
                        }
                    }

                }

                if (node.getElement().getNodeName().equals("pre")) {
                    content = "<pre>" + content + "</pre>";
                } else {
                    content = content.replace("\n", "");
                }
                IdentValue textAlign = node.getStyle().getIdent(CSSName.TEXT_ALIGN);
                if (textAlign != null) {
                    fStoJR.setCurrentTextAling(textAlign.asString());
                }

                fStoJR.addStaticText(content, xPosition, yPosition, width, height);
                break;
            case BlockBox.CONTENT_EMPTY:
                if ("hr".equals(node.getElement().getTagName())) {

                    fStoJR.addLine(xPosition, yPosition, width, yPosition);
                } else if (hasBorder(node)) {
                    fStoJR.addStaticText("", (int) (xPosition), (int) (yPosition), (int) (width), (int) (height));
                }
                break;
            case BlockBox.CONTENT_BLOCK:

                if (hasBorder(node)) {
                    fStoJR.addStaticText("", (int) (xPosition), (int) (yPosition), (int) (width), (int) (height));
                }
                break;
        }

    }

    protected String extractText(Box lineBox) {
        StringBuilder sb = new StringBuilder();
        for (Object content : lineBox.getChildren()) {
            if (content instanceof Box) {
                sb.append(extractText((Box) content));

            } else {
                System.out.println("unexpected");
            }
        }
        if (lineBox instanceof InlineLayoutBox) {
            ArrayList<String> tagToClose = new ArrayList<>();
            InlineLayoutBox inline = (InlineLayoutBox) lineBox;
            for (Object child : inline.getInlineChildren()) {
                if (child instanceof InlineText) {
                    String parentTag = "";
                    InlineText inlineText = (InlineText) child;
                    String style = "";
                    if (inlineText.getParent() != null && inlineText.getParent().getStyle() != null) {

                        if (inlineText.getTextNode() != null && inlineText.getTextNode().getParentNode() != null && inlineStyleNodeName.contains(inlineText.getTextNode().getParentNode().getNodeName())) {
                            parentTag = inlineText.getTextNode().getParentNode().getNodeName();
                        } else {
                            parentTag = "span";
                        }
                        style = toStyleString(inlineText.getParent().getStyle());

                        if (!style.isEmpty()) {

                            sb.append("<").append(parentTag);

                            sb.append(" style=\"").append(style).append("\"");

                            sb.append(">");
                            tagToClose.add(parentTag);

                        }

                    }
                    sb.append(inlineText.getSubstring());

                } else if (child instanceof InlineLayoutBox) {
                    sb.append(extractText((InlineLayoutBox) child));
                }

            }
            for (String tag : tagToClose) {
                sb.append("</").append(tag).append(">");
            }
            sb.append("\n");

        }
        return sb.toString();
    }

    private String getGlyph(IdentValue ident) {
        String name = ident.asString();
        switch (name) {
            case "disc":
                return "<span style=\"font-family:DejaVu Sans;\">&#8226;&nbsp</span>";
            case "square":
                return "<span style=\"font-family:DejaVu Sans;\">&#9642;&nbsp</span>";
            case "circle":
            default:
                return "<span style=\"font-family:DejaVu Sans;\">&#9702;&nbsp</span>";
        }
    }

    private boolean hasBorder(Box node) {
        BorderPropertySet border = node.getStyle().getBorder(layoutContext);
        return !(border.noBottom() && border.noTop() && border.noLeft() && border.noRight());
    }

    private String toStyleString(CalculatedStyle style) {
        StringBuilder sb = new StringBuilder();
        if (style.getColor() != null) {
            final FSRGBColor fsRBGColor = (FSRGBColor) style.getColor();
            final Color currentColor = fStoJR.getCurrentCurrentForgroundColor();
            if (!(fsRBGColor.getBlue() == currentColor.getBlue() && currentColor.getRed() == fsRBGColor.getRed() && currentColor.getGreen() == fsRBGColor.getGreen())) {
                final FSRGBColor color = (FSRGBColor) style.getColor();

                sb.append("color:").append(String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue())).append(";");
            }
        }
        if (style.getBackgroundColor() != null) {
            final FSRGBColor fsRBGColor = (FSRGBColor) style.getBackgroundColor();
            final Color currentColor = fStoJR.getCurrentBackgroundColor();
            if (!(fsRBGColor.getBlue() == currentColor.getBlue() && currentColor.getRed() == fsRBGColor.getRed() && currentColor.getGreen() == fsRBGColor.getGreen())) {
                final FSRGBColor color = (FSRGBColor) style.getBackgroundColor();

                sb.append("background-color:").append(String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue())).append(";");
            }
        }
        if (style.getTextDecorations() != null && !style.getTextDecorations().isEmpty()) {
            sb.append("text-decoration: ");
            for (Object textDeco : style.getTextDecorations()) {
                sb.append(textDeco).append("; ");
            }
        }
        FSFont fsFont = style.getFSFont(layoutContext);
        if (fsFont != null && !((AWTFSFont) fsFont).getAWTFont().equals(fStoJR.getCurrentFont())) {

            Font awtFont = ((AWTFSFont) fsFont).getAWTFont();
            if (awtFont.isBold()) {
                sb.append("font-weight:bold; ");
            }
            if (awtFont.isItalic()) {
                sb.append("font-style:italic; ");
            }

            if (!awtFont.getFamily().equals(fStoJR.getCurrentFont().getFamily()) && fStoJR.getCurrentFont().getFamily() != null) {
                sb.append("font-family:").append(awtFont.getFamily()).append("; ");
            }

        }

        if (fsFont.getSize2D() != fStoJR.getCurrentFont().getSize2D()) {
            sb.append("font-size:").append(fsFont.getSize2D()).append("pt; ");
        }
        return sb.toString().trim();
    }

}
