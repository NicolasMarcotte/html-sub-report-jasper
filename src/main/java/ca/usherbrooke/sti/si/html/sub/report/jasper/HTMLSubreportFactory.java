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

import java.io.ByteArrayInputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

public class HTMLSubreportFactory {

    /**
     * Entry Point
     * The way to use this component in a report is to :
     * 1- add ca.usherbrooke.sti.si.html.sub.report.jasper.HTMLSubreportFactory to your report imports
     * 2- insert a sub report
     * 3- set its ReportExpression to HTMLSubreportFactory.htmlToSubReport(htmlString,subReportWidth)
     * 4- set the ReportExpressionType ot JasperReport
     * <p>
     * @author marn2402
     */
    public static JasperReport htmlToSubReport(String html, int width) throws JRException {

        Tidy tidy = new Tidy();
        tidy.setXHTML(true);
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        tidy.setSmartIndent(true);
        tidy.setBreakBeforeBR(true);
        tidy.setJoinStyles(true);
        tidy.setInputEncoding("UTF-8");
        tidy.setNumEntities(true);

        Document doc = tidy.parseDOM(new ByteArrayInputStream(html.getBytes()), null);

        JRRenderer renderer = new JRRenderer(doc, width);
     
        JasperDesign buildReport = renderer.buildReport();

        final JasperReport compileReport = JasperCompileManager.compileReport(buildReport);

        return compileReport;
    }
}
