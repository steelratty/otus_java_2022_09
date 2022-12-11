
package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileSerializer implements Serializer {
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) throws JsonProcessingException {
        //отсортировать подойдет тримап с настройками по умолчанию
        var res = new TreeMap<String, Double>(data);
        String json = new ObjectMapper().writeValueAsString(res);
        try (var bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
