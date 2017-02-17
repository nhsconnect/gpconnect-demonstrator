package uk.gov.hscic.patient;

import java.util.ArrayList;
import java.util.List;

public class BuildHtmlTable {

    public StringBuilder tableBuilder(int numberOfColumns, ArrayList<Object> columnHeaders, ArrayList<Object> startDate,
            String title) {
        // TODO Auto-generated method stub

        StringBuilder buf = new StringBuilder();

        // Creates the tables and the Section Headers
        for (int x = 0; x < 1; x++) {
            buf.append("<h2>" + title + "</h2>" + "<table>" + "<thead>" + "<tr>");

            for (int i = 0; i < numberOfColumns; i++) {

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

        return buf;
    }

    public String tableCreation(List<ArrayList<Object>> headerData, List<ArrayList<Object>> tableData,
            ArrayList<Object> tableTitles) {

        StringBuilder layout = new StringBuilder();
        layout.append("<div>");

        for (int i = 0; i < headerData.size(); i++) {
            StringBuilder table = tableBuilder(headerData.get(i).size(), headerData.get(i), tableData.get(i),
                    tableTitles.get(i).toString());
            layout.append(table);
        }

        layout.append("</div>");

        String htmlTable = layout.toString();

        return htmlTable;
    }

    public String buildEmptyHtml(String sectionName) {
        // TODO Auto-generated method stub
        return  "<div><p>No "+sectionName+" data is recorded for this patient.</p></div>";
    }

}
