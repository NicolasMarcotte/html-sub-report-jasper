This componment render a html file to a Jasper Report subreport using flying saucer

The way to use this component in a report is to :

1.  add ca.usherbrooke.sti.si.html.sub.report.jasper.HTMLSubreportFactory to your report imports
2.  insert a sub report
3.  set its ReportExpression to HTMLSubreportFactory.htmlToSubReport(htmlString,subReportWidth)
4.  set the ReportExpressionType ot JasperReport
