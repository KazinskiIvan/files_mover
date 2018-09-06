package com.diana.parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

interface XMLParser {
    ArrayList<String> getFoldersMapping() throws SAXException, IOException, ParserConfigurationException;
    File getXMLFIle();
}
