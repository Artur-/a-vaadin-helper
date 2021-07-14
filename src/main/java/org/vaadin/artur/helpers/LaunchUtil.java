package org.vaadin.artur.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.vaadin.flow.server.VaadinContext;
import com.vaadin.flow.server.VaadinServletContext;
import com.vaadin.flow.server.frontend.FrontendTools;
import com.vaadin.flow.server.frontend.FrontendUtils;
import com.vaadin.flow.server.startup.ApplicationConfiguration;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class LaunchUtil {
    private static final String LAUNCH_TRACKER = "LaunchUtil.hasLaunched";

    private static boolean isProductionMode(ConfigurableApplicationContext app) {
        if (!(app instanceof GenericWebApplicationContext)) {
            getLogger().warn("Unable to determine production mode for an Spring Boot application context of type "
                    + app.getClass().getName());
            return false;
        }

        try {
            // This is V19+
            ServletContext servletContext = ((GenericWebApplicationContext) app).getServletContext();
            VaadinContext context = new VaadinServletContext(servletContext);
            ApplicationConfiguration applicationConfiguration = ApplicationConfiguration.get(context);
            return applicationConfiguration.isProductionMode();
        } catch (Throwable e) {
        }

        try {
            // This is V14-V18
            return com.vaadin.flow.server.DevModeHandler.getDevModeHandler() == null;
        } catch (Throwable e) {
        }

        // Assume production when unclear so no browser is popped up for production
        return true;
    }

    public static void launchBrowser(String location, String alternativeText) {
        try {
            File bundleTempFile = File.createTempFile("a-vaadin-helper-bundle", "js");
            bundleTempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(bundleTempFile)) {
                IOUtils.copyLarge(
                        LaunchUtil.class.getResourceAsStream("/META-INF/resources/frontend/a-vaadin-helper-bundle.js"),
                        out);
                String cmd = String.format("const open = require('%s').open;open('" + location + "');",
                        bundleTempFile.getAbsolutePath().replace("\\", "/"));
                runNodeScript(cmd);
            } catch (Throwable e) {
                getLogger().info(alternativeText);
            }
        } catch (Exception e) {
            getLogger().error("Unable to create temp file for bundle", e);
        }
    }

    private static Logger getLogger() {
        return LoggerFactory.getLogger(LaunchUtil.class);
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

        if (!LaunchUtil.isProductionMode(app)) {
            String location = "http://localhost:" + app.getEnvironment().getProperty("local.server.port") + "/";
            LaunchUtil.launchBrowser(location, "Application started at " + location);
            setLaunched();
        }

    }

}
