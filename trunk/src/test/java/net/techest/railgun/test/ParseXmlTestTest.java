/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.techest.railgun.test;

import junit.framework.TestCase;
import net.techest.railgun.system.Shell;
import org.dom4j.Element;

/**
 *
 * @author baizhongwei.pt
 */
public class ParseXmlTestTest extends TestCase {
    
    public ParseXmlTestTest(String testName) {
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
     * Test of main method, of class ParseXmlTest.
     */
    public void testMain() throws Exception {
        System.out.println("main");
        String[] argvs = null;
        ParseXmlTest.main(argvs);
    }
}
