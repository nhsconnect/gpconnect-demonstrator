package uk.gov.hscic.patient;

import java.util.ArrayList;

public class buildHTMLTABLE {
    
    public StringBuilder tableBuilder(int numberOfTables, int numberOfColumns, ArrayList<Object> columnHeaders,
            ArrayList<Object> startDate, String title) {
        // TODO Auto-generated method stub

        StringBuilder buf = new StringBuilder();

        // Creates the tables and the Section Headers
        for (int x = 0; x < numberOfTables; x++) {
            buf.append("<h2>" + title + "</h2>" + "<table>" + "<thead>" + "<tr>");

            for (int i = 0; i < numberOfColumns; i++) {
                // buf.append( "<th>"+columnHeaders.get(i)+"</th>");
                buf.append("<th>" + columnHeaders.get(i) + "</th>");
            }

            buf.append("</tr>" + "</thead>" + "<tbody>");
            // Creates the rows

            for (int i = 0; i < startDate.size(); i = i + numberOfColumns) {
                buf.append("<tr>");
                for (int j = 0; j < numberOfColumns; j++) {
                    buf.append("<td>");
                    buf.append(startDate.get(i + j));
                    buf.append("</td>");
                }
                buf.append("</tr>");

            }

            buf.append("</tbody></table>");

        }

        // String htmlTable = buf.toString();
        return buf;
    }
}
