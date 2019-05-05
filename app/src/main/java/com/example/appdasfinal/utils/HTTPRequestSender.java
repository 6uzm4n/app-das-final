package com.example.appdasfinal.utils;

import android.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/*
https://github.com/AnderRasoVazquez/android-project-manager/blob/master/app/src/main/java/com/example/projectmanager/controller/Facade.java
 */
public class HTTPRequestSender {

    /*
    Link to the API documentation
    https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST
     */

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

    /**
     * Tries to log in using and email and password.
     * If successful, the response will contain the user token to
     * use in future requests:
     * {
     * "token": "<TOKEN>"
     * }
     *
     * @param email    User email
     * @param password User password
     * @return A String with the server's response.
     */
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

    /**
     * Tries to register a new user.
     * If successful, the response will contain information
     * about the user.
     * {
     * "message":"New user created!",
     * "user":{
     * "email":"<EMAIL>",
     * "projects":[],
     * "user_id":"<USER_ID>"
     * }
     * }
     *
     * @param email    User email
     * @param password User password
     * @return A String with the server's response.
     */
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

    /**
     * Tries to get all the projects from the user whose
     * id token has been stored.
     * If successful, the response will contain information
     * about the user's projects.
     * {
     * "projects": [
     * {
     * "_links": {
     * "collection": "/api/v1/projects",
     * "self": "/api/v1/projects/7d5cb349-8425-4280-bd57-934d1b7cd75b"
     * },
     * "name": "Pokemon API",
     * "project_id": "7d5cb349-8425-4280-bd57-934d1b7cd75b",
     * "requests": [
     * "2246053a-c226-4b23-a6ad-272c7c08223d",
     * "827323ac-1039-44fd-b65f-1d5fd11305ed"
     * ],
     * "user": "6bca489e-a766-4cfa-870c-6b7090259e5b"
     * },
     * {
     * ...
     * }
     * ]
     * }
     *
     * @return A String with the server's response.
     */
    public HttpRequest.Builder getProjects() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("x-access-token", getInstance().getServerToken());

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.GET)
                .setUrl(URL_GET_PROJECTS)
                .setHeaders(headers);

        return builder;
    }

    /**
     * Tries to get a project from the user whose
     * id token has been stored.
     * If successful, the response will contain information
     * about the project.
     * {
     * "project": {
     * "_links": {
     * "collection": "/api/v1/projects",
     * "self": "/api/v1/projects/7d5cb349-8425-4280-bd57-934d1b7cd75b"
     * },
     * "name": "Pokemon API",
     * "project_id": "7d5cb349-8425-4280-bd57-934d1b7cd75b",
     * "requests": [
     * "2246053a-c226-4b23-a6ad-272c7c08223d",
     * "827323ac-1039-44fd-b65f-1d5fd11305ed"
     * ],
     * "user": "6bca489e-a766-4cfa-870c-6b7090259e5b"
     * }
     * }
     *
     * @param projectId Id of the project
     * @return A String with the server's response.
     */
    public HttpRequest.Builder getProject(String projectId) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("x-access-token", getInstance().getServerToken());

        HttpRequest.Builder builder = new HttpRequest.Builder();
        builder.setRequestMethod(HttpRequest.RequestMethod.GET)
                .setUrl(String.format(URL_GET_PROJECT, projectId))
                .setHeaders(headers);

        return builder;
    }

    /**
     * Tries to create a project for the user whose
     * id token has been stored.
     * If successful, the response will contain information
     * about the project.
     * {
     * "message": "New project created!",
     * "project": {
     * "_links": {
     * "collection": "/api/v1/projects",
     * "self": "/api/v1/projects/64862083-44ad-412c-a099-07f420ecf95a"
     * },
     * "name": "Proyecto",
     * "project_id": "64862083-44ad-412c-a099-07f420ecf95a",
     * "requests": [],
     * "user": "30ced211-2298-47a0-a49e-aa33e68ee995"
     * }
     * }
     *
     * @param projectName Project name
     * @return A String with the server's response.
     */
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

    /**
     * Tries to change the name a project of the user whose
     * id token has been stored.
     * If successful, the response will contain information
     * about the project.
     * {
     * "message": "Project updated!",
     * "project": {
     * "_links": {
     * "collection": "/api/v1/projects",
     * "self": "/api/v1/projects/64862083-44ad-412c-a099-07f420ecf95a"
     * },
     * "name": "Nombre nuevo",
     * "project_id": "64862083-44ad-412c-a099-07f420ecf95a",
     * "requests": [],
     * "user": "30ced211-2298-47a0-a49e-aa33e68ee995"
     * }
     * }
     *
     * @param projectId   Project id
     * @param projectName Project name
     * @return A String with the server's response.
     */
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

    /**
     * Tries to delete name a project of the user whose
     * id token has been stored.
     *
     * @param projectId   Project id
     * @return A String with the server's response.
     */
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
