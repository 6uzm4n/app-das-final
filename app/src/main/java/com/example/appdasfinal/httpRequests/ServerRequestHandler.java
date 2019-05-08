package com.example.appdasfinal.httpRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ServerRequestHandler {

    public interface ServerRequestHandlerListener {

        /**
         * Called when the login request has been completed successfully.
         *
         * @param token User token returned by the server
         */
        default void onLoginSuccess(String token) {}

        /**
         * Called when the login request has failed.
         *
         * @param errorMessage Error message returned by the server
         */
        default void onLoginFailure(String errorMessage) {}

        /**
         * Called when the register request has been completed successfully.
         *
         * @param message Message returned by the server
         * @param userId  User id returned by the server
         */
        default void onRegisterSuccess(String message, String userId) {}

        /**
         * Called when the register request has failed.
         *
         * @param message Message returned by the server
         */
        default void onRegisterFailure(String message) {}

        /**
         * Called when the get projects request has been completed successfully.
         *
         * @param jsonProjects List of the projects returned by the server.
         *                     Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#obtener-todos-los-proyectos-del-usuario
         */
        default void onGetProjectsSuccess(JSONArray jsonProjects) {}

        /**
         * Called when the get projects request has failed.
         *
         * @param message List Message returned by the server
         */
        default void onGetProjectsFailure(String message) {}

        /**
         * Called when the get project request has been completed successfully.
         *
         * @param jsonProject Project returned by the server.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#obtener-un-proyecto
         */
        default void onGetProjectSuccess(JSONObject jsonProject) {}

        /**
         * Called when the get project request has failed.
         *
         * @param message List Message returned by the server
         */
        default void onGetProjectFailure(String message) {}

        /**
         * Called when the create project request has been completed successfully.
         *
         * @param message     Message returned by the server
         * @param jsonProject Project data returned by the server.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#crear-un-proyecto
         */
        default void onCreateProjectSuccess(String message, JSONObject jsonProject) {}

        /**
         * Called when the create project request has failed.
         *
         * @param message Message returned by the server
         */
        default void onCreateProjectFailure(String message) {}

        /**
         * Called when the update project request has been completed successfully.
         *
         * @param message     Message returned by the server
         * @param jsonProject Project data returned by the server.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#modificar-un-proyecto
         */
        default void onUpdateProjectSuccess(String message, JSONObject jsonProject) {}

        /**
         * Called when the update project request has failed.
         *
         * @param message Message returned by the server
         */
        default void onUpdateProjectFailure(String message) {}

        /**
         * Called when the update project request has been completed successfully.
         *
         * @param message Message returned by the server
         */
        default void onDeleteProjectSuccess(String message) {}

        /**
         * Called when the update project request has failed.
         *
         * @param message Message returned by the server
         */
        default void onDeleteProjectFailure(String message) {}

        /**
         * Called when the get requests request has been completed successfully.
         *
         * @param jsonRequests List of the requests returned by the server.
         *                     Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#obtener-todas-las-requests-de-un-proyecto
         */
        default void onGetRequestsSuccess(JSONArray jsonRequests) {}

        /**
         * Called when the get requests request has failed.
         *
         * @param message Message returned by the server
         */
        default void onGetRequestsFailure(String message) {}

        /**
         * Called when the get request request has been completed successfully.
         *
         * @param jsonRequest Request returned by the server.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#obtener-una-sola-request
         */
        default void onGetRequestSuccess(JSONObject jsonRequest) {}

        /**
         * Called when the get request request has failed.
         *
         * @param message Message returned by the server
         */
        default void onGetRequestFailure(String message) {}

        /**
         * Called when the create request request has been completed successfully.
         *
         * @param message     Message returned by the server
         * @param jsonRequest Project data returned by the server. Null if any problems occurred.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#crear-una-request
         */
        default void onCreateRequestSuccess(String message, JSONObject jsonRequest) {}

        /**
         * Called when the create request request has failed.
         *
         * @param message Message returned by the server
         */
        default void onCreateRequestFailure(String message) {}

        /**
         * Called when the update request request has been completed successfully.
         *
         * @param message     Message returned by the server
         * @param jsonRequest Project data returned by the server.
         *                    Structure: https://github.com/AnderRasoVazquez/api-das-final/wiki/Documentaci%C3%B3n-API-REST#modificar-una-request
         */
        default void onUpdateRequestSuccess(String message, JSONObject jsonRequest) {}

        /**
         * Called when the update request request has failed.
         *
         * @param message     Message returned by the server
         */
        default void onUpdateRequestFailure(String message) {}

        /**
         * Called when the update request request has been completed successfully.
         *
         * @param message Message returned by the server
         */
        default void onDeleteRequestSuccess(String message) {}

        /**
         * Called when the update request request has failed.
         *
         * @param message Message returned by the server
         */
        default void onDeleteRequestFailure(String message) {}

        /**
         * Called when the connection could no be established.
         */
        default void onNoConnection() {}
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
                            listener.onLoginSuccess(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onLoginFailure("Unexpected error");
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onLoginFailure(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onLoginFailure("Unexpected error");
                        }
                    }

                    @Override
                    public void onNoConnection() {
                        listener.onNoConnection();
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
                            listener.onRegisterSuccess(
                                    message,
                                    userId
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onRegisterFailure(
                                    "Unexpected error"
                            );
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
                            listener.onRegisterFailure(
                                    message
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onRegisterFailure(
                                    "Unexpected error"
                            );
                        }
                    }

                    @Override
                    public void onNoConnection() {
                        listener.onNoConnection();
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
                            listener.onGetProjectsSuccess(jsonProjects);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onGetProjectsFailure("Unexpected error");
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onGetProjectsFailure(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onGetProjectsFailure("Unexpected error");
                        }
                    }

                    @Override
                    public void onNoConnection() {
                        listener.onNoConnection();
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
                            listener.onGetProjectSuccess(
                                    jsonProject
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onGetProjectFailure("Unexpected error");
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onGetProjectFailure(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onGetProjectFailure("Unexpected error");
                        }
                    }

                    @Override
                    public void onNoConnection() {
                        listener.onNoConnection();
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
                            listener.onCreateProjectSuccess(
                                    message,
                                    jsonProject
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onCreateProjectFailure(
                                    "Unexpected error"
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onCreateProjectFailure(
                                    message
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onCreateProjectFailure(
                                    "Unexpected error"
                            );
                        }
                    }

                    @Override
                    public void onNoConnection() {
                        listener.onNoConnection();
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
                            listener.onUpdateProjectSuccess(
                                    message,
                                    jsonProject
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onUpdateProjectFailure(
                                    "Unexpected error"
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onUpdateProjectFailure(
                                    message
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onUpdateProjectFailure(
                                    "Unexpected error"
                            );
                        }
                    }

                    @Override
                    public void onNoConnection() {
                        listener.onNoConnection();
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
                            listener.onDeleteProjectSuccess(
                                    message
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onDeleteProjectFailure(
                                    "Unexpected error"
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onDeleteProjectFailure(
                                    message
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onDeleteProjectFailure(
                                    "Unexpected error"
                            );
                        }
                    }

                    @Override
                    public void onNoConnection() {
                        listener.onNoConnection();
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
                            listener.onGetRequestsSuccess(jsonRequests);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onGetRequestsFailure(
                                    "Unexpected error"
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onGetRequestsFailure(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onGetRequestsFailure(
                                    "Unexpected error"
                            );
                        }
                    }

                    @Override
                    public void onNoConnection() {
                        listener.onNoConnection();
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
                            listener.onGetRequestSuccess(
                                    jsonRequest
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onGetRequestFailure(
                                    "Unexpected error"
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onGetRequestFailure(
                                    message
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onGetRequestFailure(
                                    "Unexpected error"
                            );
                        }
                    }

                    @Override
                    public void onNoConnection() {
                        listener.onNoConnection();
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
                            listener.onCreateRequestSuccess(
                                    message,
                                    jsonRequest
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onCreateRequestFailure(
                                    "Unexpected error"
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onCreateRequestFailure(
                                    message
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onCreateRequestFailure(
                                    "Unexpected error"
                            );
                        }
                    }

                    @Override
                    public void onNoConnection() {
                        listener.onNoConnection();
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
                            listener.onUpdateRequestSuccess(
                                    message,
                                    jsonRequest
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onUpdateRequestFailure(
                                    "Unexpected error"
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onUpdateRequestFailure(
                                    message
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onUpdateRequestFailure(
                                    "Unexpected error"
                            );
                        }
                    }

                    @Override
                    public void onNoConnection() {
                        listener.onNoConnection();
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
                            listener.onDeleteRequestSuccess(
                                    message
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onDeleteRequestFailure(
                                    "Unexpected error"
                            );
                        }
                    }
                },
                new OnConnectionFailure() {
                    @Override
                    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
                        listener.onDeleteProjectFailure(
                                "Unexpected error"
                        );
                    }

                    @Override
                    public void onNoConnection() {
                        listener.onNoConnection();
                    }
                }
        );
    }

}
