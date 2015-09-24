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
 * 
 * @author Nicolas Marcotte
 */
package ca.usherbrooke.sti.si.html.sub.report.jasper;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.xhtmlrenderer.css.constants.CSSName;
import org.xhtmlrenderer.css.constants.IdentValue;
import org.xhtmlrenderer.css.style.derived.BorderPropertySet;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.newtable.TableCellBox;
import org.xhtmlrenderer.render.AnonymousBlockBox;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.render.Box;
import org.xhtmlrenderer.render.InlineLayoutBox;
import org.xhtmlrenderer.render.InlineText;

/**
 *
 * @author marn2402
 */
class BoxVisitor {

    protected static final Set<String> styleNodeName = new HashSet<>(Arrays.asList(new String[]{"a", "em", "b", "i", "style", "font", "mark", "small", "strong", "del", "ins", "sup", "sub"}));
    protected final FStoJR fStoJR;
    protected final LayoutContext layoutContext;

    BoxVisitor(FStoJR fStoJR, LayoutContext ctx) {
        layoutContext = ctx;
        this.fStoJR = fStoJR;

    }

    protected void convertTree(Box node, int depth) {
        String space = "";
        for (int i = 0; i < depth; i++) {
            space += " ";
        }
        System.out.print(space + node);
        if (node instanceof BlockBox) {
            final BlockBox blocBox = (BlockBox) node;
            processBlock(blocBox);

        }
        System.out.println();
        List<Box> children = node.getChildren();

        for (Box child : children) {

            convertTree(child, depth + 1);
        }

    }

    protected void processBlock(BlockBox node) {
        System.out.print(node.getChildrenContentType());
        int leftBorder = 0;
        int rightBorder = 0;
        int topBorder = 0;
        int bottomBorder = 0;
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
        if (node.getMargin(layoutContext) != null) {
            topMargin = (int) Math.ceil(node.getMargin(layoutContext).top());
        }
        fStoJR.setCurrentBorder(node.getStyle().getBorder(layoutContext));

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
                }
                IdentValue textAlign = node.getStyle().getIdent(CSSName.TEXT_ALIGN);
                if (textAlign != null) {
                    fStoJR.setCurrentTextAling(textAlign.asString());
                }

                fStoJR.setCurrentFont(FStoJR.toAwtFont(node.getStyle().getFSFont(layoutContext)));

                fStoJR.addStaticText(content, node.getAbsX(), node.getAbsY() + topMargin, node.getWidth(), node.getHeight() - topMargin);
                break;
            case BlockBox.CONTENT_EMPTY:
                if (hasBorder((BlockBox) node.getParent())) {
                    fStoJR.setCurrentBorder(node.getParent().getStyle().getBorder(layoutContext));
                }
                fStoJR.addRectangle(node.getAbsX(), node.getAbsY() + topMargin, node.getWidth(), node.getHeight() - topMargin, false);
                break;
            case BlockBox.CONTENT_BLOCK:
                if(node instanceof TableCellBox && node.getChildCount()>0&&node.getChild(0) instanceof BlockBox)
                      fStoJR.addRectangle(node.getAbsX(), node.getAbsY() + topMargin, node.getWidth(), node.getHeight() - topMargin, false);
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
            InlineLayoutBox inline = (InlineLayoutBox) lineBox;
            for (Object child : inline.getInlineChildren()) {
                if (child instanceof InlineText) {
                    String parentTag = "";
                    InlineText inlineText = (InlineText) child;
                    if (inlineText.getTextNode() != null && inlineText.getTextNode().getParentNode() != null) {
                        parentTag = inlineText.getTextNode().getParentNode().getNodeName();
                        if (styleNodeName.contains(parentTag)) {
                            sb.append("<").append(parentTag).append(">");
                        }
                    }
                    sb.append(inlineText.getSubstring());
                    if (inlineText.getTextNode() != null && inlineText.getTextNode().getParentNode() != null) {
                        if (styleNodeName.contains(parentTag)) {
                            sb.append("</").append(parentTag).append(">");
                        }
                    }

                } else if (child instanceof InlineLayoutBox) {
                    sb.append(extractText((InlineLayoutBox) child));
                }

            }
            sb.append("\n");

        }
        return sb.toString();
    }

    private String getGlyph(IdentValue ident) {
        String name = ident.asString();
        switch (name) {
            case "disc":
                return "<font face=\"DejaVu Sans\">&#9679;&nbsp</font>";
            case "square":
                return "<font face=\"DejaVu Sans\">&#9642;&nbsp</font>";
            case "circle":
            default:
                return "<font face=\"DejaVu Sans\">&#9675;&nbsp</font>";
        }
    }

    private boolean hasBorder(Box node) {
        BorderPropertySet border = node.getStyle().getBorder(layoutContext);
        return !(border.noBottom() && border.noTop() && border.noLeft() && border.noRight());
    }

}
