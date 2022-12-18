package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.*;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    final private String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() throws IOException{
        //читает файл, парсит и возвращает результат
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("inputData.json").getFile());
        String json;
        try ( InputStream inFl = new BufferedInputStream(new FileInputStream(file))){
            json = new String(inFl.readAllBytes());
           // System.out.println(json);
        }

        var gson = new Gson();
        Measurement[] arrMea = gson.fromJson(json, Measurement[].class);
        return List.of(arrMea);
    }
}
