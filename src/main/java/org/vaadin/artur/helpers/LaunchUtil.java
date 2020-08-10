package org.vaadin.artur.helpers;

import java.awt.Desktop;
import java.net.URL;

import com.vaadin.flow.server.DevModeHandler;

import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class LaunchUtil {
    private static final String LAUNCH_TRACKER = "LaunchUtil.hasLaunched";
    private static final String HEADLESS_PROPERTY = "java.awt.headless";

    private static boolean isProductionMode() {
        return DevModeHandler.getDevModeHandler() == null;
    }

    public static void launchBrowser(String location, String alternativeText) {

        String oldProperty = System.getProperty(HEADLESS_PROPERTY);
        System.setProperty(HEADLESS_PROPERTY, "false");

        try {
            URL url = new URL(location);
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(url.toURI());
        } catch (Exception e) {
            LoggerFactory.getLogger(LaunchUtil.class).info(alternativeText);
        } finally {
            if (oldProperty == null) {
                System.clearProperty(HEADLESS_PROPERTY);
            } else {
                System.setProperty(HEADLESS_PROPERTY, oldProperty);
            }

        }

    }

    private static boolean isLaunched() {
        return "yes".equals(System.getProperty(LAUNCH_TRACKER));
    }

    private static void setLaunched() {
        System.setProperty(LAUNCH_TRACKER, "yes");
    }

    public static void launchBrowserInDevelopmentMode(ConfigurableApplicationContext app) {
        if (isLaunched()) {
            // Only launch browser on startup, not on reload
            return;
        }

        if (!LaunchUtil.isProductionMode()) {
            String location = "http://localhost:" + app.getEnvironment().getProperty("local.server.port") + "/";
            LaunchUtil.launchBrowser(location, "Application started at " + location);
            setLaunched();
        }

    }

}