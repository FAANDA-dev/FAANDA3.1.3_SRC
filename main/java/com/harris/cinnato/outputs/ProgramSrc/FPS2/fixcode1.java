
// Iterate over individual messages
// String row = "";
String expression = "//fdm:fltdMessage";
NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
for (int i = 0; i < nodeList.getLength(); i++) {
    Node nNode = nodeList.item(i);
    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;

        // Check if message is a flight plan message
        if (eElement.getAttribute("msgType").equals("trackInformation")) {
            row = eElement.getElementsByTagName("nxce:aircraftId").item(0).getTextContent();

            //departure point airport *****************************************
            // Process child node 'qualifiedAircraftId'
            String expressionChildX7 = ".//nxce:departurePoint";
            NodeList childNodesX7 = (NodeList) xPath.compile(expressionChildX7).evaluate(eElement, XPathConstants.NODESET);

            // Check if any descendants are found
            boolean hasFix1 = false;
            // Iterate over child nodes
            for (int j = 0; j < childNodesX7.getLength(); j++) {
                Node nNodeX7 = childNodesX7.item(j);
                if (nNodeX7.getNodeType() == Node.ELEMENT_NODE) {
                    Element trackZ = (Element) nNodeX7;

                    // Check if descendants exist for the child node
                    NodeList descendants = trackZ.getElementsByTagName("nxce:airport");
                    if (descendants.getLength() > 0) {
                        hasFix1 = true;
                        row = row + "," + trackZ.getElementsByTagName("nxce:airport").item(0).getTextContent();
                    }
                }
            }

            //arrival point airport *****************************************
            // Process child node 'qualifiedAircraftId'
            String expressionChildX30 = ".//nxce:arrivalPoint";
            NodeList childNodesX30 = (NodeList) xPath.compile(expressionChildX30).evaluate(eElement, XPathConstants.NODESET);

            // Check if any descendants are found
            boolean hasFix3 = false;
            // Iterate over child nodes
            for (int j = 0; j < childNodesX30.getLength(); j++) {
                Node nNodeX30 = childNodesX30.item(j);
                if (nNodeX30.getNodeType() == Node.ELEMENT_NODE) {
                    Element track3 = (Element) nNodeX30;
                    row = row + "," + track3.getElementsByTagName("nxce:airport").item(0).getTextContent();

                    // Check if descendants exist for the child node
                    NodeList descendants3 = track3.getElementsByTagName("nxce:airport");
                    if (descendants3.getLength() > 0) {
                        hasFix3 = true;
                        row = row + "," + track3.getElementsByTagName("nxce:airport").item(0).getTextContent();
                    }
                }
            }
        }
    }
}



               
//**************fix!!!! regular
 
   // Process child node 'qualifiedAircraftId'
   String expressionChildX10 = ".//nxcm:qualifiedAircraftId";
   NodeList childNodesX10 = (NodeList) xPath.compile(expressionChildX10).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean hasFix2 = false;
 // Iterate over child nodes
for (j = 0; j < childNodesX10.getLength(); j++) {
    Node nNodeX10 = childNodesX10.item(j);
    if (nNodeX10.getNodeType() == Node.ELEMENT_NODE) {
        Element track = (Element) nNodeX10;

        // Check if descendants exist for the child node
        NodeList descendants5 = track.getElementsByTagName("nxce:fix");
        if (descendants5.getLength() > 0) {
            hasFix2 = true;
             row = row + "," + "FIX_X";
             System.out.println(row);

             
            // Iterate over descendants
            for (int k = 0; k < descendants5.getLength(); k++) {
                Node descendantNode = descendants5.item(k);
                if (descendantNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element descendantElement = (Element) descendantNode;

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





                }
            }
        }
    }
}

//**************fix!!!! departure

// Process child node 'departurePoint'
String expressionChildX17 = ".//nxce:departurePoint";
NodeList childNodesX17 = (NodeList) xPath.compile(expressionChildX17).evaluate(eElement, XPathConstants.NODESET);

// Check if any descendants are found
boolean hasFix7 = false;
// Iterate over child nodes
for (int j = 0; j < childNodesX17.getLength(); j++) {
    Node nNodeX17 = childNodesX17.item(j);
    if (nNodeX17.getNodeType() == Node.ELEMENT_NODE) {
        Element track = (Element) nNodeX17;

        // Check if descendants exist for the child node
        NodeList descendants6 = track.getElementsByTagName("nxce:fix");
        if (descendants6.getLength() > 0) {
            hasFix7 = true;
            row = row + "," + "FIX_Dep";
            System.out.println(row);

            // Iterate over descendants
            for (int k = 0; k < descendants6.getLength(); k++) {
                Node descendantNode = descendants6.item(k);
                if (descendantNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element descendantElement = (Element) descendantNode;

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
                }
            }
        }
    }
}


//**************fix!!!! arrival

// Process child node "arrivalPoint'
String expressionChildX15 = ".//nxce:arrivalPoint";
NodeList childNodesX15 = (NodeList) xPath.compile(expressionChildX15).evaluate(eElement, XPathConstants.NODESET);

// Check if any descendants are found
boolean hasFix5 = false;
// Iterate over child nodes
for (int j = 0; j < childNodesX15.getLength(); j++) {
    Node nNodeX15 = childNodesX15.item(j);
    if (nNodeX15.getNodeType() == Node.ELEMENT_NODE) {
        Element track = (Element) nNodeX15;

        // Check if descendants exist for the child node
        NodeList descendants7 = track.getElementsByTagName("nxce:fix");
        if (descendants7.getLength() > 0) {
            hasFix5 = true;
            row = row + "," + "FIX_Arr";
            System.out.println(row);


            // Iterate over descendants
            for (int k = 0; k < descendants7.getLength(); k++) {
                Node descendantNode = descendants7.item(k);
                if (descendantNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element descendantElement = (Element) descendantNode;

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
                }
            }
        }
    }
}




//************************************************End of func************************************************************************
 
