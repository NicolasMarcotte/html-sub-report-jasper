Purpose
===========
This componment render a html file to a Jasper Report subreport using flying saucer

Usage
============
The way to use this component in a report is to :

1.  Add ca.usherbrooke.sti.si.html.sub.report.jasper.HTMLSubreportFactory to your report imports
2.  Insert a sub report
3.  Set its ReportExpression to HTMLSubreportFactory.htmlToSubReport(htmlString,subReportWidth)
4.  Set its ReportExpressionType ot JasperReport

*Make sur that the font DejaVu Sans is available to JasperReport, this font is used for the list bullets*

Knowns isusses and limitations
=====================

1. the padding is not respected in the final rendering
2. the margins are not respected in the final rendering
3. the support for images was not implemented
4. the only style of border currently supported are solid and they are always collaspsed

Disclaimer
===============
This code is given as is to comply with the LGPL. 
**Caveat emptor**

Your gladly encourage to fork this project if you need it to do more.

License
=============

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License
as published by the Free Software Foundation; either version 2.1
of the License, or (at your option) any later version.
 
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 
@author Nicolas Marcotte
