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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;


public class Main {

    private static boolean done;

    public static void main(String[] args) throws JRException {
        Tidy tidy = new Tidy();
        tidy.setXHTML(true);
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        tidy.setSmartIndent(true);
        tidy.setBreakBeforeBR(true);
        tidy.setJoinStyles(true);
        tidy.setInputEncoding("UTF-8");
        tidy.setNumEntities(true);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Document doc = tidy.parseDOM(Main.class.getResourceAsStream("/test.html"), null);

        JRRenderer renderer = new JRRenderer(doc, 572);

        JasperDesign jasperDesing = renderer.buildReport();

        JasperReport compileReport = JasperCompileManager.compileReport(jasperDesing);
        JasperCompileManager.writeReportToXmlFile(compileReport, "NoXmlDesignReport.jrxml");
        JasperPrint fillReport = JasperFillManager.fillReport(compileReport, new HashMap<String, Object>(), new JREmptyDataSource(1));
        JasperExportManager.exportReportToPdfFile(fillReport,"NoXmlDesignReport.pdf");
  
           

    }

}
