package com.example.appdasfinal.httpRequests;

import android.os.AsyncTask;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
https://github.com/AnderRasoVazquez/android-project-manager/blob/master/app/src/main/java/com/example/projectmanager/utils/HttpRequest.java
*/
public class HttpRequest extends AsyncTask<Void, Void, Object[]> {

    public enum RequestMethod {
        GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");

        private final String requestMethod;

        RequestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
        }

        public String getValue() {
            return requestMethod;
        }
    }

    private String url;
    private OnConnectionSuccess onConnectionSuccess;
    private OnConnectionFailure onConnectionFailure;
    private RequestMethod method;
    private int statusCode;
    private HashMap<String, String> headers;
    private JSONObject body = null;

    private HttpRequest() { }

    @Override
    protected Object[] doInBackground(Void... voids) {
        try {
            HttpURLConnection connection = getHttpConnection();
            connection.connect();

            this.statusCode = connection.getResponseCode();

            InputStream inputStream;
            if (statusCode < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
            } else {
                inputStream = new BufferedInputStream(connection.getErrorStream());
            }
            String responseBody = convertInputStreamToString(inputStream);

            HashMap<String, String> responseHeaders = new HashMap<>();
            Map<String, List<String>> headers = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String key = entry.getKey();

                StringBuilder value = new StringBuilder();
                Iterator<String> iterator = entry.getValue().iterator();
                while (iterator.hasNext()) {
                    value.append(iterator.next());
                    if (iterator.hasNext()) {
                        value.append(", ");
                    }
                }

                responseHeaders.put(key, value.toString());
            }

            return new Object[]{responseBody, responseHeaders};
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private HttpURLConnection getHttpConnection() throws IOException {
        URL url = new URL(this.url);

        HttpURLConnection connection = (HttpURLConnection)
                                               url.openConnection();

        connection.setReadTimeout(30000);
        connection.setConnectTimeout(30000);

        // hashmap para poner las cabeceras
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        // si hay un json lo añade
        if (body != null) {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(body.toString());
            wr.flush();
            wr.close();
        }

        // poner el método después del cuerpo para que
        // no se sobreescriba a post automáticamente
        // por añadir un cuerpo
        connection.setRequestMethod(method.getValue());

        return connection;
    }

    @Override
    protected void onPostExecute(Object[] result) {
//        System.out.println(this.statusCode + "\n" + response);
        if (result != null) {
            String response = (String) result[0];
            HashMap<String, String> headers = (HashMap<String, String>) result[1];
            if (this.statusCode / 100 != 2) {
                onConnectionFailure.onFailure(this.statusCode, response, headers);
            } else {
                onConnectionSuccess.onSuccess(this.statusCode, response, headers);
            }
        } else {
            onConnectionFailure.onNoConnection();
        }
    }


    public static class Builder {

        private HttpRequest t = new HttpRequest();

        public Builder setUrl(String url) {
            t.url = url;
            return this;
        }

        public Builder setRequestMethod(RequestMethod method) {
            t.method = method;
            return this;
        }

        public Builder setBody(JSONObject body) {
            t.body = body;
            return this;
        }

        public Builder setHeaders(HashMap<String, String> headers) {
            t.headers = headers;
            return this;
        }

        public HttpRequest get() {
            return t;
        }

        public HttpRequest run(OnConnectionSuccess onConnectionSuccess, OnConnectionFailure onConnectionFailure) {
            t.onConnectionSuccess = onConnectionSuccess;
            t.onConnectionFailure = onConnectionFailure;
            t.execute();
            return t;
        }

        public Builder() {
        }
    }
}

