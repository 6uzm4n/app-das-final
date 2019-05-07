package com.example.appdasfinal.httpRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
         * Called when the get project request has been completed.
         *
         * @param jsonProject Project returned by the server. Null if any problems occurred.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#obtener-un-proyecto
         */
        default void onGetProjectResponse(JSONObject jsonProject) {}

        /**
         * Called when the create project request has been completed.
         *
         * @param message     Message returned by the server. Indicates success of cause of error
         * @param jsonProject Project data returned by the server. Null if any problems occurred.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#crear-un-proyecto
         */
        default void onCreateProjectResponse(String message, JSONObject jsonProject) {}

        /**
         * Called when the update project request has been completed.
         *
         * @param message     Message returned by the server. Indicates success of cause of error
         * @param jsonProject Project data returned by the server. Null if any problems occurred.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#modificar-un-proyecto
         */
        default void onUpdateProjectResponse(String message, JSONObject jsonProject) {}

        /**
         * Called when the update project request has been completed.
         *
         * @param message Message returned by the server. Indicates success of cause of error
         * @param success Boolean indicating the success of the operation
         */
        default void onDeleteProjectResponse(String message, boolean success) {}

        /**
         * Called when the get requests request has been completed.
         *
         * @param jsonRequests List of the requests returned by the server. Null if any problems occurred.
         *                     Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#obtener-todas-las-requests-de-un-proyecto
         */
        default void onGetRequestsResponse(JSONArray jsonRequests) {}

        /**
         * Called when the get request request has been completed.
         *
         * @param jsonRequest Request returned by the server. Null if any problems occurred.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#obtener-una-sola-request
         */
        default void onGetRequestResponse(JSONObject jsonRequest) {}

        /**
         * Called when the create request request has been completed.
         *
         * @param message     Message returned by the server. Indicates success of cause of error
         * @param jsonRequest Project data returned by the server. Null if any problems occurred.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#crear-una-request
         */
        default void onCreateRequestResponse(String message, JSONObject jsonRequest) {}

        /**
         * Called when the update request request has been completed.
         *
         * @param message     Message returned by the server. Indicates success of cause of error
         * @param jsonRequest Project data returned by the server. Null if any problems occurred.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#modificar-una-request
         */
        default void onUpdateRequestResponse(String message, JSONObject jsonRequest) {}

        /**
         * Called when the update request request has been completed.
         *
         * @param message Message returned by the server. Indicates success of cause of error
         * @param success Boolean indicating the success of the operation
         */
        default void onDeleteRequestResponse(String message, boolean success) {}
    }

    public static void login(String email, String password, ServerRequestHandlerListener listener) {
        login(email, password, true, listener);
    }

    /**
     * Tries to log in using and email and password.
     * If successful, the response will contain the user token to
     * use in future requests:
     * <p>
     * https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#login
     *
     * @param email     User email
     * @param password  User password
     * @param saveToken Flag to indicate whether the token received should be
     *                  stored automatically
     * @param listener  Listener not notify of the result
     */
    public static void login(String email, String password, boolean saveToken, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().login(email, password).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
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
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onLoginResponse(null);
                    }
                }
        );
    }

    /**
     * Tries to register a new user.
     * If successful, the response will contain information
     * about the user.
     * <p>
     * https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#registrarse
     *
     * @param email    User email
     * @param password User password
     * @param listener Listener not notify of the result
     */
    public static void register(String email, String password, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().register(email, password).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
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
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
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

    /**
     * Tries to get all the projects from the user whose
     * id token has been stored.
     * If successful, the response will contain information
     * about the user's projects.
     * <p>
     * https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#obtener-todos-los-proyectos-del-usuario
     *
     * @param listener Listener not notify of the result
     */
    public static void getProjects(ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().getProjects().run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
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
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onGetProjectsResponse(null);
                    }
                }
        );
    }

    /**
     * Tries to get a project from the user whose
     * id token has been stored.
     * If successful, the response will contain information
     * about the project.
     * <p>
     * https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#obtener-un-proyecto
     *
     * @param projectId Id of the project
     * @param listener  Listener not notify of the result
     */
    public static void getProject(String projectId, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().getProject(projectId).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject jsonProject = jsonResponse.getJSONObject("project");
                            listener.onGetProjectResponse(
                                    jsonProject
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onGetProjectResponse(
                                    null
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onGetProjectResponse(
                                null
                        );
                    }
                }
        );
    }

    /**
     * Tries to create a project for the user whose
     * id token has been stored.
     * If successful, the response will contain information
     * about the project.
     * <p>
     * https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#crear-un-proyecto
     *
     * @param projectName Project name
     * @param listener    Listener not notify of the result
     */
    public static void createProject(String projectName, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().createProject(projectName).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            JSONObject jsonProject = jsonResponse.getJSONObject("project");
                            listener.onCreateProjectResponse(
                                    message,
                                    jsonProject
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onCreateProjectResponse(
                                    "Unexpected error",
                                    null
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onCreateProjectResponse(
                                "Unexpected error",
                                null
                        );
                    }
                }
        );
    }

    /**
     * Tries to change the name a project of the user whose
     * id token has been stored.
     * If successful, the response will contain information
     * about the project.
     * <p>
     * https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#modificar-un-proyecto
     *
     * @param projectId   Project id
     * @param projectName New project name
     * @param listener    Listener not notify of the result
     */
    public static void updateProject(String projectId, String projectName, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().updateProject(projectId, projectName).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            JSONObject jsonProject = jsonResponse.getJSONObject("project");
                            listener.onUpdateProjectResponse(
                                    message,
                                    jsonProject
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onUpdateProjectResponse(
                                    "Unexpected error",
                                    null
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onUpdateProjectResponse(
                                "Unexpected error",
                                null
                        );
                    }
                }
        );
    }

    /**
     * Tries to delete a project of the user whose
     * id token has been stored.
     * <p>
     * https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#eliminar-un-proyecto
     *
     * @param projectId Project id
     * @param listener  Listener not notify of the result
     */
    public static void deleteProject(String projectId, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().deleteProject(projectId).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onDeleteProjectResponse(
                                    message,
                                    true
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onDeleteProjectResponse(
                                    "Unexpected error",
                                    false
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onDeleteProjectResponse(
                                "Unexpected error",
                                false
                        );
                    }
                }
        );
    }

    /**
     * Tries to get all the requests of a given project.
     * If successful, the response will contain information
     * about the project's requests.
     * <p>
     * https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#obtener-todas-las-requests-de-un-proyecto
     *
     * @param projectId Project id
     * @param listener  Listener not notify of the result
     */
    public static void getRequests(String projectId, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().getRequests(projectId).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonRequests = jsonResponse.getJSONArray("requests");
                            listener.onGetRequestsResponse(jsonRequests);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onGetRequestsResponse(null);
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onGetRequestsResponse(null);
                    }
                }
        );
    }

    /**
     * Tries to get a specific request.
     * If successful, the response will contain information
     * about the request.
     * <p>
     * https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#obtener-una-sola-request
     *
     * @param requestId Request id
     * @param listener  Listener not notify of the result
     */
    public static void getRequest(String requestId, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().getRequest(requestId).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject jsonRequest = jsonResponse.getJSONObject("request");
                            listener.onGetRequestResponse(
                                    jsonRequest
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onGetRequestResponse(
                                    null
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onGetRequestResponse(
                                null
                        );
                    }
                }
        );
    }

    /**
     * Tries to create an empty request for a given project.
     * If successful, the response will contain information
     * about the request.
     * <p>
     * https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#crear-una-request
     *
     * @param projectId   Project id
     * @param requestName Request name
     * @param listener    Listener not notify of the result
     */
    public static void createRequest(String projectId, String requestName, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().createRequest(projectId, requestName).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            JSONObject jsonRequest = jsonResponse.getJSONObject("request");
                            listener.onCreateRequestResponse(
                                    message,
                                    jsonRequest
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onCreateRequestResponse(
                                    "Unexpected error",
                                    null
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onCreateRequestResponse(
                                "Unexpected error",
                                null
                        );
                    }
                }
        );
    }

    /**
     * Tries to update the fields of a given request.
     * If successful, the response will contain information
     * about the request.
     * <p>
     * https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#modificar-una-request
     *
     * @param requestId      Request id
     * @param requestName    New request name
     * @param requestURL     New request URL
     * @param requestBody    New request body in a json-formatted String
     * @param requestMethod  New request method
     * @param requestHeaders New request headers in key-value pairs
     * @param listener       Listener not notify of the result
     */
    public static void updateRequest(String requestId,
                                     String requestName,
                                     String requestURL,
                                     String requestBody,
                                     String requestMethod,
                                     HashMap<String, String> requestHeaders,
                                     ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().updateRequest(
                requestId,
                requestName,
                requestURL,
                requestBody,
                requestMethod,
                requestHeaders
        ).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            JSONObject jsonRequest = jsonResponse.getJSONObject("request");
                            listener.onUpdateRequestResponse(
                                    message,
                                    jsonRequest
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onUpdateRequestResponse(
                                    "Unexpected error",
                                    null
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onUpdateRequestResponse(
                                "Unexpected error",
                                null
                        );
                    }
                }
        );
    }

    /**
     * Tries to delete a request of the user whose
     * id token has been stored.
     * <p>
     * https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#eliminar-una-request
     *
     * @param requestId Request id
     * @param listener  Listener not notify of the result
     */
    public static void deleteRequest(String requestId, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().deleteRequest(requestId).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onDeleteProjectResponse(
                                    message,
                                    true
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onDeleteProjectResponse(
                                    "Unexpected error",
                                    false
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onDeleteProjectResponse(
                                "Unexpected error",
                                false
                        );
                    }
                }
        );
    }

    public static void customRequest(String url, String body, String method, HashMap<String, String> headers, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().customRequest(url, body, method, headers).run(
                new OnConnectionSuccess() {
                    @Override
                    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onDeleteProjectResponse(
                                    message,
                                    true
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onDeleteProjectResponse(
                                    "Unexpected error",
                                    false
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        System.out.println(statusCode);
                        System.out.println(response);
                        listener.onDeleteProjectResponse(
                                "Unexpected error",
                                false
                        );
                    }
                }
        );
    }
}
