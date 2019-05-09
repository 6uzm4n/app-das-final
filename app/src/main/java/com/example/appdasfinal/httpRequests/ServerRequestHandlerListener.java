package com.example.appdasfinal.httpRequests;

import org.json.JSONArray;
import org.json.JSONObject;

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
     * @param message Error message returned by the server
     */
    default void onLoginFailure(String message) {}

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
     * @param message Message returned by the server
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