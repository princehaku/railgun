/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.techest.railgun;

import junit.framework.TestCase;

/**
 *
 * @author baizhongwei.pt
 */
public class MainTest extends TestCase {
    
    public MainTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of main method, of class Main.
     */
    public void testMain() throws Exception {
        if (System.getProperty("java.home") == null) {
            System.out.println("Need JAVA HOME");
            fail("Need JAVA HOME SETTED!!!");
        }
    }
}
