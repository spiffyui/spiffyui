/*******************************************************************************
 * 
 * Copyright 2011 Spiffy UI Team   
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package org.spiffyui.client.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spiffyui.client.JSONUtil;
import org.spiffyui.client.JSUtil;
import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.i18n.SpiffyUIStrings;
import org.spiffyui.client.rest.v2.RESTOAuthProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;

/**
 * A set of utilities for calling REST from GWT.
 */
public final class RESTility
{
    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);
    private static final String LOCALE_COOKIE = "Spiffy_Locale";

    private static final RESTility RESTILITY = new RESTility();

    /**
     * This method represents an HTTP GET request
     */
    public static final HTTPMethod GET = RESTILITY.new HTTPMethod("GET");

    /**
     * This method represents an HTTP PUT request
     */
    public static final HTTPMethod PUT = RESTILITY.new HTTPMethod("PUT");

    /**
     * This method represents an HTTP POST request
     */
    public static final HTTPMethod POST = RESTILITY.new HTTPMethod("POST");

    /**
     * This method represents an HTTP DELETE request
     */
    public static final HTTPMethod DELETE = RESTILITY.new HTTPMethod("DELETE");

    private static boolean g_inLoginProcess = false;

    private static List<RESTLoginCallBack> g_loginListeners = new ArrayList<RESTLoginCallBack>();

    private int m_callCount = 0;

    private boolean m_hasLoggedIn = false;
    
    private boolean m_logInListenerCalled = false;
    
    private boolean m_secureCookies = false;
    
    private String m_sessionCookie = "Spiffy_Session";

    private static RESTAuthProvider g_authProvider;

    private static RESTOAuthProvider g_oAuthProvider;
    
    private String m_sessionCookiePath;

    /**
     * This is a helper type class so we can pass the HTTP method as a type safe
     * object instead of a string.
     */
    public final class HTTPMethod
    {
        private String m_method;

        /**
         * Create a new HTTPMethod object
         *
         * @param method the method
         */
        private HTTPMethod(String method)
        {
            m_method = method;
        }

        /**
         * Get the method string for this object
         *
         * @return the method string
         */
        private String getMethod()
        {
            return m_method;
        }
    }

    private Map<RESTCallback, RESTCallStruct> m_restCalls = new HashMap<RESTCallback, RESTCallStruct>();
    private String m_userToken = null;
    private String m_tokenType = null;
    private String m_tokenServerUrl = null;
    private String m_username = null;
    private String m_tokenServerLogoutUrl = null;
    private String m_bestLocale = null;
    
    /**
     * Just to make sure that nobody else can instatiate this object.
     */
    private RESTility()
    {
    }

    private static final String URI_KEY = "uri";
    private static final String SIGNOFF_URI_KEY = "signoffuri";

    static {
        RESTAuthProvider authUtil = new AuthUtil();
        RESTility.setAuthProvider(authUtil);
    }

    /**
     * <p>
     * Sets the authentication provider used for future REST requests.
     * </p>
     * 
     * <p>
     * By default authentication is provided by the AuthUtil class, but this
     * class may be replaced to provide support for custom authentication schemes.
     * </p>
     * 
     * @param authProvider
     *               the new authentication provider
     * 
     * @see AuthUtil
     */
    public static void setAuthProvider(RESTAuthProvider authProvider)
    {
        g_authProvider = authProvider;
    }

    /**
     * Set the OAuth provider for this application.
     * 
     * @param oAuthProvider
     *               the oAuth provider
     */
    public static void setOAuthProvider(RESTOAuthProvider oAuthProvider)
    {
        g_oAuthProvider = oAuthProvider;
    }
    
    /**
     * <p>
     * Sets the name of the Spiffy UI session cookie.
     * </p>
     * 
     * <p>
     * Spiffy UI uses a local cookie to save the current user token.  This cookie has 
     * the name <code>Spiffy_Session</code> by default.  This method will change the 
     * name of the cookie to something else.  
     * </p>
     * 
     * <p>
     * Calling this method will not change the name of the cookie until a REST call is made
     * which causes the cookie to be reset.
     * </p>
     * 
     * @param name   the name of the cookie Spiffy UI session cookie
     */
    public static void setSessionCookieName(String name)
    {
        if (name == null) {
            throw new IllegalArgumentException("The session cookie name must not be null");
        }
        
        RESTILITY.m_sessionCookie = name;
    }
    
    /**
     * Gets the current name of the Spiffy UI session cookie.
     * 
     * @return the name of the session cookie
     */
    public static String getSessionCookieName()
    {
        return RESTILITY.m_sessionCookie;
    }
    
    /**
     * <p>
     * Sets if RESTility cookies should only be sent via SSL.
     * </p>
     * 
     * <p>
     * RESTility stores a small amount of information locally so users can refresh the page
     * without logging out or resetting their locale.  This information is sometimes stored
     * in cookies.  These cookies are normally not sent back to the server, but can be in 
     * some cases.  
     * </p>
     * 
     * <p>
     * If your application is concerned with sercurity you might want to require all cookies 
     * are only sent via a secure connection (SSL).  Set this method true to make all cookies
     * stored be RESTility and Spiffy UI require an SSL connection before they are sent.
     * </p>
     * 
     * <p>
     * The default value of this field is false.
     * </p>
     * 
     * @param secure true if cookies should require SSL and false otherwise
     */
    public static void setRequireSecureCookies(boolean secure)
    {
        RESTILITY.m_secureCookies = secure;
    }
    
    /**
     * Determines if RESTility will force all cookies to require SSL.
     * 
     * @return true if cookies require SSL and false otherwise
     */
    public static boolean requiresSecureCookies()
    {
        return RESTILITY.m_secureCookies;
    }

    /**
     * Gets the current auth provider which will be used for future REST calls.
     * 
     * @return The current auth provider
     */
    public static RESTAuthProvider getAuthProvider()
    {
        return g_authProvider;
    }

    /**
     * Make a login request using RESTility authentication framework.
     * 
     * @param callback  the rest callback called when the login is complete
     * @param response  the response from the server requiring the login
     * @param url       the URL of the authentication server
     * @param errorCode the error code from the server returned with the 401
     * 
     * @exception RESTException
     *                   if there was an exception when making the login request
     */
    public static void login(RESTCallback callback, Response response, String url, String errorCode)
        throws RESTException
    {
        RESTILITY.doLogin(callback, response, url, errorCode, null);
    }
    
    /**
     * Make a login request using RESTility authentication framework.
     * 
     * @param callback  the rest callback called when the login is complete
     * @param response  the response from the server requiring the login
     * @param url       the URL of the authentication server
     * @param exception the RESTException which prompted this login
     * 
     * @exception RESTException
     *                   if there was an exception when making the login request
     */
    public static void login(RESTCallback callback, Response response, String url, RESTException exception)
        throws RESTException
    {
        RESTILITY.doLogin(callback, response, url, null, exception);
    }
    
    private static String trimQuotes(String header)
    {
        if (header == null) {
            return header;
        }
        
        String ret = header;
        
        if (ret.startsWith("\"")) {
            ret = ret.substring(1);
        }
        
        if (ret.endsWith("\"")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        
        return ret;
    }

    private void doLogin(RESTCallback callback, Response response, String url, String errorCode, RESTException exception)
        throws RESTException
    {
        JSUtil.println("doLogin(" + url + ", " + errorCode + ")");

        /*
         When the server returns a status code 401 they are required
         to send back the WWW-Authenticate header to tell us how to
         authenticate.
         */
        String auth = response.getHeader("WWW-Authenticate");
        if (auth == null) {
            throw new RESTException(RESTException.NO_AUTH_HEADER,
                                    "", STRINGS.noAuthHeader(),
                                    new HashMap<String, String>(),
                                    response.getStatusCode(),
                                    url);
        }

        /*
         * Now we have to parse out the token server URL and other information.
         *
         * The String should look like this:
         *
         * X-OPAQUE uri=<token server URI>,signoffUri=<token server logout url>
         *
         * First we'll remove the token type
         */
        
        String tokenType = auth;
        String loginUri = null;
        String logoutUri = null;

        JSUtil.println("auth: " + auth);

        if (tokenType.indexOf(' ') != -1) {
            tokenType = tokenType.substring(0, tokenType.indexOf(' ')).trim();
            JSUtil.println("tokenType: " + tokenType);
            auth = auth.substring(auth.indexOf(' ') + 1);
            if (auth.indexOf(',') != -1) {
                String props[] = auth.split(",");
                JSUtil.println("props: " + props);
    
                for (String prop : props) {
                    if (prop.trim().toLowerCase().startsWith(URI_KEY)) {
                        loginUri = prop.substring(prop.indexOf('=') + 1, prop.length()).trim();
                    } else if (prop.trim().toLowerCase().startsWith(SIGNOFF_URI_KEY)) {
                        logoutUri = prop.substring(prop.indexOf('=') + 1, prop.length()).trim();
                    }
                }
                
                loginUri = trimQuotes(loginUri);
                logoutUri = trimQuotes(logoutUri);
        
                if (logoutUri.trim().length() == 0) {
                    logoutUri = loginUri;
                }
            }
        }

        JSUtil.println("tokenType: " + tokenType);

        setTokenType(tokenType);
        setTokenServerURL(loginUri);
        setTokenServerLogoutURL(logoutUri);

        removeCookie(RESTILITY.m_sessionCookie);
        removeCookie(LOCALE_COOKIE);

        JSUtil.println("g_oAuthProvider: " + g_oAuthProvider);
        JSUtil.println("tokenType: " + tokenType);

        if (g_oAuthProvider != null && tokenType.equalsIgnoreCase("Bearer") || tokenType.equalsIgnoreCase("MAC")) {
            handleOAuthRequest(callback, loginUri, response, exception);
        } else if (g_authProvider instanceof org.spiffyui.client.rest.v2.RESTAuthProvider) {
            ((org.spiffyui.client.rest.v2.RESTAuthProvider) g_authProvider).showLogin(callback, loginUri, response, exception);
        } else {
            g_authProvider.showLogin(callback, loginUri, errorCode);
        }
    }

    private void handleNoPrivilege(RESTException exception) 
    {
        if (g_oAuthProvider != null && exception != null && AuthUtil.NO_PRIVILEGE.equals(exception.getCode())) {
            /*
             * This is a special OAuth state that Spiffy UI recognizes.  It means that
             * the user has supplied valid credentials, but they don't have access and
             * further calls will only result in a 401.  
             * 
             * There isn't much we can do in this case so we just pass the exception to 
             * the OAuth provider to handle it.
             */
            g_oAuthProvider.error(exception);
        }
    }

    private void handleOAuthRequest(RESTCallback callback, String tokenServerUrl, Response response, RESTException exception)
        throws RESTException
    {
        String authUrl = g_oAuthProvider.getAuthServerUrl(callback, tokenServerUrl, response, exception);
        JSUtil.println("authUrl: " + authUrl);

        if (exception != null && AuthUtil.NO_PRIVILEGE.equals(exception.getCode())) {
            /*
             * This is a special OAuth state that Spiffy UI recognizes.  It means that
             * the user has supplied valid credentials, but they don't have access and
             * further calls will only result in a 401.  
             * 
             * There isn't much we can do in this case so we just pass the exception to 
             * the OAuth provider to handle it.
             */
            g_oAuthProvider.error(exception);
        } else {
            handleOAuthRequestJS(this, authUrl, g_oAuthProvider.getClientId(), g_oAuthProvider.getScope());
        }
    }

    private void oAuthComplete(String token, String tokenType)
    {
        setTokenType(tokenType);
        setUserToken(token);
        finishRESTCalls();
    }

    private native String base64Encode(String s) /*-{
        return $wnd.Base64.encode(s);
    }-*/;

    private native void handleOAuthRequestJS(RESTility callback, String authUrl, String clientId, String scope) /*-{
        $wnd.spiffyui.oAuthAuthenticate(authUrl, clientId, scope, function(token, tokenType) {
            callback.@org.spiffyui.client.rest.RESTility::oAuthComplete(Ljava/lang/String;Ljava/lang/String;)(token,tokenType);
        });
    }-*/;

    /**
     * Returns HTTPMethod corresponding to method name.
     * If the passed in method does not match any, GET is returned.
     *
     * @param method a String representation of a http method
     * @return the HTTPMethod corresponding to the passed in String method representation.
     */
    public static HTTPMethod parseString(String method)
    {
        if (POST.getMethod().equalsIgnoreCase(method)) {
            return POST;
        } else if (PUT.getMethod().equalsIgnoreCase(method)) {
            return PUT;
        } else if (DELETE.getMethod().equalsIgnoreCase(method)) {
            return DELETE;
        } else {
            //Otherwise return GET
            return GET;
        }
    }

    /**
     * Upon logout, delete cookie and clear out all member variables
     */
    public static void doLocalLogout()
    {
        RESTILITY.m_hasLoggedIn = false;
        RESTILITY.m_logInListenerCalled = false;
        RESTILITY.m_callCount = 0;
        RESTILITY.m_userToken = null;
        RESTILITY.m_tokenType = null;
        RESTILITY.m_tokenServerUrl = null;
        RESTILITY.m_tokenServerLogoutUrl = null;
        RESTILITY.m_username = null;
        removeCookie(RESTILITY.m_sessionCookie);
        removeCookie(LOCALE_COOKIE);
    }

    /**
     * The normal GWT mechanism for removing cookies will remove a cookie at the path
     * the page is on.  The is a possibility that the session cookie was set on the
     * server with a slightly different path.  In that case we need to try to delete
     * the cookie on all the paths of the current URL.  This method handles that case.
     * 
     * @param name   the name of the cookie to remove
     */
    private static void removeCookie(String name)
    {
        Cookies.removeCookie(name);
        if (Cookies.getCookie(name) != null) {
            /*
             * This could mean that the cookie was there,
             * but was on a different path than the one that
             * we get by default.
             */
            removeCookie(name, Window.Location.getPath()); 
        }
    }

    private static void removeCookie(String name, String currentPath)
    {
        Cookies.removeCookie(name, currentPath);
        if (Cookies.getCookie(name) != null) {
            /*
             * This could mean that the cookie was there,
             * but was on a different path than the one that
             * we were passed.  In that case we'll bump up
             * the path and try again.
             */
            String path = currentPath;
            if (path.charAt(0) != '/') {
                path = "/" + path;
            }

            int slashloc = path.lastIndexOf('/');
            if (slashloc > 1) {
                path = path.substring(0, slashloc);
                removeCookie(name, path);
            }
        }
    }

    /**
     * In some cases, like login, the original REST call returns an error and we
     * need to run it again.  This call gets the same REST request information and
     * tries the request again.
     */
    public static void finishRESTCalls()
    {
        for (RESTCallback callback : RESTILITY.m_restCalls.keySet()) {
            RESTCallStruct struct = RESTILITY.m_restCalls.get(callback);
            if (struct != null && struct.shouldReplay()) {
                callREST(struct.getUrl(), struct.getData(), struct.getMethod(), callback);
            }
        }
    }

    /**
     * Make a rest call using an HTTP GET to the specified URL.
     *
     * @param callback the callback to invoke
     * @param url    the properly encoded REST url to call
     */
    public static void callREST(String url, RESTCallback callback)
    {
        callREST(url, "", RESTility.GET, callback);
    }

    /**
     * Make a rest call using an HTTP GET to the specified URL including
     * the specified data..
     *
     * @param url    the properly encoded REST url to call
     * @param data   the data to pass to the URL
     * @param callback the callback to invoke
     */
    public static void callREST(String url, String data, RESTCallback callback)
    {
        callREST(url, data, RESTility.GET, callback);
    }

    /**
     * Set the user token in JavaScript memory and and saves it in a cookie.
     * @param token  user token
     */
    public static void setUserToken(String token)
    {
        g_inLoginProcess = false;
        RESTILITY.m_userToken = token;
        setSessionToken();
    }

    /**
     * Set the authentication server url in JavaScript memory and and saves it in a cookie.
     * @param url  authentication server url
     */
    public static void setTokenServerURL(String url)
    {
        RESTILITY.m_tokenServerUrl = url;
        setSessionToken();
    }

    /**
     * Fire the login success event to all listeners if it hasn't been fired already.
     */
    public static void fireLoginSuccess()
    {
        RESTILITY.m_hasLoggedIn = true;
        if (!RESTILITY.m_logInListenerCalled) {
            for (RESTLoginCallBack listener : g_loginListeners) {
                listener.onLoginSuccess();
            }
            RESTILITY.m_logInListenerCalled = true;
        }
    }
    
    /**
     * <p>
     * Set the type of token RESTility will pass.
     * </p>
     * 
     * <p>
     * Most of the time the token type is specified by the REST server and the
     * client does not have to specify this value.  This method is mostly used
     * for testing.
     * </p>
     * 
     * @param type   the token type
     */
    public static void setTokenType(String type)
    {
        RESTILITY.m_tokenType = type;
        setSessionToken();
    }

    /**
     * Set the user name in JavaScript memory and and saves it in a cookie.
     * @param username  user name
     */
    public static void setUsername(String username)
    {
        RESTILITY.m_username = username;
        setSessionToken();
    }

    /**
     * Set the authentication server logout url in JavaScript memory and and saves it in a cookie.
     * @param url  authentication server logout url
     */
    public static void setTokenServerLogoutURL(String url)
    {
        RESTILITY.m_tokenServerLogoutUrl = url;
        setSessionToken();
    }


    /**
     * We can't know the best locale until after the user logs in because we need to consider
     * their locale from the identity vault.  So we get the locale as part of the identity
     * information and then store this in a cookie.  If the cookie doesn't match the current
     * locale then we need to refresh the page so we can reload the JavaScript libraries.
     *
     * @param locale the locale
     */
    public static void setBestLocale(String locale)
    {
        if (getBestLocale() != null) {
            if (!getBestLocale().equals(locale)) {
                /*
                 If the best locale from the server doesn't
                 match the cookie.  That means we set the cookie
                 with the new locale and refresh the page.
                 */
                RESTILITY.m_bestLocale = locale;
                setSessionToken();
                JSUtil.reload();
            } else {
                /*
                 If the best locale from the server matches
                 the best locale from the cookie then we are
                 done.
                 */
                return;
            }
        } else {
            /*
             This means they didn't have a best locale from
             the server stored as a cookie in the client.  So
             we set the locale from the server into the cookie.
             */
            RESTILITY.m_bestLocale = locale;
            setSessionToken();

            /*
             * If there are any REST requests in process when we refresh
             * the page they will cause errors that show up before the
             * page reloads.  Hiding the page makes those errors invisible.
             */
            JSUtil.hide("body", "");
            JSUtil.reload();
        }
    }
    
    /**
     * <p>
     * Set the path for the Spiffy_Session cookie.
     * </p>
     * 
     * <p>
     * When Spiffy UI uses token based authentication it saves token and user information 
     * in a cookie named Spiffy_Session.  This cookie allows the user to remain logged in
     * after they refresh the page the reset JavaScript memory.  
     * </p>
     * 
     * <p>
     * By default that cookie uses the path of the current page.  If an application uses 
     * multiple pages it can make sense to use a more general path for this cookie to make
     * it available to other URLs in the application.
     * </p>
     * 
     * <p>
     * The path must be set before the first authentication request.  If it is called 
     * afterward the cookie path will not change.
     * </p>
     * 
     * @param newPath the new path for the cookie or null if the cookie should use the path of the current page
     */
    public static void setSessionCookiePath(String newPath)
    {
        RESTILITY.m_sessionCookiePath = newPath;
    }
    
    /**
     * Get the path of the session cookie.  By default this is null indicating a path of
     * the current page will be used.
     * 
     * @return the current session cookie path.
     */
    public static String getSessionCookiePath()
    {
        return RESTILITY.m_sessionCookiePath;
    }

    private static void setSessionToken()
    {
        if (RESTILITY.m_sessionCookiePath != null) {
            Cookies.setCookie(RESTILITY.m_sessionCookie, RESTILITY.m_tokenType + "," +
                              RESTILITY.m_userToken + "," + RESTILITY.m_tokenServerUrl + "," +
                              RESTILITY.m_tokenServerLogoutUrl + "," + RESTILITY.m_username, 
                              null, null, RESTILITY.m_sessionCookiePath, RESTILITY.m_secureCookies);
            if (RESTILITY.m_bestLocale != null) {
                Cookies.setCookie(LOCALE_COOKIE, RESTILITY.m_bestLocale, null, null, 
                                  RESTILITY.m_sessionCookiePath, RESTILITY.m_secureCookies);
            }
        } else {
            Cookies.setCookie(RESTILITY.m_sessionCookie, RESTILITY.m_tokenType + "," +
                              RESTILITY.m_userToken + "," + RESTILITY.m_tokenServerUrl + "," +
                              RESTILITY.m_tokenServerLogoutUrl + "," + RESTILITY.m_username,
                              null, null, null, RESTILITY.m_secureCookies);
            if (RESTILITY.m_bestLocale != null) {
                Cookies.setCookie(LOCALE_COOKIE, RESTILITY.m_bestLocale, null, null, null, RESTILITY.m_secureCookies);
            }
        }
    }

    /**
     * Returns a boolean flag indicating whether user has logged in or not
     *
     * @return boolean indicating whether user has logged in or not
     */
    public static boolean hasUserLoggedIn()
    {
        return RESTILITY.m_hasLoggedIn;
    }

    /**
     * Returns user's full authentication token, prefixed with "X-OPAQUE"
     *
     * @return user's full authentication token prefixed with "X-OPAQUE"
     */
    public static String getFullAuthToken()
    {
        return getTokenType() + " " + getUserToken();
    }
    
    private static void checkSessionCookie()
    {
         String sessionCookie = Cookies.getCookie(RESTILITY.m_sessionCookie);
         if (sessionCookie != null && sessionCookie.length() > 0) {
             
             // If the cookie value is quoted, strip off the enclosing quotes
             if (sessionCookie.length() > 2 && 
                  sessionCookie.charAt(0) == '"' && 
                  sessionCookie.charAt(sessionCookie.length() - 1) == '"') {
                 sessionCookie = sessionCookie.substring(1, sessionCookie.length() - 1);
             }
             String sessionCookiePieces [] = sessionCookie.split(",");
                
             if (sessionCookiePieces != null) {
                 if (sessionCookiePieces.length >= 1) {
                     RESTILITY.m_tokenType = sessionCookiePieces [0];
                 }
                 if (sessionCookiePieces.length >= 2) {
                     RESTILITY.m_userToken = sessionCookiePieces [1];
                 }
                 if (sessionCookiePieces.length >= 3) {
                     RESTILITY.m_tokenServerUrl = sessionCookiePieces [2];
                 }
                 if (sessionCookiePieces.length >= 4) {
                     RESTILITY.m_tokenServerLogoutUrl = sessionCookiePieces [3];
                 }
                 if (sessionCookiePieces.length >= 5) {
                     RESTILITY.m_username = sessionCookiePieces [4];
                 }
             }
         }
    }

    /**
     * Returns user's authentication token
     *
     * @return user's authentication token
     */
    public static String getUserToken()
    {
        if (RESTILITY.m_userToken == null) {
            checkSessionCookie();
        }
        return RESTILITY.m_userToken;
    }
    
    /**
     * Returns user's authentication token type
     *
     * @return user's authentication token type
     */
    public static String getTokenType()
    {
        if (RESTILITY.m_tokenType == null) {
            checkSessionCookie();
        }
        return RESTILITY.m_tokenType;
    }

    /**
     * Returns the authentication server url
     *
     * @return authentication server url
     */
    public static String getTokenServerUrl()
    {
        if (RESTILITY.m_tokenServerUrl == null) {
            checkSessionCookie();
        }
        return RESTILITY.m_tokenServerUrl;
    }

    /**
     * Returns authentication server logout url
     *
     * @return authentication server logout url
     */
    public static String getTokenServerLogoutUrl()
    {
        if (RESTILITY.m_tokenServerLogoutUrl == null) {
            checkSessionCookie();
        }
        return RESTILITY.m_tokenServerLogoutUrl;
    }

    /**
     * Returns the name of the currently logged in user or null
     * if the current user is not logged in.
     * 
     * @return  user name
     */
    public static String getUsername()
    {
        if (RESTILITY.m_username == null) {
            checkSessionCookie();
        }
        return RESTILITY.m_username;
    }

    /**
     * Returns best matched locale
     *
     * @return best matched locale
     */
    public static String getBestLocale()
    {
        if (RESTILITY.m_bestLocale != null) {
            return RESTILITY.m_bestLocale;
        } else {
            RESTILITY.m_bestLocale = Cookies.getCookie(LOCALE_COOKIE);
            return RESTILITY.m_bestLocale;
        }
    }

    public static List<RESTLoginCallBack> getLoginListeners()
    {
        return  g_loginListeners;
    }

    /**
     * Make an HTTP call and get the results as a JSON object.  This method handles
     * cases like login, error parsing, and configuration requests.
     *
     * @param url      the properly encoded REST url to call
     * @param data     the data to pass to the URL
     * @param method   the HTTP method, defaults to GET
     * @param callback the callback object for handling the request results
     */
    public static void callREST(String url, String data, RESTility.HTTPMethod method, RESTCallback callback)
    {
        callREST(url, data, method, callback, false, null);
    }

    /**
     * Make an HTTP call and get the results as a JSON object.  This method handles
     * cases like login, error parsing, and configuration requests.
     *
     * @param url      the properly encoded REST url to call
     * @param data     the data to pass to the URL
     * @param method   the HTTP method, defaults to GET
     * @param callback the callback object for handling the request results
     * @param etag     the option etag for this request
     */
    public static void callREST(String url, String data, RESTility.HTTPMethod method,
                                RESTCallback callback, String etag)
    {
        callREST(url, data, method, callback, false, etag);
    }


    /**
     * The client can't really handle the test for all XSS attacks, but we can
     * do some general sanity checking.
     * 
     * @param data   the data to check
     * 
     * @return true if the data is valid and false otherwise
     */
    private static boolean hasPotentialXss(final String data)
    {
        if (data == null) {
            return false;
        }

        String uppercaseData = data.toUpperCase();
        
        if (uppercaseData.indexOf("<SCRIPT") > -1) {
            return true;
        }
        
        return false;
    }


    /**
     * Make an HTTP call and get the results as a JSON object.  This method handles
     * cases like login, error parsing, and configuration requests.
     *
     * @param url      the properly encoded REST url to call
     * @param data     the data to pass to the URL
     * @param method   the HTTP method, defaults to GET
     * @param callback the callback object for handling the request results
     * @param isLoginRequest
     *                 true if this is a request to login and false otherwise
     * @param etag     the option etag for this request
     *
     */
    public static void callREST(String url, String data, RESTility.HTTPMethod method,
                                   RESTCallback callback, boolean isLoginRequest, String etag)
    {
        callREST(url, data, method, callback, isLoginRequest, true, etag);
    }
    
    /**
     * <p> 
     * Make an HTTP call and get the results as a JSON object.  This method handles
     * allows the caller to override any HTTP headers in the request. 
     * </p> 
     *  
     * <p> 
     * By default RESTility sets two HTTP headers: <code>Accept=application/json</code> and 
     * <code>Accept-Charset=UTF-8</code>.  Other headers are added by the browser running this 
     * application.
     * </p> 
     *
     * @param url      the properly encoded REST url to call
     * @param data     the data to pass to the URL
     * @param method   the HTTP method, defaults to GET
     * @param callback the callback object for handling the request results
     * @param etag     the option etag for this request 
     * @param headers  a map containing the headers to the HTTP request.  Any item 
     *                 in this map will override the default headers. 
     */
    public static void callREST(String url, String data, RESTility.HTTPMethod method,
                                RESTCallback callback, String etag, Map<String, String> headers)
    {
        callREST(url, data, method, callback, false, true, etag, headers);
    }

    /**
     * Make an HTTP call and get the results as a JSON object.  This method handles
     * cases like login, error parsing, and configuration requests.
     *
     * @param url      the properly encoded REST url to call
     * @param data     the data to pass to the URL
     * @param method   the HTTP method, defaults to GET
     * @param callback the callback object for handling the request results
     * @param isLoginRequest
     *                 true if this is a request to login and false otherwise
     * @param shouldReplay true if this request should repeat after a login request
     *                 if this request returns a 401
     * @param etag     the option etag for this request
     */
    public static void callREST(String url, String data, RESTility.HTTPMethod method,
                                RESTCallback callback, boolean isLoginRequest,
                                boolean shouldReplay, String etag)
    {
        callREST(url, data, method, callback, isLoginRequest, shouldReplay, etag, null);
    }
    
    /**
     * <p> 
     * Make an HTTP call and get the results as a JSON object.  This method handles
     * cases like login, error parsing, and configuration requests. 
     * </p> 
     *  
     * <p> 
     * By default RESTility sets two HTTP headers: <code>Accept=application/json</code> and 
     * <code>Accept-Charset=UTF-8</code>.  Other headers are added by the browser running this 
     * application.
     * </p>  
     *
     * @param url      the properly encoded REST url to call
     * @param data     the data to pass to the URL
     * @param method   the HTTP method, defaults to GET
     * @param callback the callback object for handling the request results
     * @param isLoginRequest
     *                 true if this is a request to login and false otherwise
     * @param shouldReplay true if this request should repeat after a login request
     *                 if this request returns a 401
     * @param etag     the option etag for this request 
     * @param headers  a map containing the headers to the HTTP request.  Any item 
     *                 in this map will override the default headers.  
     */
    public static void callREST(String url, String data, RESTility.HTTPMethod method,
                                RESTCallback callback, boolean isLoginRequest,
                                boolean shouldReplay, String etag, Map<String, String> headers)
    {
        RESTOptions options = new RESTOptions();
        options.setURL(url);
        if (data != null && data.trim().length() > 0) {
            options.setData(JSONParser.parseStrict(data));
        }
        options.setMethod(method);
        options.setCallback(callback);
        options.setIsLoginRequest(isLoginRequest);
        options.setShouldReplay(shouldReplay);
        options.setEtag(etag);
        options.setHeaders(headers);
        callREST(options);
    }
    
    /**
     * <p>
     * Make an HTTP call and get the results as a JSON object.  This method handles
     * cases like login, error parsing, and configuration requests.
     *  </p>
     * 
     * @param options the options for the REST request
     */
    public static void callREST(RESTOptions options)
    {
        if (hasPotentialXss(options.getDataString())) {
            options.getCallback().onError(new RESTException(RESTException.XSS_ERROR,
                                                            "", STRINGS.noServerContact(),
                                                            new HashMap<String, String>(),
                                                            -1, options.getURL()));
            return;
        }

        RESTILITY.m_restCalls.put(options.getCallback(), new RESTCallStruct(options.getURL(), 
                                                                            options.getDataString(), options.getMethod(), 
                                                                            options.shouldReplay(), options.getEtag()));

        RequestBuilder builder = new RESTRequestBuilder(options.getMethod().getMethod(), options.getURL());
        /*
         Set our headers
         */
        builder.setHeader("Accept", "application/json");
        builder.setHeader("Accept-Charset", "UTF-8");
        if (options.getHeaders() != null) {
            for (String k : options.getHeaders().keySet()) {
                builder.setHeader(k, options.getHeaders().get(k));
            }
        }

        if (RESTILITY.m_bestLocale != null) {
            /*
             * The REST end points use the Accept-Language header to determine
             * the locale to use for the contents of the REST request.  Normally
             * the browser will fill this in with the browser locale and that
             * doesn't always match the preferred locale from the Identity Vault
             * so we need to set this value with the correct preferred locale.
             */
            builder.setHeader("Accept-Language", RESTILITY.m_bestLocale);
        }

        if (getUserToken() != null &&
            getTokenServerUrl() != null) {
            builder.setHeader("Authorization", getFullAuthToken());
            builder.setHeader("TS-URL", getTokenServerUrl());
        }

        if (options.getEtag() != null) {
            builder.setHeader("If-Match", options.getEtag());
        }

        if (options.getData() != null) {
            /*
             Set our request data
             */
            builder.setRequestData(options.getDataString());

            //b/c jaxb/jersey chokes when there is no data when content-type is json
            builder.setHeader("Content-Type", "application/json");
        }

        builder.setCallback(RESTILITY.new RESTRequestCallback(options.getCallback()));

        try {
            /*
             If we are in the process of logging in then all other
             requests will just return with a 401 until the login
             is finished.  We want to delay those requests until
             the login is complete when we will replay all of them.
             */
            if (options.isLoginRequest() || !g_inLoginProcess) {
                builder.send();
            }
        } catch (RequestException e) {
            MessageUtil.showFatalError(e.getMessage());
        }

    }

    /**
     * The RESTCallBack object that implements the RequestCallback interface
     */
    private class RESTRequestCallback implements RequestCallback
    {
        private RESTCallback m_origCallback;

        public RESTRequestCallback(RESTCallback callback)
        {
            m_origCallback = callback;
        }
        
        /**
         * Check the server response for an NCAC formatted fault and parse it into a RESTException
         * if it is .
         * 
         * @param val      the JSON value returned from the server
         * @param response the server response
         * 
         * @return the RESTException if the server response contains an NCAC formatted fault
         */
        private RESTException handleNcacFault(JSONValue val, Response response)
        {
            RESTCallStruct struct = RESTILITY.m_restCalls.get(m_origCallback);
            RESTException exception = JSONUtil.getRESTException(val, response.getStatusCode(), struct.getUrl());
            
            if (exception == null) {
                return null;
            } else {
                if (RESTException.AUTH_SERVER_UNAVAILABLE.equals(exception.getSubcode())) {
                    /*
                     * This is a special case where the server can't connect to the
                     * authentication server to validate the token.
                     */
                    MessageUtil.showFatalError(STRINGS.unabledAuthServer());
                }
                
                return exception;
            }
        }
        
        /**
         * Handles an unauthorized (401) response from the server
         * 
         * @param struct    the struct for this request
         * @param exception the exception for this request if available
         * @param response  the response object
         * 
         * @return true if this is an invalid request and false otherwise
         */
        private boolean handleUnauthorized(RESTCallStruct struct, RESTException exception, Response response)
        {
            JSUtil.println("response.getStatusCode(): " + response.getStatusCode());
            JSUtil.println("struct.getUrl(): " + struct.getUrl());
            if (response.getStatusCode() == Response.SC_UNAUTHORIZED) {
                if (g_inLoginProcess) {
                    /*
                     * If we're already in the process of logging in then it will complete
                     * and this call will be replayed and we don't want to start a second
                     * login process.
                     */
                    return true;
                }

                g_inLoginProcess = true;
                /*
                 * For return values of 401 we need to show the login dialog
                 */
                try {
                    for (RESTLoginCallBack listener : g_loginListeners) {
                        listener.loginPrompt();
                    }

                    String code = null;
                    if (exception != null) {
                        code = exception.getSubcode();
                    }

                    doLogin(m_origCallback, response, struct.getUrl(), code, exception);
                } catch (RESTException e) {
                    RESTILITY.m_restCalls.remove(m_origCallback);
                    m_origCallback.onError(e);
                }
                return true;
            } else if (response.getStatusCode() == Response.SC_FORBIDDEN) {
                handleNoPrivilege(exception);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onResponseReceived(Request request, Response response)
        {
            JSUtil.println("onResponseReceived.response.getStatusCode(): " + response.getStatusCode());
            JSUtil.println("RESTILITY.m_restCalls.get(m_origCallback)..getUrl(): " + 
                           RESTILITY.m_restCalls.get(m_origCallback).getUrl());
            if (response.getStatusCode() == 0) {
                /*
                 This means we couldn't contact the server.  It might be that the
                 server is down or that we have a network timeout
                 */
                RESTCallStruct struct = RESTILITY.m_restCalls.remove(m_origCallback);
                m_origCallback.onError(new RESTException(RESTException.NO_SERVER_RESPONSE,
                                                         "", STRINGS.noServerContact(),
                                                         new HashMap<String, String>(),
                                                         response.getStatusCode(),
                                                         struct.getUrl()));
                return;
            }

            JSUtil.println("checkJSON(response.getText()): " + checkJSON(response.getText()));

            if (!checkJSON(response.getText())) {
                if (handleUnauthorized(RESTILITY.m_restCalls.get(m_origCallback), null, response)) {
                    return;
                } else {
                    RESTCallStruct struct = RESTILITY.m_restCalls.remove(m_origCallback);
                    m_origCallback.onError(new RESTException(RESTException.UNPARSABLE_RESPONSE,
                                                             "", "", new HashMap<String, String>(),
                                                             response.getStatusCode(),
                                                             struct.getUrl()));
                    return;
                }
            }

            JSONValue val = null;
            RESTException exception = null;
            
            if (response.getText() != null &&
                response.getText().trim().length() > 1) {
                val = null;
                
                try {
                    val = JSONParser.parseStrict(response.getText());
                } catch (JavaScriptException e) {
                    /*
                     This means we couldn't parse the response this is unlikely
                     because we have already checked it, but it is possible.
                     */
                    RESTCallStruct struct = RESTILITY.m_restCalls.get(m_origCallback);
                    exception = new RESTException(RESTException.UNPARSABLE_RESPONSE,
                                                  "", response.getText(), new HashMap<String, String>(),
                                                  response.getStatusCode(),
                                                  struct.getUrl());
                }

                exception = handleNcacFault(val, response);
            }


            

            RESTCallStruct struct = RESTILITY.m_restCalls.get(m_origCallback);
            if (handleUnauthorized(struct, exception, response)) {
                /*
                 Then this is a 401 and the login will handle it
                 */
                return;
            } else {
                handleSuccessfulResponse(val, exception, response);
            }
        }

        /**
         * Handle successful REST responses which have parsable JSON, aren't NCAC faults, 
         * and don't contain login requests.
         * 
         * @param val       the JSON value returned from the server
         * @param exception the exception generated by the response if available
         * @param response  the server response
         */
        private void handleSuccessfulResponse(JSONValue val, RESTException exception, Response response)
        {
            RESTILITY.m_restCalls.remove(m_origCallback);

            if (exception != null) {
                m_origCallback.onError(exception);
            } else {
                RESTILITY.m_callCount++;
                /*
                 * You have to have at least three valid REST calls before
                 * we show the application UI.  This covers the build info
                 * and the successful login with and invalid token.  It
                 * would be really nice if we didn't have to worry about
                 * this at this level, but then the UI will flash sometimes
                 * before the user has logged in.  Hackito Ergo Sum.
                 */
                if (RESTILITY.m_callCount > 2) {
                    fireLoginSuccess();
                }

                if (response.getHeader("ETag") != null &&
                    m_origCallback instanceof ConcurrentRESTCallback) {
                    ((ConcurrentRESTCallback) m_origCallback).setETag(response.getHeader("ETag"));
                }

                m_origCallback.onSuccess(val);
            }
        }


        public void onError(Request request, Throwable exception)
        {
            MessageUtil.showFatalError(exception.getMessage());
        }
    }

    /**
     * The GWT parser calls the JavaScript eval function.  This is dangerous since it
     * can execute arbitrary JavaScript.  This method does a simple check to make sure
     * the JSON data we get back from the server is safe to parse.
     *
     * This parsing scheme is taken from RFC 4627 - http://www.ietf.org/rfc/rfc4627.txt
     *
     * @param json   the JSON string to test
     *
     * @return true if it is safe to parse and false otherwise
     */
    private static native boolean checkJSON(String json)  /*-{
        return !(/[^,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]/.test(
             json.replace(/"(\\.|[^"\\])*"/g, '')));
    }-*/;

    /**
     * Add login listeners
     *
     * @param callback listeners to be added
     */
    public static void addLoginListener(RESTLoginCallBack callback)
    {
        RESTility.g_loginListeners.add(callback);
    }

    /**
     * Remove login listeners
     *
     * @param callback listeners to be removed
     */
    public static void removeLoginListener(RESTLoginCallBack callback)
    {
        RESTility.g_loginListeners.remove(callback);
    }
}

/**
 * A struct for holding data about a REST request
 */
class RESTCallStruct
{
    private String m_url;
    private String m_data;
    private RESTility.HTTPMethod m_method;
    private boolean m_shouldReplay;
    private String m_etag;

    /**
     * Creates a new RESTCallStruct
     *
     * @param url    the URL for this REST call
     * @param data   the data for this REST call
     * @param method the method for this REST call
     * @param shouldReplay should this request be repeated if we get a 401
     */
    protected RESTCallStruct(String url, String data, RESTility.HTTPMethod method,
                             boolean shouldReplay, String etag)
    {
        m_url = url;
        m_data = data;
        m_method = method;
        m_shouldReplay = shouldReplay;
        m_etag = etag;
    }

    /**
     * Gets the URL
     *
     * @return the URL
     */
    public String getUrl()
    {
        return m_url;
    }

    /**
     * Gets the data
     *
     * @return the data
     */
    public String getData()
    {
         return m_data;
    }

    /**
     * Gets the HTTP method
     *
     * @return the method
     */
    public RESTility.HTTPMethod getMethod()
    {
        return m_method;
    }

    /**
     * Gets the ETag for this call
     *
     * @return the ETag
     */
    public String getETag()
    {
        return m_etag;
    }

    /**
     * Should this request repeat
     *
     * @return true if it should repeat, false otherwise
     */
    public boolean shouldReplay()
    {
        return m_shouldReplay;
    }
}

/**
 * This class extends RequestBuilder so we can call PUT and DELETE
 */
class RESTRequestBuilder extends RequestBuilder
{
    /**
     * Creates a new RESTRequestBuilder
     *
     * @param method the HTTP method
     * @param url    the request URL
     */
    public RESTRequestBuilder(String method, String url)
    {
        super(method, url);
    }
}

