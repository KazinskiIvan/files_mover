package com.diana.parser;

// JSON
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

// XML
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//IO
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

// Collections
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PackageXMLParser implements XMLParser {
    private File configFile;
    private final static String PATH_TO_CONFIG_JSON = "mapping.json";
    private HashMap<String, String> foldersMap;
    private HashMap<String, MetadataType> metaDataTypes;
    

    public PackageXMLParser(String pathToConfigFile) throws Exception {
        this.foldersMap = new HashMap<>();
        this.metaDataTypes = new HashMap<>();
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
    
    public void xxx (){
    	
    }
    
    /**
     * for "ApexClass" returns "classes"
     * @param componentName
     * @return
     */

    private String getFolderName(String componentName) {
        if (this.foldersMap.containsKey(componentName)) {
        	System.out.println(componentName + " " + this.foldersMap.get(componentName));
            return this.foldersMap.get(componentName);
        }
        return componentName;
    }

    private void loadFolderMapFromJSON() throws Exception {
        File jsonFile = new File(PackageXMLParser.PATH_TO_CONFIG_JSON);
        String jsonSTR = "";

        InputStream reader= null;
        try {
	        if (jsonFile.exists()) {
	            reader = new FileInputStream(PackageXMLParser.PATH_TO_CONFIG_JSON);
	        } else {
	            reader = PackageXMLParser.class.getResourceAsStream("/" + PackageXMLParser.PATH_TO_CONFIG_JSON);
	        }
        
	        ArrayList<Byte> bytes = new ArrayList<>();
	        boolean eof = false;
	        while (!eof) {
	        	int intVal = reader.read();
	        	if (intVal > -1) {
	        		bytes.add((byte)intVal);
	        	} else {
	        		eof = true;
	        	}
	        }
	        
	        byte [] byteArray = new byte[bytes.size()];
	        for (int i = 0; i < bytes.size(); i++){
	        	byteArray[i] = bytes.get(i);
	        }
	        
	        jsonSTR = new String (byteArray);
        } catch (IOException e){
        	System.out.println(e.getMessage());
        } finally {
        	reader.close();
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
                MetadataType metadataType = new MetadataType();
                metadataType.sXMLName = component;
                metadataType.sDirName = folder;
                this.metaDataTypes.put(component, metadataType);
            }
        }
    }

}
