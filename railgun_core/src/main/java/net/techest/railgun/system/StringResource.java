/**
 * Copyright 2012 Etao Inc.
 *
 * Project Name : net.techest_railgun_jar_0.3 Created on : Nov 13, 2012, 4:25:53
 * PM Author : haku
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.techest.railgun.system;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.techest.railgun.util.Log4j;

/**
 * 资源装饰器
 */
public class StringResource {

    private Resource res;

    public StringResource(Resource res) {
        this.res = res;
    }

    public String getText() {
        if (this.res.getParam("bytedata") == null) {
            return "";
        }
        byte[] data = (byte[]) this.res.getParam("bytedata");
        try {
            return new String(data, this.getCharset());
        } catch (UnsupportedEncodingException ex) {
            Log4j.getInstance().error(ex.getMessage());
            return "";
        }
    }

    public String getCharset() {
        if (this.res.getParam("charset") == null) {
            return "utf8";
        }
        return (String) this.res.getParam("charset");
    }

    public void setBytes(byte[] bytes) {
        this.putParam("bytedata", bytes);
    }

    public void putParam(String key, Object val) {
        this.res.putParam(key, val);
    }

    public void getParam(String key) {
        this.res.getParam(key);
    }
}
