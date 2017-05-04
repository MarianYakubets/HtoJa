package com.htoja.mifik.htoja.data;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by marian on 11.04.17.
 */

public class Vocabulary {
    private static final Vocabulary ourInstance = new Vocabulary();
    private static final String TAG = String.valueOf(Vocabulary.class);
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


    public static void readFirebase(Context ctx) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("vocabulary");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String name = (String) messageSnapshot.child("name").getValue();
                    List<String> words = (List<String>) messageSnapshot.child("words").getValue();
                    if (ourInstance.categories.containsKey(name) && words != null) {
                        ourInstance.categories.put(name, words);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }


    public static void readRawData() {
        try {
            raw = raw.replace("* [[", "XXY");
            raw = raw.replace("]]", "YXX");
            Scanner sc = new Scanner(raw);
            Pattern MY_PATTERN = Pattern.compile("XXY.*YXX");

            String all = "";

            Matcher m = MY_PATTERN.matcher(raw);
            while (m.find()) {
                String s = m.group();
                s = s.replace("XXY", "\"");
                s = s.replace("YXX", "\"");
                all += s + ", \n";
            }
            Log.d("JSON", all);
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
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

    private static String raw =
            "* [[Авіамодельний спорт]]\n" +
                    "* [[Автомобільний спорт]]\n" +
                    "* [[Автомодельний спорт]]\n" +
                    "* [[Айкідо]]\n" +
                    "* [[Аквабайк]]\n" +
                    "* [[Акробатичний рок-н-рол]]\n" +
                    "* [[Альпінізм]]\n" +
                    "* [[Американський футбол]]\n" +
                    "* [[Армреслінг|Армспорт]]\n" +
                    "* [[Багатоборство тілоохоронців]]\n" +
                    "* [[Більярдний спорт]]\n" +
                    "* [[Богатирське багатоборство]]\n" +
                    "* [[Бодібілдинг]]\n" +
                    "* [[Бойове самбо]]\n" +
                    "* [[Куреш|Боротьба Кураш]]\n" +
                    "* [[Боротьба на поясах]]\n" +
                    "* [[Боротьба на поясах «Алиш»|Боротьба на поясах Алиш]]\n" +
                    "* [[самбо|Боротьба самбо]]\n" +
                    "* [[Боулінг]]\n" +
                    "* [[Вейкбординг]]\n" +
                    "* [[Вертолітний спорт]]\n" +
                    "* [[Веслування на човнах «Дракон»]]\n" +
                    "* [[Військово-спортивні багатоборства]]\n" +
                    "* [[Водні лижі|Воднолижний спорт]]\n" +
                    "* [[Водно-моторний спорт]]\n" +
                    "* [[Гирьовий спорт]]\n" +
                    "* [[Ґо (гра)|Го]]\n" +
                    "* [[Годзю-рю карате]]\n" +
                    "* [[Голубиний спорт]]\n" +
                    "* [[Городковий спорт]]\n" +
                    "* [[Дартс]]\n" +
                    "* [[Дельтапланерний спорт]]\n" +
                    "* [[Джиу-джитсу]]\n" +
                    "* [[Естетична гімнастика]]\n" +
                    "* [[Мішані бойові мистецтва|Змішані єдиноборства (ММА)]]\n" +
                    "* Карате JKA WF\n" +
                    "* Карате JKS\n" +
                    "* Карате WKC\n" +
                    "* [[Карате]] \n" +
                    "* Кікбоксинг WKA\n" +
                    "* Кікбоксинг WPKA\n" +
                    "* Кікбоксинг WАКО\n" +
                    "* Кікбоксинг ВТКА\n" +
                    "* Кіокушин карате\n" +
                    "* Кіокушинкай карате\n" +
                    "* Кіокушинкайкан карате\n" +
                    "* [[Козацький двобій]]\n" +
                    "* Комбат Дзю-Дзюцу\n" +
                    "* Косіки карате\n" +
                    "* [[Кунгфу]]\n" +
                    "* [[Літаковий спорт]]\n" +
                    "* [[Міні-гольф]]\n" +
                    "* [[Морські багатоборства]]\n" +
                    "* [[Мотоциклетний спорт]]\n" +
                    "* [[Панкратіон]]\n" +
                    "* [[Парапланеризм|Парапланерний спорт]]\n" +
                    "* [[Парашутний спорт]]\n" +
                    "* [[Пауерліфтинг]]\n" +
                    "* [[Пейнтбол]]\n" +
                    "* [[Перетягування канату]]\n" +
                    "* [[Петанк]]\n" +
                    "* [[Підводний спорт]]\n" +
                    "* [[Планерний спорт]]\n" +
                    "* [[Пляжний гандбол]]\n" +
                    "* [[Пляжний футбол]]\n" +
                    "* [[Повітроплавальний спорт]]\n" +
                    "* [[Пожежно-прикладний спорт]]\n" +
                    "* [[Поліатлон]]\n" +
                    "* [[Стрільба з блочного лука|Практична стрільба]]\n" +
                    "* [[Професійний бокс]]\n" +
                    "* [[Радіоспорт]]\n" +
                    "* [[Ракетомодельний спорт]]\n" +
                    "* [[Регбіліг]]\n" +
                    "* [[Рибальський спорт|Риболовний спорт]]\n" +
                    "* [[Роликовий спорт]]\n" +
                    "* [[Рукопашний бій]]\n" +
                    "* [[Сквош]]\n" +
                    "* [[Скелелазіння]]\n" +
                    "* [[Спорт із собаками]]\n" +
                    "* [[Спорт надлегких літальних апаратів]]\n" +
                    "* [[Спортивна аеробіка]]\n" +
                    "* [[Спортивна акробатика]]\n" +
                    "* [[Спортивне орієнтування]]\n" +
                    "* [[Бридж|Спортивний бридж]]\n" +
                    "* [[Спортивний туризм]]\n" +
                    "* [[Спортивні танці]]\n" +
                    "* [[Спортінг]]\n" +
                    "* [[Стронгмен]]\n" +
                    "* [[Судномодельний спорт]]\n" +
                    "* [[Сумо]]\n" +
                    "* Таеквондо (ІТФ)\n" +
                    "* [[Муай-тай|Таїландський бокс Муей Тай]]\n" +
                    "* [[Танцювальний спорт]]\n" +
                    "* [[Традиційне карате]]\n" +
                    "* [[Українська боротьба на поясах]]\n" +
                    "* [[Спас (бойовий звичай)|Український рукопаш «Спас»]]\n" +
                    "* [[Універсальний бій]]\n" +
                    "* [[Ушу]]\n" +
                    "* [[Фітнес]]\n" +
                    "* [[Флорбол]]\n" +
                    "* [[Французький бокс Сават]]\n" +
                    "* [[Фрі-файт]]\n" +
                    "* [[Фунакоші шотокан карате]]\n" +
                    "* [[Футзал]]\n" +
                    "* [[Хортинг]]\n" +
                    "* [[Черлідинг]]\n" +
                    "* [[Шахи]]\n" +
                    "* [[Шашки]]";

}
