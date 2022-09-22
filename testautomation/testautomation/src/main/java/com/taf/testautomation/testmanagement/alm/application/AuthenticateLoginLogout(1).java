package com.taf.testautomation.testmanagement.alm.application;

import com.taf.testautomation.testmanagement.alm.infrastructure.Base64Encoder;
import com.taf.testautomation.testmanagement.alm.infrastructure.Response;
import com.taf.testautomation.testmanagement.alm.infrastructure.RestConnector;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * This example shows how to login/logout/authenticate to the server with REST.
 */
public class AuthenticateLoginLogout {

    private RestConnector con;

    /**
     * <p>
     * Once you initialized the class RestConnector, you can use this
     * constructor to create a new object from AlmConnector since the referenced
     * class RestConnector is keeping the connection details.
     * </p>
     */
    public AuthenticateLoginLogout() {
        this.con = RestConnector.getInstance();
    }

    /**
     * For logging in into an ALM project. If a user is already
     * authenticated, True will be returned. If user is already authenticated with a different
     * credential, first logout before login
     *
     * @param username
     * @param password
     * @return true if user is successfully authenticated else false.
     * @throws Exception
     */
    public boolean login(String username, String password) throws Exception {
        /**
         * Get the current authentication status.
         */
        String authenticationPoint = this.isAuthenticated();
        if (authenticationPoint != null) {
            return this.login(authenticationPoint, username, password);
        }

        return true;
    }

    /**
     * Standard HTTP login (basic authentication), where
     * one must store the returned cookies for further use.
     *
     * @param loginUrl
     * @param username
     * @param password
     * @return true if login is successful, else false.
     * @throws Exception
     */
    private boolean login(String loginUrl, String username, String password)
            throws Exception {
        byte[] credBytes = (username + ":" + password).getBytes();
        String credEncodedString = "Basic " + Base64Encoder.encode(credBytes);

        Map<String, String> map = new HashMap<String, String>();
        map.put("Authorization", credEncodedString);

        Response response = con.httpGet(loginUrl, null, map);
        System.out.println("Login GET response is " + response.getStatusCode());

        boolean ret = response.getStatusCode() == HttpURLConnection.HTTP_OK;

        return ret;
    }

    /**
     * Closes a session and cleans session cookies on a client.
     *
     * @return true if logout successful.
     * @throws Exception
     */
    public boolean logout() throws Exception {
        Response response = con.httpGet(
                con.buildUrl("authentication-point/logout"), null, null);
        return (response.getStatusCode() == HttpURLConnection.HTTP_OK);
    }

    /**
     * @return null if user already authenticated.
     *         else an URL to authenticate against.
     * @throws Exception
     *
     */
    public String isAuthenticated() throws Exception {
        String isAuthenticateUrl = con.buildUrl("rest/is-authenticated");
        String ret;

        Response response = con.httpGet(isAuthenticateUrl, null, null);
        int responseCode = response.getStatusCode();

        /**
         * If already authenticated
         */
        if (responseCode == HttpURLConnection.HTTP_OK) {
            ret = null;
        }
        /**
         * If not authenticated yet, return an URL to
         * authenticate via www-authenticate.
         */
        else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            ret = con.buildUrl("authentication-point/authenticate");
        }
        /**
         * Throws an Exception if an error occurred during login
         */
        else {
            throw response.getFailure();
        }

        return ret;
    }
}
