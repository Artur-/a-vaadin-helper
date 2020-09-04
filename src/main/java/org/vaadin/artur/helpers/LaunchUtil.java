package org.vaadin.artur.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.server.DevModeHandler;
import com.vaadin.flow.server.frontend.FrontendTools;
import com.vaadin.flow.server.frontend.FrontendUtils;

import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

@NpmPackage(value = "open", version = "^7.2.1")
public class LaunchUtil {
    private static final String LAUNCH_TRACKER = "LaunchUtil.hasLaunched";

    private static boolean isProductionMode() {
        return DevModeHandler.getDevModeHandler() == null;
    }

    public static void launchBrowser(String location, String alternativeText) {
        try {
            runNodeScript("const open = require('open');open('" + location + "');");
        } catch (Throwable e) {
            LoggerFactory.getLogger(LaunchUtil.class).info(alternativeText);
        }
    }

    private static int runNodeScript(String script) throws InterruptedException, IOException {
        FrontendTools tools = new FrontendTools("", () -> FrontendUtils.getVaadinHomeDirectory().getAbsolutePath());
        String node = tools.getNodeExecutable();
        List<String> command = new ArrayList<>();
        command.add(node);
        command.add("-e");
        command.add(script);
        ProcessBuilder builder = FrontendUtils.createProcessBuilder(command);
        return builder.start().waitFor();
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
