package com.databaseproj.caltracker.view;

import okhttp3.Response;

public class GlobalClass {
    private static Response response;

    public static Response getResponse() {
        Response result = response;
        response = null;
        return result;
    }
    public static void setResponse(Response aResponse) {
        response = aResponse;
    }
    public static boolean isNull() {
        return response == null;
    }
}
