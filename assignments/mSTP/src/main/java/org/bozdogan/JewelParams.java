package org.bozdogan;

import com.lexicalscope.jewel.cli.Option;

/** Parameter info for command line argument parser. */
public interface JewelParams{
    @Option(shortName = "d", longName = "depots",
            description = "number of depots", defaultValue = "5")
    int getNumDepots();

    @Option(shortName = "s", longName = {"salesmen", "vehicles"},
            description = "number of salesmen per depot", defaultValue = "2")
    int getNumSalesmen();

    @Option(shortName = "v", longName = "verbose",
            description = "use city names when displaying/printing")
    boolean isVerbose();

    @Option(helpRequest = true, shortName = {"h", "?"}, longName = "help",
            description = "display help")
    boolean isHelp();
}
