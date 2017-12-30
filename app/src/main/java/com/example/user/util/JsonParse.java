package com.example.user.util;

import java.util.List;

import com.example.user.model.Dish;
import com.example.user.model.Order;
import com.example.user.model.OrderItem;
import com.example.user.model.Shop;
import com.example.user.model.Types;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;



/**
 * Created by user on 2017/12/12.
 */
public class JsonParse {
    public static List<Dish> getDish(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Dish>>(){}.getType();
        List<Dish> list = gson.fromJson(json, listType);
        return list;
    }

    public static List<Shop> getShop(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Shop>>(){}.getType();
        List<Shop> list = gson.fromJson(json, listType);
        return list;
    }

    public static List<Order> getOrder(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Order>>(){}.getType();
        List<Order> list = gson.fromJson(json, listType);
        return list;
    }

    public static List<Types> getTypes(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Types>>(){}.getType();
        List<Types> list = gson.fromJson(json, listType);
        return list;
    }

    public static JsonArray  getDishTOjson(List<Dish> list) {
        JsonArray  jsonArray = new Gson().toJsonTree(list, new TypeToken<List<Dish>>() {}.getType()).getAsJsonArray();
        return jsonArray;
    }

    public static JsonArray  getOrderItemTOjson(List<OrderItem> list) {
        JsonArray  jsonArray = new Gson().toJsonTree(list, new TypeToken<List<OrderItem>>() {}.getType()).getAsJsonArray();
        return jsonArray;
    }
}
