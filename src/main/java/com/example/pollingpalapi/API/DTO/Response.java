package com.example.pollingpalapi.API.DTO;

public class Response<T> {
    public int http;
    public T res;

    public Response(int http, T res) {
        this.http = http;
        this.res = res;
    }
}
