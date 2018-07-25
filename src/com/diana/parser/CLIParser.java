package com.diana.parser;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CLIParser {
    public static Options getOptions() {
        Options options = new Options();
        Option option;
        
        option = new Option("p", "package", true, "input path to package.xml");
        option.setRequired(true);
        options.addOption(option);

        option = new Option("s", "source", true, "input path to source folder with files");
        option.setRequired(true);
        options.addOption(option);

        option = new Option("t", "target", true, "input path to target folder to copy files");
        option.setRequired(true);
        options.addOption(option);
        
        option = new Option("l", "labels", true, "input path to labels\\CustomLabels.labels file");
        option.setRequired(true);
        options.addOption(option);

        return options;
    }
}
