package com.example.login.service;
import com.example.login.Model.Login;
import com.example.login.Model.Test;
import com.example.login.Model.User;
import com.example.login.data.Cursa;
import com.example.login.data.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserClient {
    @GET("info")
    Call<Test> info();

    @FormUrlEncoded
    @POST("loginJWT")
    Call<User> loginJWT(@FieldMap Map<String, String> login);

    @FormUrlEncoded
    @POST("registerJWT")
    Call<User> registerJWT(@FieldMap Map<String, String> register);

    @FormUrlEncoded
    @POST("curse")
    Call<ArrayList<Cursa>>  getRoutes(@FieldMap Map<String,String> req);

    @FormUrlEncoded
    @POST("mytickets")
    Call<ArrayList<Ticket>>  getTickets(@FieldMap Map<String,String> req);

    @FormUrlEncoded
    @POST("reservations")
    Call<List<Integer>> getReservationsSeats(@FieldMap Map<String,String> req);

    @FormUrlEncoded
    @POST("buyticket")
    Call<String> buyTicket(@FieldMap Map<String,String> req);
}
