package org.bozdogan;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;

import static org.junit.Assert.*;

import org.junit.Test;

public class CLIArgsTest{
    @Test
    public void test_case1_vebose(){
        final JewelParams args0 = parseArgs("-d", "5", "-s", "2", "-v");
        assertArrayEquals(
                new Object[]{5, 2, true},
                new Object[]{
                        args0.getNumDepots(),
                        args0.getNumSalesmen(),
                        args0.isVerbose()}
        );
    }

    @Test
    public void test_case2_nonvebose(){
        final JewelParams args0 = parseArgs("-d", "2", "-s", "5");
        assertArrayEquals(
                new Object[]{2, 5, false},
                new Object[]{
                        args0.getNumDepots(),
                        args0.getNumSalesmen(),
                        args0.isVerbose()
                }
        );
    }

    @Test(expected = ArgumentValidationException.class)
    public void test_case3_monkeytapping(){
        final JewelParams args0 = parseArgs("-d", "asdf", "-s", "sd32", "-vverere");
        System.out.println(args0.getNumDepots()+", "+
                args0.getNumSalesmen()+", "+
                args0.isVerbose());
    }

    private static JewelParams parseArgs(String... argv){
        return CliFactory.parseArguments(JewelParams.class, argv);
    }
}
