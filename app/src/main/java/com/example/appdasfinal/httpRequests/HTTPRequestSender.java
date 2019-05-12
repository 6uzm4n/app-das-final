package com.example.appdasfinal.httpRequests;

import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
https://github.com/AnderRasoVazquez/android-project-manager/blob/master/app/src/main/java/com/example/projectmanager/controller/Facade.java
 */
public class HTTPRequestSender {

    /*
    Link to the API documentation
    https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST
     */

    private static final String SERVER_ADDRESS = "https://api-das.herokuapp.com/api/v1/";
    private static final String URL_LOGIN = SERVER_ADDRESS + "login";
    private static final String URL_REGISTER = SERVER_ADDRESS + "users";
    private static final String URL_GET_PROJECTS = SERVER_ADDRESS + "projects";
    private static final String URL_GET_PROJECT = SERVER_ADDRESS + "projects/%s";
    private static final String URL_CREATE_PROJECT = SERVER_ADDRESS + "projects";
    private static final String URL_UPDATE_PROJECT = SERVER_ADDRESS + "projects/%s";
    private static final String URL_DELETE_PROJECT = SERVER_ADDRESS + "projects/%s";
    private static final String URL_GET_REQUESTS = SERVER_ADDRESS + "projects/%s/requests";
    private static final String URL_GET_REQUEST = SERVER_ADDRESS + "requests/%s";
    private static final String URL_CREATE_REQUEST = SERVER_ADDRESS + "projects/%s/requests";
    private static final String URL_UPDATE_REQUEST = SERVER_ADDRESS + "requests/%s";
    private static final String URL_DELETE_REQUEST = SERVER_ADDRESS + "requests/%s";

    private String serverToken = "";

    private static HTTPRequestSender instance = null;

    private HTTPRequestSender() {
    }

    public static HTTPRequestSender getInstance() {
        if (instance == null) {
            instance = new HTTPRequestSender();
        }
        return instance;
    }

    /**
     * Sets the identification token for the current user.
     *
     * @param serverToken User token
     */
    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public String getServerToken() {
        return this.serverToken;
    }

    public HttpRequest.Builder login(String email, String password) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", getBasicAuth(email, password));

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.GET)
                .setUrl(URL_LOGIN)
                .setHeaders(headers);
        return builder;
    }

    private static String getBasicAuth(String email, String password) {
        String authString = email + ":" + password;
        byte[] authStringEnc = Base64.encode(authString.getBytes(), Base64.DEFAULT);
        return "Basic " + new String(authStringEnc);
    }

    public HttpRequest.Builder register(String email, String password) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        JSONObject data = new JSONObject();
        try {
            data.put("email", email);
            data.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.POST)
                .setUrl(URL_REGISTER)
                .setHeaders(headers)
                .setBody(data.toString());

        return builder;
    }

    public HttpRequest.Builder getProjects() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("x-access-token", getInstance().getServerToken());

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.GET)
                .setUrl(URL_GET_PROJECTS)
                .setHeaders(headers);

        return builder;
    }

    public HttpRequest.Builder getProject(String projectId) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("x-access-token", getInstance().getServerToken());

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.GET)
                .setUrl(String.format(URL_GET_PROJECT, projectId))
                .setHeaders(headers);

        return builder;
    }

    public HttpRequest.Builder createProject(String projectName) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token", getInstance().getServerToken());

        JSONObject data = new JSONObject();
        try {
            data.put("name", projectName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.POST)
                .setUrl(URL_CREATE_PROJECT)
                .setBody(data.toString())
                .setHeaders(headers);

        return builder;
    }

    public HttpRequest.Builder updateProject(String projectId, String projectName) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token", getInstance().getServerToken());

        JSONObject data = new JSONObject();
        try {
            data.put("name", projectName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.POST)
                .setUrl(String.format(URL_UPDATE_PROJECT, projectId))
                .setBody(data.toString())
                .setHeaders(headers);

        return builder;
    }

    public HttpRequest.Builder deleteProject(String projectId) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("x-access-token", getInstance().getServerToken());

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.DELETE)
                .setUrl(String.format(URL_DELETE_PROJECT, projectId))
                .setHeaders(headers);

        return builder;
    }

    public HttpRequest.Builder getRequests(String projectId) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("x-access-token", getInstance().getServerToken());

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.GET)
                .setUrl(String.format(URL_GET_REQUESTS, projectId))
                .setHeaders(headers);

        return builder;
    }

    public HttpRequest.Builder getRequest(String requestId) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("x-access-token", getInstance().getServerToken());

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.GET)
                .setUrl(String.format(URL_GET_REQUEST, requestId))
                .setHeaders(headers);

        return builder;
    }

    public HttpRequest.Builder createRequest(String projectId, String requestName) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token", getInstance().getServerToken());

        JSONObject data = new JSONObject();
        try {
            data.put("name", requestName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.POST)
                .setUrl(String.format(URL_CREATE_REQUEST, projectId))
                .setHeaders(headers)
                .setBody(data.toString());

        return builder;
    }

    public HttpRequest.Builder updateRequest(String requestId,
                                             String requestName,
                                             String requestURL,
                                             String requestBody,
                                             String requestMethod,
                                             HashMap<String, String> requestHeaders) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token", getInstance().getServerToken());

        JSONObject data = new JSONObject();
        try {
            if (requestName != null && !requestName.equals("")) {
                data.put("name", requestName);
            }

            if (requestURL != null && !requestURL.equals("")) {
                data.put("url", requestURL);
            }

            if (requestMethod != null && !requestMethod.equals("")) {
                data.put("method", requestMethod);
                if (requestMethod.toUpperCase().equals("GET")) {
                    data.put("body", "");
                } else {
                    if (requestBody != null) {
                        data.put("body", requestBody);
                    }
                }
            }

            if (requestHeaders != null) {
                JSONArray jsonRequestHeaders = new JSONArray();
                for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                    JSONObject jsonRequestHeader = new JSONObject();
                    jsonRequestHeader.put("key", entry.getKey());
                    jsonRequestHeader.put("value", entry.getValue());

                    jsonRequestHeaders.put(jsonRequestHeader);
                }
                data.put("headers", jsonRequestHeaders);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.POST)
                .setUrl(String.format(URL_UPDATE_REQUEST, requestId))
                .setHeaders(headers)
                .setBody(data.toString());


        return builder;
    }

    public HttpRequest.Builder deleteRequest(String requestId) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("x-access-token", getInstance().getServerToken());

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.DELETE)
                .setUrl(String.format(URL_DELETE_REQUEST, requestId))
                .setHeaders(headers);

        return builder;
    }

    public HttpRequest.Builder customRequest(String method, String url, String body, HashMap<String, String> headers) {
        HttpRequest.RequestMethod requestMethod;
        try {
            requestMethod = HttpRequest.RequestMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setUrl(url)
                .setBody(body)
                .setRequestMethod(requestMethod)
                .setHeaders(headers);

        return builder;
    }
}
