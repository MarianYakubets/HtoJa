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
            raw = raw.replace("title=\"Місто ", "XXY");
            raw = raw.replace("\">", "YXX");
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
                    "<tbody><tr>\n" +
                            "    <th><strong>Назва міста</strong></th>\n" +
                            "    <th><strong>Кількість населення, тис.&nbsp;осіб</strong></th>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=539\" title=\"Київ — столиця України\">Київ</a>&nbsp;**</td>\n" +
                            "    <td class=\"alignright\">2 602</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=1081\" title=\"Місто Харків\">Харків</a></td>\n" +
                            "    <td class=\"alignright\">1 470</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=1080\" title=\"Місто Дніпропетровськ\">Дніпропетровськ</a></td>\n" +
                            "    <td class=\"alignright\">1 064</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=1083\" title=\"Місто Одеса\">Одеса</a></td>\n" +
                            "    <td class=\"alignright\">1 029</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=1079\" title=\"Місто Донецьк\">Донецьк</a></td>\n" +
                            "    <td class=\"alignright\">1 016</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=432\" title=\"Місто Запоріжжя\">Запоріжжя</a></td>\n" +
                            "    <td class=\"alignright\">814</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=1084\" title=\"Місто Львів\">Львів</a></td>\n" +
                            "    <td class=\"alignright\">732</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=438\" title=\"Місто Кривий Ріг\">Кривий Ріг</a></td>\n" +
                            "    <td class=\"alignright\">667</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=433\" title=\"Місто Миколаїв\">Миколаїв</a></td>\n" +
                            "    <td class=\"alignright\">514</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=431\" title=\"Місто Маріуполь\">Маріуполь</a></td>\n" +
                            "    <td class=\"alignright\">492</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=431\" title=\"Місто Луганськ\">Луганськ</a></td>\n" +
                            "    <td class=\"alignright\">463</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\">Макіївка</td>\n" +
                            "    <td class=\"alignright\">390</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=1085\" title=\"Місто Вінниця\">Вінниця</a></td>\n" +
                            "    <td class=\"alignright\">357</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=433\" title=\"Місто Сімферополь\">Сімферополь</a></td>\n" +
                            "    <td class=\"alignright\">343</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=595\" title=\"Місто Севастополь\">Севастополь</a>&nbsp;**</td>\n" +
                            "    <td class=\"alignright\">341</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=433\" title=\"Місто Херсон\">Херсон</a></td>\n" +
                            "    <td class=\"alignright\">328</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=541\" title=\"Місто Полтава\">Полтава</a></td>\n" +
                            "    <td class=\"alignright\">318</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=540\" title=\"Місто Чернігів\">Чернігів</a></td>\n" +
                            "    <td class=\"alignright\">301</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=439\" title=\"Місто Черкаси\">Черкаси</a></td>\n" +
                            "    <td class=\"alignright\">295</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=430\" title=\"Місто Суми\">Суми</a></td>\n" +
                            "    <td class=\"alignright\">293</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=544\" title=\"Місто Горлівка\">Горлівка</a></td>\n" +
                            "    <td class=\"alignright\">292</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=540\" title=\"Місто Житомир\">Житомир</a></td>\n" +
                            "    <td class=\"alignright\">284</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\">Дніпродзержинськ</td>\n" +
                            "    <td class=\"alignright\">256</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=435\" title=\"Місто Хмельницький\">Хмельницький</a></td>\n" +
                            "    <td class=\"alignright\">254</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=439\" title=\"Місто Кіровоград\">Кіровоград</a></td>\n" +
                            "    <td class=\"alignright\">253</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=429\" title=\"Місто Рівне\">Рівне</a></td>\n" +
                            "    <td class=\"alignright\">249</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=436\" title=\"Місто Чернівці\">Чернівці</a></td>\n" +
                            "    <td class=\"alignright\">240</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=125\" title=\"Місто Кременчук\">Кременчук</a></td>\n" +
                            "    <td class=\"alignright\">234</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=435\" title=\"Місто Тернопіль\">Тернопіль</a></td>\n" +
                            "    <td class=\"alignright\">228</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=151\" title=\"Місто Івано-Франківськ\">Івано-Франківськ</a></td>\n" +
                            "    <td class=\"alignright\">218</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=147\" title=\"Місто Луцьк\">Луцьк</a></td>\n" +
                            "    <td class=\"alignright\">209</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\">Біла Церква</td>\n" +
                            "    <td class=\"alignright\">200</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=116\" title=\"Місто Краматорськ\">Краматорськ</a></td>\n" +
                            "    <td class=\"alignright\">181</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=121\" title=\"Місто Мелітополь\">Мелітополь</a></td>\n" +
                            "    <td class=\"alignright\">160</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <th><strong>Назва міста</strong></th>\n" +
                            "    <th><strong>Кількість населення, тис.&nbsp;осіб</strong></th>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\">Керч</td>\n" +
                            "    <td class=\"alignright\">157</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=543\" title=\"Місто Нікополь\">Нікополь</a></td>\n" +
                            "    <td class=\"alignright\">135</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=116\" title=\"Місто Слов'янськ\">Слов'янськ</a></td>\n" +
                            "    <td class=\"alignright\">125</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=121\" title=\"Місто Бердянськ\">Бердянськ</a></td>\n" +
                            "    <td class=\"alignright\">121</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\">Сєверодонецьк</td>\n" +
                            "    <td class=\"alignright\">120</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=117\" title=\"Місто Алчевськ\">Алчевськ</a></td>\n" +
                            "    <td class=\"alignright\">119</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=121\" title=\"Місто Павлоград\">Павлоград</a></td>\n" +
                            "    <td class=\"alignright\">119</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=436\" title=\"Місто Ужгород\">Ужгород</a></td>\n" +
                            "    <td class=\"alignright\">118</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=117\" title=\"Місто Лисичанськ\">Лисичанськ</a></td>\n" +
                            "    <td class=\"alignright\">115</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=140\" title=\"Місто Євпаторія\">Євпаторія</a></td>\n" +
                            "    <td class=\"alignright\">106</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=116\" title=\"Місто Єнакієве\">Єнакієве</a></td>\n" +
                            "    <td class=\"alignright\">104</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=143\" title=\"Місто Кам'янець-Подільський\">Кам'янець-Подільський</a></td>\n" +
                            "    <td class=\"alignright\">100</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\">Костянтинівка</td>\n" +
                            "    <td class=\"alignright\">95</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\">Красний Луч</td>\n" +
                            "    <td class=\"alignright\">95</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=135\" title=\"Місто Олександрія\">Олександрія</a></td>\n" +
                            "    <td class=\"alignright\">94</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=126\" title=\"Місто Конотоп\">Конотоп</a></td>\n" +
                            "    <td class=\"alignright\">93</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=117\" title=\"Місто Стаханов\">Стаханов</a></td>\n" +
                            "    <td class=\"alignright\">90</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=134\" title=\"Місто Умань\">Умань</a></td>\n" +
                            "    <td class=\"alignright\">89</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=132\" title=\"Місто Бердичів\">Бердичів</a></td>\n" +
                            "    <td class=\"alignright\">88</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=130\" title=\"Місто Бровари\">Бровари</a></td>\n" +
                            "    <td class=\"alignright\">87</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=126\" title=\"Місто Шостка\">Шостка</a></td>\n" +
                            "    <td class=\"alignright\">87</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=138\" title=\"Місто Ізмаїл – промисловий центр і порт на Дунаї\">Ізмаїл</a></td>\n" +
                            "    <td class=\"alignright\">84</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=116\" title=\"Місто Артемівськ\">Артемівськ</a></td>\n" +
                            "    <td class=\"alignright\">83</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=152\" title=\"Місто Мукачеве\">Мукачеве</a></td>\n" +
                            "    <td class=\"alignright\">82</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=140\" title=\"Місто Ялта\">Ялта</a></td>\n" +
                            "    <td class=\"alignright\">81</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=150\" title=\"Місто Дрогобич\">Дрогобич</a></td>\n" +
                            "    <td class=\"alignright\">79</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=131\" title=\"Місто Ніжин\">Ніжин</a></td>\n" +
                            "    <td class=\"alignright\">76</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=140\" title=\"Місто Феодосія\">Феодосія</a></td>\n" +
                            "    <td class=\"alignright\">74</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\">Свердловськ</td>\n" +
                            "    <td class=\"alignright\">73</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\">Торез</td>\n" +
                            "    <td class=\"alignright\">73</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=120\" title=\"Місто Новомосковськ\">Новомосковськ</a></td>\n" +
                            "    <td class=\"alignright\">72</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=150\" title=\"Місто Червоноград\">Червоноград</a></td>\n" +
                            "    <td class=\"alignright\">71</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=139\" title=\"Місто Первомайськ\">Первомайськ</a> (Миколаївська обл.)</td>\n" +
                            "    <td class=\"alignright\">70</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=542\" title=\"Місто Сміла\">Сміла</a></td>\n" +
                            "    <td class=\"alignright\">70</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <th><strong>Назва міста</strong></th>\n" +
                            "    <th><strong>Кількість населення, тис.&nbsp;осіб</strong></th>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\">Красноармійськ</td>\n" +
                            "    <td class=\"alignright\">69</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=151\" title=\"Місто Калуш\">Калуш</a></td>\n" +
                            "    <td class=\"alignright\">68</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=132\" title=\"Місто Коростень\">Коростень</a></td>\n" +
                            "    <td class=\"alignright\">67</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=542\" title=\"Місто Ковель\">Ковель</a></td>\n" +
                            "    <td class=\"alignright\">66</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=116\" title=\"Місто Дружківка\">Дружківка</a></td>\n" +
                            "    <td class=\"alignright\">65</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=131\" title=\"Місто Прилуки\">Прилуки</a></td>\n" +
                            "    <td class=\"alignright\">65</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=117\" title=\"Місто Рубіжне\">Рубіжне</a></td>\n" +
                            "    <td class=\"alignright\">65</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\">Антрацит</td>\n" +
                            "    <td class=\"alignright\">64</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\">Харцизьк</td>\n" +
                            "    <td class=\"alignright\">64</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=124\" title=\"Місто Лозова\">Лозова</a></td>\n" +
                            "    <td class=\"alignright\">63</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=150\" title=\"Місто Стрий\">Стрий</a></td>\n" +
                            "    <td class=\"alignright\">62</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\">Коломия</td>\n" +
                            "    <td class=\"alignright\">61</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\">Шахтарськ</td>\n" +
                            "    <td class=\"alignright\">60</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\">Сніжне</td>\n" +
                            "    <td class=\"alignright\">59</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\">Енергодар</td>\n" +
                            "    <td class=\"alignright\">56</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=124\" title=\"Місто Ізюм\">Ізюм</a></td>\n" +
                            "    <td class=\"alignright\">56</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\">Новоград-Волинський</td>\n" +
                            "    <td class=\"alignright\">56</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\">Брянка</td>\n" +
                            "    <td class=\"alignright\">55</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=130\" title=\"Місто Бориспіль\">Бориспіль</a></td>\n" +
                            "    <td class=\"alignright\">54</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\">Димитров</td>\n" +
                            "    <td class=\"alignright\">54</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=138\" title=\"Місто Іллічівськ\">Іллічівськ</a></td>\n" +
                            "    <td class=\"alignright\">54</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=147\" title=\"Місто Нововолинськ\">Нововолинськ</a></td>\n" +
                            "    <td class=\"alignright\">54</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\">Ровеньки</td>\n" +
                            "    <td class=\"alignright\">54</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=125\" title=\"Місто Лубни\">Лубни</a></td>\n" +
                            "    <td class=\"alignright\">53</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=550\" title=\"Білгород-Дністровський - 13 назв одного міста\">Білгород-Дністровський</a></td>\n" +
                            "    <td class=\"alignright\">52</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\">Комсомольськ</td>\n" +
                            "    <td class=\"alignright\">52</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\">Жовті Води</td>\n" +
                            "    <td class=\"alignright\">52</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=139\" title=\"Місто Нова Каховка\">Нова Каховка</a></td>\n" +
                            "    <td class=\"alignright\">52</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=130\" title=\"Місто Фастів\">Фастів</a></td>\n" +
                            "    <td class=\"alignright\">52</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\">Краснодон</td>\n" +
                            "    <td class=\"alignright\">51</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\"><a href=\"http://eduknigi.com/geo_view.php?id=121\" title=\"Місто Марганець\">Марганець</a></td>\n" +
                            "    <td class=\"alignright\">50</td>\n" +
                            "  </tr>\n" +
                            "  <tr class=\"odd\">\n" +
                            "    <td class=\"alignleft\"><a href=\"http://geoknigi.com/book_view.php?id=543\" title=\"Місто Охтирка\">Охтирка</a></td>\n" +
                            "    <td class=\"alignright\">50</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td class=\"alignleft\">Ромни</td>\n" +
                            "    <td class=\"alignright\">50</td>\n" +
                            "  </tr>\n" +
                            "</tbody>";

}
