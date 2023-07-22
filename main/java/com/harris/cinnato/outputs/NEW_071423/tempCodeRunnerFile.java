//Test!!!!!!!!
 // Process child node 'qualifiedAircraftId'
   String expressionChildX10 = ".//nxce:departurePoint";
   NodeList childNodesX10 = (NodeList) xPath.compile(expressionChildX10).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean hasFix = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX10.getLength(); j++) {
    Node nNodeX10 = childNodesX10.item(j);
    if (nNodeX10.getNodeType() == Node.ELEMENT_NODE) {
        Element track = (Element) nNodeX10;
        row = row + "," + track.getElementsByTagName("nxce:airport").item(0).getTextContent();
        row = track.getElementsByTagName("nxce:airport").item(0).getTextContent();
          
       
        // Check if descendants exist for the child node
        NodeList descendants = track.getElementsByTagName("nxce:fix");
        if (descendants.getLength() > 0) {
           // note fix is true under this code block
            hasFix = true;
            // row = row + "," + "FIX";
          //  row = row + "," + "FIX";
           // row = row + "," + track.getElementsByTagName("nxce:airport").item(0).getTextContent();

             if(!departureAirport.isEmpty())
             {                 
          //   row = row + "," + "Dep";              
             


            // Iterate over descendants
            for (int k = 0; k < descendants.getLength(); k++) {
                Node descendantNode = descendants.item(k);
                if (descendantNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element descendantElement = (Element) descendantNode;
                    descendantElement.getElementsByTagName("nxce:latitidue").item(0).getTextContent();




                    // Access further down nodes within the descendant element
                    NodeList furtherDescendants = descendantElement.getElementsByTagName("nxce:latitudeDMS");
                    for (int m = 0; m < furtherDescendants.getLength(); m++) {
                        Node furtherDescendantNode = furtherDescendants.item(m);
                        if (furtherDescendantNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element furtherDescendantElement = (Element) furtherDescendantNode;
                            row = row + "," + furtherDescendantElement.getAttribute("degrees");
                            row = row + "," + furtherDescendantElement.getAttribute("direction");
                            row = row + "," + furtherDescendantElement.getAttribute("minutes");   
                            System.out.println(row);

                            // Process further descendant elements as needed
                        }
                    }

                    NodeList furtherDescendants1 = descendantElement.getElementsByTagName("nxce:longitudeDMS");
                    for (int m = 0; m < furtherDescendants1.getLength(); m++) {
                        Node furtherDescendantNode1 = furtherDescendants1.item(m);
                        if (furtherDescendantNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element furtherDescendantElement1 = (Element) furtherDescendantNode1;
                            row = row + "," + furtherDescendantElement1.getAttribute("degrees");
                            row = row + "," + furtherDescendantElement1.getAttribute("direction");
                            row = row + "," + furtherDescendantElement1.getAttribute("minutes");   




                        }
                    }


                } //end if statment


                }
            }
        }
    }
}
