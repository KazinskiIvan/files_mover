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

        try {
	        FileMover mover = new FileMover(sourceFolderPath, targetFolderPath, new PackageXMLParser(packageXMLPath));
	        mover.invoke();
        } catch (Exception e) {
        	System.out.println(e.getMessage());
    	}
    }
}
