package org.vaadin.artur.helpers;

import org.junit.Test;

public class LaunchUtilTest {

    @Test
    public void openUrl() {
        LaunchUtil.launchBrowser("https://vaadin.com", "Foo bar");
    }
}
