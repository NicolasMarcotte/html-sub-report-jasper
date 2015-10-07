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

import net.sf.jasperreports.engine.design.JRDesignBreak;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.type.BreakTypeEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;

/**
 *
 * @author marn2402
 */
public class JRDesingShapeFactory {

    private final FStoJR fsToJR;

    public JRDesingShapeFactory(final FStoJR outer) {
        super();
        this.fsToJR = outer;
    }

    public JRDesignRectangle createJRDesingRectangle(int x, int y, int width, int height ,boolean transparent) {
        JRDesignRectangle rectangle = new JRDesignRectangle();
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setBackcolor(fsToJR.getCurrentBackgroundColor());
        rectangle.setHeight(height);
        rectangle.setWidth(width);
     
        rectangle.getLinePen().setLineWidth(fsToJR.getCurrentBorder().width());
        rectangle.setForecolor(fsToJR.getCurrentCurrentForgroundColor());
        rectangle.setMode(transparent?ModeEnum.TRANSPARENT:ModeEnum.OPAQUE);
        return rectangle;
    }

    public JRDesignLine createJRDesingLine(int x1, int y1, int x2, int y2) {
        JRDesignLine line = new JRDesignLine();
        if (x2 < x1) {
            int t = x1;
            x1 = x2;
            x2 = t;
        }
        if (y2 < y1) {
            int t = y1;
            y1 = y2;
            y2 = t;
        }

        line.setX(x1);
        line.setY(y1);
        line.setWidth(x2 - x1);
        line.setHeight(y2 - y1);
        line.setForecolor(fsToJR.getCurrentCurrentForgroundColor());
        line.setPositionType(PositionTypeEnum.FIX_RELATIVE_TO_TOP);
        return line;
    }

    public JRDesignBreak createBreak(int y){
        JRDesignBreak lineBreak=new JRDesignBreak();
        lineBreak.setY(y);
        lineBreak.setX(1);
        lineBreak.setWidth(1);
        lineBreak.setHeight(1);
        lineBreak.setType(BreakTypeEnum.PAGE);
        lineBreak.setPositionType(PositionTypeEnum.FIX_RELATIVE_TO_TOP);
        return lineBreak;
    }
 

}
