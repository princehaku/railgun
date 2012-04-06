/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.techest.railgun.test;

import java.security.NoSuchAlgorithmException;
import junit.framework.TestCase;
import net.techest.util.SHA;

/**
 *
 * @author baizhongwei.pt
 */
public class ShaTest extends TestCase {

    public ShaTest(String testName) {
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

    public void testHello() throws NoSuchAlgorithmException {
        String myinfo = "啊啊啊a1";
        
        System.out.println("本信息摘要是:" + SHA.getSHA1(myinfo.getBytes()));
    }
}
