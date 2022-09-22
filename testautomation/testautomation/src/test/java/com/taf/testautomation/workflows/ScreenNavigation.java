package com.taf.testautomation.workflows;

import com.taf.testautomation.Session;
import com.taf.testautomation.screens.AboutAppScreen;
import com.taf.testautomation.screens.AccountCreationScreen;
import com.taf.testautomation.screens.MobileBaseActionScreen;

public class ScreenNavigation extends MobileBaseActionScreen {

    public ScreenNavigation(Session session) {
        super(session);
    }

    /**
     * Navigate to AboutAppScreen after creating account
     *
     * @param username
     * @param password
     * @return {@link AboutAppScreen}
     */
    public AboutAppScreen getAboutAppScreenFromAccountCreationScreen(String username, String password) {
        AccountCreationScreen accountCreationScreen = new AccountCreationScreen(getSession());
        if (getSession().getCustomProperties().get("isAndroid").equals("true")) {
            return accountCreationScreen.createAccount().signInToApp(username, password);
        } else {
            return accountCreationScreen
                    .createAccount()
                    .signInToApp();
        }
    }
}
