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

import net.sf.jasperreports.engine.JRTextAlignment;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;

/**
 *
 * @author marn2402
 */
public class JRDesingStaticTextFactory {

    private final FStoJR fsToJR;

    public JRDesingStaticTextFactory(final FStoJR outer) {
        super();
        this.fsToJR = outer;
    }

    public JRDesignStaticText createStaticText(String text, int x, int y, int width, int height) {
        JRDesignStaticText staticText = new JRDesignStaticText();
        final String styleName = fsToJR.getCurrentStyleName();
        if (!fsToJR.getStyles().contains(styleName)) {
            JRDesignStyle style = new JRDesignStyle();
            fsToJR.configureFont(style);
            fsToJR.configureBorder(style);
            style.setForecolor(fsToJR.getCurrentCurrentForgroundColor());
            style.setBackcolor(fsToJR.getCurrentBackgroundColor());
            style.setName(styleName);
            
            fsToJR.addStyle(style);
           

        }
        configureTextAligment(staticText);
        staticText.setMarkup("html");
        staticText.setX(x);
        staticText.setY(y);
        staticText.setWidth(width);
        staticText.setHeight(height);
        staticText.setStyleNameReference(styleName);
        staticText.setText(text);
        staticText.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        staticText.setMode(fsToJR.getCurrentBackgroundColor().equals(fsToJR.getPageBackGroungColor())?ModeEnum.TRANSPARENT:ModeEnum.OPAQUE);

        return staticText;
    }

    protected void configureTextAligment(JRTextAlignment style) {
        switch (fsToJR.getCurrentTextAling()) {
            default:
            case "justify":
                style.setHorizontalTextAlign(HorizontalTextAlignEnum.JUSTIFIED);
                break;
            case "left":
                style.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
                break;
            case "right":
                style.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
                break;
            case "center":
                style.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
                break;
        }
    }

 
}
