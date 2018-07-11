package com.diana.parser;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.json.simple.JSONObject;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PackageXMLParser implements XMLParser {

    private File configFile;
    private final static String PATH_TO_CONFIG_JSON = "mapping.json";
    private HashMap<String, String> foldersMap;

    public PackageXMLParser(String pathToConfigFile) throws Exception {
        this.foldersMap = new HashMap<>();
        this.configFile = new File(pathToConfigFile);
        if (!this.configFile.exists()) {
            throw new Exception(String.format("File %s doesn't exist.", this.configFile.getAbsolutePath()));
        }
        this.loadFolderMapFromJSON();
    }

    public File getXMLFIle(){
        return configFile;
    }

    public ArrayList<String> getFoldersMapping() throws SAXException, IOException, ParserConfigurationException {
        ArrayList<String> filesList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(this.configFile);

        Element rootElement = doc.getDocumentElement();
        NodeList elems = rootElement.getElementsByTagName("types");

        for (int i = 0; i < elems.getLength(); i++) {
            Element elem = (Element) elems.item(i);

            NodeList nameNodes = elem.getElementsByTagName("name");
            if (nameNodes.getLength() == 0) {
                continue;
            }

            String componentType = nameNodes.item(0).getTextContent().trim();
            String folderName = getFolderName(componentType);

            NodeList componentNodes = elem.getElementsByTagName("members");

            if (componentNodes.getLength() == 0) {
                continue;
            }

            for (int j = 0; j < componentNodes.getLength(); j++) {
                filesList.add(String.format("%s/%s", folderName, componentNodes.item(j).getTextContent().trim()));
            }
        }
        return filesList;
    }

    private String getFolderName(String componentName) {
        if (this.foldersMap.containsKey(componentName)) {
            return this.foldersMap.get(componentName);
        }
        return componentName;
    }

    private void loadFolderMapFromJSON() throws Exception {

        File jsonFile = new File(this.PATH_TO_CONFIG_JSON);
        String jsonSTR = "";

        if (jsonFile.exists()) {
            InputStream reader = new FileInputStream(this.PATH_TO_CONFIG_JSON);
            jsonSTR = new String(reader.readAllBytes());
        } else {
            InputStream inpStr = PackageXMLParser.class.getResourceAsStream("/" + this.PATH_TO_CONFIG_JSON);
            jsonSTR = new String(inpStr.readAllBytes());
        }

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(jsonSTR);
        JSONArray arr = (JSONArray) obj.get("rules");
        if (arr == null) {
            throw new Exception("Invalid  mapping.json file.");
        }

        Iterator iter = arr.iterator();
        while (iter.hasNext()) {
            JSONObject sfMap = (JSONObject) iter.next();
            String folder = (String) sfMap.get("folder");
            String component = (String) sfMap.get("component");
            if (folder.trim() != "") {
                this.foldersMap.put(component, folder);
            }
        }
    }

}
