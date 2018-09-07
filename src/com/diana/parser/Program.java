package com.diana.parser;

import java.io.File;

import org.apache.commons.cli.*;

public class Program {
    public static void main(String[] args) throws Exception {
        Options options = CLIParser.getOptions();
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
            return;
        }

        String sourceFolderPath = cmd.getOptionValue("source");
        String targetFolderPath = cmd.getOptionValue("target");
        String packageXMLPath = cmd.getOptionValue("package");
        if (packageXMLPath == null) {
        	packageXMLPath = new File(sourceFolderPath).getPath() + "\\" + "package.xml";
        }
        
    	System.out.println(
			"\n" + "Notes:" +
			 "\n" + "1. '*' is not supported yet. Please explicitly list all necessary files in package.xml" + 
			 "\n" + "2. Be aware of adding 'Translations' to package.xml. They are not correct in GIT now. Please deploy translations separately."	
		);        
        
        try {
	        FileMover mover = new FileMover(sourceFolderPath, targetFolderPath, new PackageXMLParser(packageXMLPath));
	        mover.invoke();
        } catch (Exception e) {
        	System.out.println(e.getMessage());
    	}
    }
}
