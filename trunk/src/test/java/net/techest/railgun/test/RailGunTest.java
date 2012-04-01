/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.techest.railgun.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import net.techest.railgun.RailGun;
import org.dom4j.DocumentException;
/**
 *
 * @author baizhongwei.pt
 */
public class RailGunTest extends TestCase {
    
    public RailGunTest(String testName) {
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
     * Test of fire method, of class RailGun.
     */
    public void testFire() {
        try {
            System.out.println("loadShellXml");
            String xmlpath = "src/main/java/testtask1.xml";
            RailGun instance = new RailGun();
            instance.loadShellXml(xmlpath);
            instance.fire();
        } catch (DocumentException ex) {
            ex.printStackTrace();
            fail(""+ex.getMessage());
        }
    }
}
