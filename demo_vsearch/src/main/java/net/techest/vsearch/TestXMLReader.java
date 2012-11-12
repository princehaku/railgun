/**
 * Copyright 2012 Etao Inc.
 *
 * Project Name : net.techest_vsearch_jar_1.0-SNAPSHOT Created on : Nov 12,
 * 2012, 10:29:48 AM Author : haku
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.techest.vsearch;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.techest.railgun.RailGun;
import net.techest.railgun.system.Shell;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author haku
 */
public class TestXMLReader {

    public static void main(String[] args) throws DocumentException {
        InputStream in = ClassLoader.getSystemResourceAsStream("net/techest/vsearch/sites/oabt.org.xml");
        InputStreamReader ir= new InputStreamReader(in);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(ir);
        Element root = document.getRootElement();
       System.out.println(root.getData().toString());
    }
}
