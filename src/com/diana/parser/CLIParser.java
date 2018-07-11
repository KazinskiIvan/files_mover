package com.diana.parser;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CLIParser {

    public static Options getOptions() {

        Options options = new Options();

        Option packageOpt = new Option("p", "package", true, "input path to package.xml");
        packageOpt.setRequired(true);
        options.addOption(packageOpt);

        Option sourceOpt = new Option("s", "source", true, "input path to source folder with files");
        sourceOpt.setRequired(true);
        options.addOption(sourceOpt);

        Option targetOpt = new Option("t", "target", true, "input path to target folder to copy files");
        targetOpt.setRequired(true);
        options.addOption(targetOpt);

        return options;
    }
}
