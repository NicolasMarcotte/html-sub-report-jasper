This componment render a html file to a Jasper Report subreport using flying saucer

The way to use this component in a report is to :

1.  insert a sub report
2.  set its ReportExpression to HTMLSubreportFactory.htmlToSubReport(htmlString,subReportWidth)
3.  set the ReportExpressionType ot JasperReport
