package net.techest.vsearch;

import net.techest.railgun.thread.RailGunThreadPool;
import net.techest.railgun.util.Configure;
import net.techest.railgun.util.Log4j;
import net.techest.vsearch.rest.APIServer;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {

        if (Configure.getSystemConfig().getString("REST_ENABLE", "true").equals("true")) {
            Log4j.getInstance().info("API 尝试启动");
            APIServer.getInstance().start();
        } else {
            Log4j.getInstance().info("API 不启动" + Configure.getSystemConfig().getString("REST_ENABLE", "true"));
        }
        Log4j.getInstance().info("RailGun开始运行...");
        RailGunThreadPool.getInstance().start();
    }
}
