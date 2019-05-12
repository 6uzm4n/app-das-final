package com.example.appdasfinal.httpRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ServerRequestHandler {

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

    public static void login(String authString, ServerRequestHandlerListener listener) {
        login(authString, true, listener);
    }

    public static void login(String authString, boolean saveToken, ServerRequestHandlerListener listener) {
        HTTPRequestSender.getInstance().login(authString).run(
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
     * Any null parameter will be ignored and not modified.
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
