package com.diana.parser;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CLIParser {
    public static Options getOptions() {
        Options options = new Options();
        Option option;
        
        option = new Option("s", "source", true, "full path to source (e.g. git) folder with metadata files");
        option.setRequired(true);
        options.addOption(option);

        option = new Option("t", "target", true, "full path to target folder you want to copy files into");
        option.setRequired(true);
        options.addOption(option);

        option = new Option("p", "package", true, "full path to package.xml");
        option.setRequired(true);
        options.addOption(option);

        return options;
    }
}
