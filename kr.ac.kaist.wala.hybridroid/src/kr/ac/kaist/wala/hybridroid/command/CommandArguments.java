/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and KAIST.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * KAIST - initial API and implementation
 *******************************************************************************/
package kr.ac.kaist.wala.hybridroid.command;

import org.apache.commons.cli.*;

public class CommandArguments {
  public static final String PROP_ARG = "p";
  public static final String PROP_LONG_ARG = "property";
  public static final String MODEL_ARG = "m";
  public static final String MODEL_LONG_ARG = "model";
  public static final String TARGET_ARG = "t";
  public static final String TARGET_LONG_ARG = "target";
  public static final String DROIDEL_ARG = "droidel";
  public static final String MANIFEST_ARG = "manifest";
  public static final String CFG_ARG = "cfg";
  public static final String CFG_LONG_ARG = "cfg";
  public static final String PRE_STRING_ARG = "prestr";
  public static final String ONLY_JS_ARG = "j";
  public static final String ONLY_JS_LONG_ARG = "jsonly";
  private static Options options;

  static {
    options = new Options();

    Option targetOp =
        Option.builder(TARGET_ARG)
            .longOpt(TARGET_LONG_ARG)
            .hasArg(true)
            .desc("target apk file of analysis")
            .build();
    //		targetOp.setRequired(true);
    options.addOption(targetOp);

    Option propOp =
        Option.builder(PROP_ARG)
            .longOpt(PROP_LONG_ARG)
            .hasArg(true)
            .desc("set the wala property file")
            .build();
    //		propOp.setRequired(true);
    options.addOption(propOp);

    // Option modelOp =
    // OptionBuilder.withArgName(MODEL_ARG).withLongOpt(MODEL_LONG_ARG).withDescription("set the
    // wala property file").create();

    // //     new Option(MODEL_ARG, MODEL_LONG_ARG, true, "set the wala property file");
    // options.addOption(modelOp);

    // Option jsOp =
    // OptionBuilder.withArgName(ONLY_JS_ARG).withLongOpt(ONLY_JS_LONG_ARG).withDescription("set the
    // wala property file").create();

    // //    new Option(ONLY_JS_ARG, ONLY_JS_LONG_ARG, false, "set the wala property file");
    // options.addOption(jsOp);

    //		OptionGroup functions = new OptionGroup();
    Option cfgOp =
        Option.builder(CFG_ARG)
            .longOpt(CFG_LONG_ARG)
            .hasArg(false)
            .desc("construct cfg for the android application")
            .build();

    options.addOption(cfgOp);
    //		options.addOptionGroup(functions);
    ////		options.addOption(new Option(PRE_STRING_ARG, false, "pre-analysis for "), hasArg,
    // description)
    //		options.addOption(new Option(DROIDEL_ARG, false, "enable pre transforming using DROIDEL"));
    //		options.addOption(new Option(MANIFEST_ARG, false, "enable the manifest analysis"));
  }

  private CommandLine cmd;

  public CommandArguments(String[] args) {
    CommandLineParser parser = new DefaultParser();
    try {
      cmd = parser.parse(options, args, false);

      if (!computeDependency()) {
        usage();
        System.exit(-1);
      }
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      System.out.println("parsing error: " + e);
      usage();
      System.exit(-1);
    }
  }

  public void usage() {
    HelpFormatter help = new HelpFormatter();
    help.printHelp("command line options", options);
  }

  private boolean computeDependency() {
    if (!cmd.hasOption(CFG_ARG) || !cmd.hasOption(PROP_ARG) || !cmd.hasOption(TARGET_ARG)) {
      return false;
    } else return true;
  }

  public boolean has(String op) {
    return cmd.hasOption(op);
  }

  public String get(String op) {
    return cmd.getOptionValue(op);
  }
}
