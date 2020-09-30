package com.java.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.java.Main;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class PlayerJSON {

    private File file;
    private Gson gson;
    private FileWriter writer;
    private FileReader reader;
    private JSONParser parser;

    public PlayerJSON(String filepath, String defaultJson) {
        file = new File(filepath);
        gson = new GsonBuilder().create();
        parser = new JSONParser();
        try {
            writer = new FileWriter(filepath);
            reader = new FileReader(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
                defaultFile(defaultJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void defaultFile(String json) {
        gson.toJson("{\n" +
                "\t\"Username\":\"\",\n" +
                "\t\"Last Seen\":0,\n" +
                "\t\"Current Class\":\"\",\n" +
                "\t\"Cooldowns\":[],\n" +
                "\t\"IdleSlot\":0,\n" +
                "\t\"SendExp\":true,\n" +
                "\t\"ToggleOffhand\":true,\n" +
                "\t\"ClassData\":{\n" +
                "\t}\n" +
                "}");
    }

    public Gson getGson() {
        return gson;
    }

    public File getFile() {
        return file;
    }

    public String getFullJson() {
        return reader.toString();
    }

    public void setString(String key, String value) {
        gson.toJson("{\"" + key + "\":\"" + value + "\"}", writer);
        //this wont work because it will override
    }

    public String getString(String key) {
        try {
            JSONObject jObject = (JSONObject) parser.parse(reader);
            return (String) jObject.get(key);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null; //remove later
    }

    public boolean containsKey(String key) {
        return (getString(key) != null);
    }

    public void save() {
        try {
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
