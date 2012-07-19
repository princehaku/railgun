/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.techest.railgun.system;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import junit.framework.TestCase;
import net.techest.railgun.test.TestHandler;
import net.techest.railgun.util.Log4j;

/**
 *
 * @author baizhongwei.pt
 */
public class ResourceTest extends TestCase {

    public ResourceTest(String testName) {
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

    public void testParamHashSet() throws Exception {
        LinkedList<Resource> resources = new LinkedList<Resource>();
        int iw = 0;
        while (iw++ < 10) {
            Resource r = new Resource();
            Resource clone = (Resource) r.clone();
            resources.add(clone);
        }
        Shell s = new Shell();
        s.setResources(resources);

        for (Iterator i = s.getResources().iterator(); i.hasNext();) {
            Resource res = (Resource) i.next();
            new TestHandler().process(res);
        }
        for (Iterator i = s.getResources().iterator(); i.hasNext();) {
            Resource res = (Resource) i.next();
            Log4j.getInstance().error("ID + " + res.getParam("id"));
        }


    }
}
