package com.example.appdasfinal.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerRequestHandler {

    public interface ServerRequestHandlerListener {

        /**
         * Called when the login request has been completed.
         *
         * @param token User token returned by the server. Null if any problems occurred
         */
        default void onLoginResponse(String token) {}

        /**
         * Called when the register request has been completed.
         *
         * @param message Message returned by the server. Indicates success of cause of error
         * @param userId  User id returned by the server. Null if the registration was not successful
         */
        default void onRegisterResponse(String message, String userId) {}

        /**
         * Called when the get projects request has been completed.
         *
         * @param jsonProjects List of the projects returned by the server. Null if any problems occurred.
         *                     Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#obtener-todos-los-proyectos-del-usuario
         */
        default void onGetProjectsResponse(JSONArray jsonProjects) {}

        /**
         * Called when the create projects request has been completed.
         *
         * @param message     Message returned by the server. Indicates success of cause of error
         * @param jsonProject Project data returned by the server. Null if any problems occurred.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#crear-un-proyecto
         */
        default void onCreateProjectsResponse(String message, JSONObject jsonProject) {}

        /**
         * Called when the update projects request has been completed.
         *
         * @param message     Message returned by the server. Indicates success of cause of error
         * @param jsonProject Project data returned by the server. Null if any problems occurred.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#modificar-un-proyecto
         */
        default void onUpdateProjectsResponse(String message, JSONObject jsonProject) {}

        /**
         * Called when the update projects request has been completed.
         *
         * @param message Message returned by the server. Indicates success of cause of error
         * @param success Boolean indicating the success of the operation
         */
        default void onDeleteProjectsResponse(String message, boolean success) {}
    }

    public static void login(String email, String password, ServerRequestHandlerListener listener) {
        login(email, password, true, listener);
    }

    public static void login(String email, String password, boolean saveToken, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().login(email, password).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String token = jsonResponse.getString("token");
                            if (saveToken) {
                                HTTPRequestSender.getInstance().setServerToken(token);
                            }
                            listener.onLoginResponse(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onLoginResponse(null);
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onLoginResponse(null);
                    }
                }
        );
    }

    public static void register(String email, String password, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().register(email, password).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        try {
                            // get the success message and the user id
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            String userId = jsonResponse.getJSONObject("user").getString("user_id");
                            listener.onRegisterResponse(
                                    message,
                                    userId
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onRegisterResponse(
                                    "Unexpected error",
                                    null);
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        try {
                            // get the error message
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onRegisterResponse(
                                    message,
                                    null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onRegisterResponse(
                                    "Unexpected error",
                                    null);
                        }
                    }
                }
        );
    }

    public static void getProjects(ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().getProjects().run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonProjects = jsonResponse.getJSONArray("projects");
                            listener.onGetProjectsResponse(jsonProjects);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onGetProjectsResponse(null);
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onGetProjectsResponse(null);
                    }
                }
        );
    }

//    TODO maybe
//    public static void getProject(String project Id, ServerRequestHandlerListener listener)

    public static void createProject(String projectName, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().createProject(projectName).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            JSONObject jsonProjects = jsonResponse.getJSONObject("project");
                            listener.onCreateProjectsResponse(
                                    message,
                                    jsonProjects
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onCreateProjectsResponse(
                                    "Unexpected error",
                                    null
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onCreateProjectsResponse(
                                "Unexpected error",
                                null
                        );
                    }
                }
        );
    }

    public static void updateProject(String projectId, String projectName, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().updateProject(projectId, projectName).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            JSONObject jsonProjects = jsonResponse.getJSONObject("project");
                            listener.onCreateProjectsResponse(
                                    message,
                                    jsonProjects
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onCreateProjectsResponse(
                                    "Unexpected error",
                                    null
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onCreateProjectsResponse(
                                "Unexpected error",
                                null
                        );
                    }
                }
        );
    }

    public static void deleteProject(String projectId, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().deleteProject(projectId).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onDeleteProjectsResponse(
                                    message,
                                    true
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onDeleteProjectsResponse(
                                    "Unexpected error",
                                    false
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onDeleteProjectsResponse(
                                "Unexpected error",
                                false
                        );
                    }
                }
        );
    }
}
