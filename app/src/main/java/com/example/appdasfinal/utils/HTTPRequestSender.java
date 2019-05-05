package com.example.appdasfinal.utils;

import android.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/*
https://github.com/AnderRasoVazquez/android-project-manager/blob/master/app/src/main/java/com/example/projectmanager/controller/Facade.java
 */
public class HTTPRequestSender {

    public static final String SERVER_ADDRESS = "https://api-das.herokuapp.com/api/v1/";
    public static final String URL_LOGIN = SERVER_ADDRESS + "login";
    public static final String URL_REGISTER = SERVER_ADDRESS + "users";
    public static final String URL_GET_PROJECTS = SERVER_ADDRESS + "projects";
    public static final String URL_GET_PROJECT = SERVER_ADDRESS + "projects/%s";
    public static final String URL_CREATE_PROJECT = SERVER_ADDRESS + "projects";
    public static final String URL_UPDATE_PROJECT = SERVER_ADDRESS + "projects/%s";
    public static final String URL_DELETE_PROJECT = SERVER_ADDRESS + "projects/%s";

    private String serverToken = "";

    private static HTTPRequestSender instance = null;

    private HTTPRequestSender() {}

    public static HTTPRequestSender getInstance() {
        if (instance == null) {
            instance = new HTTPRequestSender();
        }
        return instance;
    }

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

        System.out.println(data.toString());

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.POST)
                .setUrl(URL_REGISTER)
                .setHeaders(headers)
                .setBody(data);

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
                .setBody(data)
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
                .setBody(data)
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

}
