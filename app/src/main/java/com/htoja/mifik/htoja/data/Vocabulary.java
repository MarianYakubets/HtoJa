package com.htoja.mifik.htoja.data;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by marian on 11.04.17.
 */

public class Vocabulary {
    private static final Vocabulary ourInstance = new Vocabulary();
    private Map<String, List<String>> categories;

    public static Vocabulary getInstance() {
        return ourInstance;
    }

    private Vocabulary() {

    }

    private static String loadJSONFromAsset(Context ctx) {
        String json;
        try {
            InputStream is = ctx.getAssets().open("vocabulary.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void readJson(Context ctx) {
        ourInstance.categories = new HashMap<>();
        Type listType = new TypeToken<ArrayList<Category>>() {
        }.getType();

        List<Category> categories = new GsonBuilder().create().fromJson(loadJSONFromAsset(ctx), listType);
        for (Category category : categories) {
            ourInstance.categories.put(category.getName(), category.getWords());
        }
    }

    public List<String> getWordsForCategories(String... names) {
        List<String> words = new ArrayList<>();
        for (String name : names) {
            words.addAll(categories.get(name));
        }
        return words;
    }

    public static Vocabulary getOurInstance() {
        return ourInstance;
    }

    public List<String> getCategories() {
        Set<String> set = categories.keySet();
        List<String> list = new ArrayList<>(set.size());
        list.addAll(set);
        return list;
    }

    public int getSizeOfCategory(String name) {
        return categories.get(name).size();
    }


    private class Category {
        @SerializedName("name")
        private String name;
        @SerializedName("words")
        private List<String> words;

        public Category() {
        }

        public Category(String name, List<String> words) {
            this.name = name;
            this.words = words;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getWords() {
            return words;
        }

        public void setWords(List<String> words) {
            this.words = words;
        }
    }

}
