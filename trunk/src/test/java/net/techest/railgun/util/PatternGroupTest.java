/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.techest.railgun.util;

import java.util.ArrayList;
import java.util.HashMap;
import junit.framework.TestCase;

/**
 *
 * @author baizhongwei.pt
 */
public class PatternGroupTest extends TestCase {
    
    public PatternGroupTest(String testName) {
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
     * Test of addNewString method, of class PatternGroup.
     */
    public void testAddNewString() {
        System.out.println("addNewString");
        String keyName = "";
        String input = "";
        boolean convert = false;
        PatternGroup pg = new PatternGroup();
        pg.addNewString("url", "uu$[1,2]", true);
        pg.addNewString("cookie", "aa$[3,4]", true);
        pg.addNewString("set", "ss$[5,6]", true);
        ArrayList<HashMap<String, String>> ss = pg.convert();
        for (int i = 0, size = ss.size(); i < size; i++) {
            HashMap<String, String> hash = ss.get(i);
            System.out.println("================");
            System.out.println(hash.get("url"));
            System.out.println(hash.get("cookie"));
            System.out.println(hash.get("set"));
            System.out.println("================");
        }
    }
}
