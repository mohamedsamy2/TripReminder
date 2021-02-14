package com.example.tripreminder.Dao.RoomDao;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    @TypeConverter
    public String toString(List<String> list){
        return new Gson().toJson(list);
    }


    @TypeConverter
    public List<String> toArrayList(String gson){
        return new Gson().fromJson(gson,ArrayList.class);
    }




}
