package com.labsfunware.tsp.api;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by james on 8/6/14.
 */
public interface TSPClient {

    public static final String API_ENDPOINT = "https://agent.electricimp.com/";

    @GET("/vfR-e76MntZb")
    public void getStatus(Callback<BoardStatus> cb);

}
