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
            raw = raw.replace("||[[", "XXY");
            raw = raw.replace("]]||", "YXX");
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

    private static String raw = "<s><s>₴₴₴</s></s>== Станом на 12 січня 2015 року ==\n" +
            "{| class=\"sortable\" border=\"0\" cellspacing=\"1\" cellpadding=\"2\" style=\"border:1px solid #CCCCCC;\"\n" +
            "|- bgcolor=\"#a9a9FF\"\n" +
            "!Позиція!!Фільм!!Оригінальна назва!!Рік!!Рейтинг!!Режисер\n" +
            "|- bgcolor=\"#a0ffa0\"\n" +
            "|1||[[Втеча з Шоушенка]]||''The Shawshank Redemption''||1994||9,2||[[Френк Дарабонт]]\n" +
            "|- bgcolor=\"#a0ffa0\"\n" +
            "|2||[[Хрещений батько (фільм)|Хрещений батько]]||''The Godfather''||1972||9,2||[[Френсіс Форд Коппола]]\n" +
            "|- bgcolor=\"#a0ffa0\"\n" +
            "|3||[[Хрещений батько 2]]||''The Godfather: Part II''||1974||9,0||[[Френсіс Форд Коппола]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|4||[[Темний лицар]]||''The Dark Knight''||2008||8,9||[[Крістофер Нолан]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|5||[[Кримінальне чтиво]]||''Pulp Fiction''||1994||8,9||[[Квентін Тарантіно]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|6||[[12 розгніваних чоловіків]]||''12 Angry Men''||1957||8,9||[[Сідні Люмет]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|7||[[Список Шиндлера]]||''Schindler's List''||1993||8,9||[[Стівен Спілберг]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|8||[[Хороший, поганий, злий]]||''Il buono, il brutto, il cattivo''||1966||8,9||[[Серджо Леоне]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|9||[[Володар Перснів: Повернення короля]]||''The Lord of the Rings: The Return of the King''||2003||8,9||[[Пітер Джексон]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|10||[[Бійцівський клуб (фільм)|Бійцівський клуб]]||''Fight Club''||1999||8,8||[[Девід Фінчер]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|11||[[Володар Перснів: Братерство Персня]]||''The Lord of the Rings: The Fellowship of the Ring''||2001||8,8||[[Пітер Джексон]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|12||[[Зоряні війни. Епізод V. Імперія завдає удару у відповідь]]||''Star Wars: Episode V&nbsp;— The Empire Strikes Back''||1980||8,8||[[Ірвін Кершнер]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|13||[[Форрест Ґамп]]||''Forrest Gump''||1994||8,7||[[Роберт Земекіс]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|14||[[Початок (фільм, 2010)|Початок]]||''Inception''||2010||8,7||[[Крістофер Нолан]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|15||[[Пролітаючи над гніздом зозулі]]||''One Flew Over the Cuckoo's Nest''||1975||8,7||[[Мілош Форман]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|16||[[Володар Перснів: Дві вежі]]||''The Lord of the Rings: The Two Towers''||2002||8,7||[[Пітер Джексон]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|17||[[Славні хлопці]]||''Goodfellas''||1990||8,7||[[Мартін Скорсезе]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|18||[[Матриця (фільм)|Матриця]]||''The Matrix''||1999||8,7||[[брати Ваховські]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|19||[[Зоряні війни. Епізод IV. Нова надія]]||''Star Wars: Episode IV&nbsp;— A New Hope''||1977||8,7||[[Джордж Лукас]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|20||[[Сім самураїв]]||七人の侍||1954||8,7||[[Акіра Куросава]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|21||[[Місто Бога]]||''City of God''||2002||8,7||Фернанду Мейрелліш\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|22||[[Сім (фільм)|Сім]]||''Se7en''||1995||8,6||[[Девід Фінчер]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|23||[[Мовчання ягнят]]||''The Silence of the Lambs''||1991||8,6||[[Джонатан Деммі]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|24||[[Звичайні підозрювані]]||''The Usual Suspects''||1995||8,6||[[Браян Сінґер]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|25||[[Це дивовижне життя]]||''It's a Wonderful Life''||1946||8,6||[[Френк Капра]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|26||[[Життя прекрасне (фільм, 1997)|Життя прекрасне]]||''Life Is Beautiful''||1997||8,6||[[Роберто Беніньї]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|27||[[Леон (фільм)|Леон]]||''Léon''||1994||8,6||[[Люк Бессон]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|28||[[Якось на Дикому Заході]]||''Once Upon a Time in the West''||1968||8,6||[[Серджіо Леоне]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|29||[[Інтерстеллар]]||''Interstellar''||2014||8,7||[[Крістофер Нолан]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|30||[[Врятувати рядового Раяна]]||''Saving Private Ryan''||1998||8,5||[[Стівен Спілберг]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|31||[[Касабланка (фільм)|Касабланка]]||''Casablanca''||1942||8,6||[[Майкл Кертіс]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|32||[[Американська історія Ікс]]||''American History X''||1998||8,5||[[Тоні Кей (режисер)|Тоні Кей]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|33||[[Сен та Чіхіро в полоні у духів]]||''Spirited Away''||2001||8,5||[[Міядзакі Хаяо]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|34||[[Вогні великого міста]]||''City Lights''||1931||8,5||[[Чарлі Чаплін]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|35||[[Індіана Джонс: У пошуках втраченого ковчега]]||''Raiders of the Lost Ark''||1981||8,6||[[Стівен Спілберг]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|36||[[Психо (фільм, 1960)|Психо]]||''Psycho''||1960||8,5||[[Альфред Хічкок]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|37||[[Вікно у двір]]||''Rear Window''||1954||8,5||[[Альфред Хічкок]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|38||[[Недоторканні (фільм, 2011)|Недоторканні]]||''Intouchables''||2011||8,5||[[Олів'є Накаш]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|39||[[Нові часи]]||''Modern Times''||1936||8,5||[[Чарлі Чаплін]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|40||[[Одержимість (фільм, 2014)|Одержимість]]||''Whiplash''||2014||8,5||Дам'єн Шазель\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|41||[[Термінатор 2: Судний день]]||''Terminator 2: Judgment Day''||1991||8,5||[[Джеймс Камерон]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|42||[[Зелена миля (фільм)|Зелена миля]]||''The Green Mile''||1999||8,5||[[Френк Дарабонт]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|43||[[Піаніст (фільм)|Піаніст]]||''The Pianist''||2002||8,5||[[Роман Полянський]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|44||[[Пам'ятай (фільм)|Пам'ятай]]||''Memento''||2000||8,5||[[Крістофер Нолан]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|45||[[Відступники]]||''The Departed''||2006||8,5||[[Мартін Скорсезе]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|46||[[Гладіатор (фільм)|Гладіатор]]||''Gladiator''||2000||8,5||[[Рідлі Скотт]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|47||[[Апокаліпсис сьогодні]]||''Apocalypse Now''||1979||8,5||[[Френсіс Форд Коппола]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|48||[[Назад у майбутнє]]||''Back to the Future''||1985||8,5||[[Роберт Земекіс]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|49||[[Бульвар Сансет (фільм)|Бульвар Сансет]]||''Sunset Blvd.''||1950||8,5||[[Біллі Вайлдер]]\n" +
            "|- bgcolor=\"#bbffbb\"\n" +
            "|50||[[Доктор Стрейнджлав, або Як я навчився не хвилюватися й полюбив атомну бомбу]]||''Dr. Strangelove or: How I Learned to Stop Worrying and Love the Bomb''||1964||8,5||[[Стенлі Кубрик]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|51||[[Думками навиворіт]]||''Inside Out''||2015||8,5||[[Піт Доктер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|52||[[Престиж (фільм)|Престиж]]||''The Prestige''||2006||8,4||[[Крістофер Нолан]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|53||[[Чужий (фільм)|Чужий]]||''Alien''||1979||8,5||[[Рідлі Скотт]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|54||[[Король Лев]]||''The Lion King''||1994||8,2||Роджер Аллерс, Роб Мінкофф\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|55||[[Життя інших]]||''The Lives of Others''||2006||8,4||Флоріан Хенкель фон Доннерсмарк\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|56||[[Великий диктатор]]||''The Great Dictator''||1940||8,4||[[Чарлі Чаплін]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|57||[[Новий кінотеатр «Парадізо»]]||''Cinema Paradiso''||1988||8,4||[[Джузеппе Торнаторе]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|58||[[Сяйво (фільм)|Сяйво]]||''The Shining''||1980||8,4||[[Стенлі Кубрик]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|59||[[Джанґо вільний]]||''Django Unchained''||2012||8,4||[[Квентін Тарантіно]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|60||[[Шляхи слави]]||''Paths of Glory''||1957||8,4||[[Стенлі Кубрик]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|61||[[Темний лицар повертається]]||The Dark Knight Rises||2012||8,8||[[Крістофер Нолан]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|62||[[ВОЛЛ-І]]||''WALL·E''||2008||8,4||[[Ендрю Стентон]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|63||[[Краса по-американськи]]||''American Beauty''||1999||8,4||Сем Мендес\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|64||[[Могила світлячків]]||''Grave of the Fireflies''||1988||8,4||[[Ісао Такахата]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|65||[[Чужі (фільм)|Чужі]]||''Aliens''||1986||8,4||[[Джеймс Камерон]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|66||[[Громадянин Кейн]]||''Citizen Kane''||1941||8,4||[[Орсон Уеллс]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|67||[[На північ через північний захід]]||''North by Northwest''||1959||8,4||[[Альфред Хічкок]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|68||[[Запаморочення (фільм)|Запаморочення]]||''Vertigo''||1958||8,4||[[Альфред Хічкок]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|69||[[Олдбой]]||''Oldboy''||2003||8,4||Пан Чхан Ук\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|70||[[Принцеса Мононоке]]||''Princess Mononoke''||1997||8,4||[[Міядзакі Хаяо]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|71||[[Підводний човен (фільм)|Підводний човен]]||''Das Boot''||1981||8,4||[[Вольфганг Петерсен]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|72||[[M (фільм)|M]]||''M''||1931||8,4||[[Фріц Ланг]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|73||[[Зоряні війни. Епізод VI. Повернення джедая]]||''Star Wars: Episode VI&nbsp;— Return of the Jedi''||1983||8,4||Річард Маркуанд\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|74||[[Амелі]]||''Le Fabuleux Destin d'Amelie Poulain''||2001||8,4||[[Жан-П'єр Жене]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|75||[[Одного разу в Америці]]||''Once Upon a Time in America''||1984||8,4||[[Серджіо Леоне]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|76||[[Історія іграшок 3]]||''Toy Story 3''||2010||8,4||[[Лі Анкрич]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|77||[[Скажені пси]]||''Reservoir Dogs''||1992||8,4||[[Квентін Тарантіно]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|78||[[Свідок обвинувачення]]||''Witness for the Prosecution''||1957||8,1||[[Біллі Вайлдер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|79||[[Хоробре серце]]||''Braveheart''||1995||8,4||[[Мел Гібсон]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|80||[[Механічний апельсин (фільм)|Механічний апельсин]]||''A Clockwork Orange''||1971||8,4||[[Стенлі Кубрик]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|81||[[Подвійна страховка]]||''Double Indemnity''||1944||8,4||[[Біллі Вайлдер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|82||[[Таксист (фільм)|Таксист]]||''Taxi Driver''||1976||8,4||[[Мартін Скорсезе]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|83||[[Реквієм за мрією (фільм)|Реквієм за мрією]]||''Requiem for a Dream''||2000||8,4||[[Даррен Аронофскі]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|84||[[Вбити пересмішника]]||''To Kill a Mockingbird''||1962||8,4||Роберт Малліган\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|85||[[Лоуренс Аравійський (фільм)|Лоуренс Аравійський]]||''Lawrence of Arabia''||1962||8,4||[[Девід Лін]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|86||[[Вічне сяйво чистого розуму]]||''Eternal Sunshine of the Spotless Mind''||2004||8,3||[[Мішель Ґондрі]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|87||[[Божевільний Макс: Дорога люті]]||''Mad Max: Fury Road''||2015||8,4||[[Джордж Міллер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|88||[[Суцільнометалева оболонка]]||''Full Metal Jacket''||1987||8,3||[[Стенлі Кубрик]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|89||[[Афера (фільм, 1973)|Афера]]||''The Sting''||1973||8,1||[[Джордж Рой Хілл]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|90||[[Крадії велосипедів]]||''Bicycle Thieves''||1948||8,3||[[Вітторіо де Сіка]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|91||[[Амадей (фільм)|Амадей]]||''Amadeus''||1984||8,3||[[Мілош Форман]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|92||[[Співаючи під дощем]]||''Singin' in the Rain''||1952||8,3||Стенлі Донен, Джин Келлі\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|93||[[Монті Пайтон і Священний Грааль]]||''Monty Python and the Holy Grail''||1975||8,3||[[Террі Гілліам]], [[Террі Джонс]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|94||[[Великий куш (фільм)|Великий куш]]||''Snatch.''||2000||8,3||[[Ґай Річі]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|95||[[Космічна одіссея 2001 року (фільм)|2001 рік: Космічна Одіссея]]||''2001: A Space Odyssey''||1968||8,3||[[Стенлі Кубрик]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|96||[[Маля (фільм, 1921)|Маля]]||''The Kid''||1921||8,0||[[Чарлі Чаплін]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|97||[[На декілька доларів більше (фільм)|На декілька доларів більше]]||''For a Few Dollars More''||1965||8,2||Серджіо Леоне\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|98||[[Таємниці Лос-Анджелеса (фільм)|Таємниці Лос-Анджелеса]]||''L.A. Confidential''||1997||8,3||Кертіс Хенсон\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|99||[[Расьомон (фільм)|Расьомон]]||''Rashomon''||1950||8,3||[[Акіра Куросава]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|100||[[Все про Єву]]||''All About Eve''||1950||8,3||[[Джозеф Манкевич]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|101||[[Історія іграшок]]||''Toy Story''||1995||8,3||Джон Лассетер\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|102||[[Квартира (фільм)|Квартира]]||''The Apartment''||1960||8,3||[[Біллі Вайлдер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|103||[[Безславні виродки (фільм, 2009)|Безславні виродки]]||''Inglourious Basterds''||2009||8,3||[[Квентін Тарантіно]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|104||[[Скарби Сьєрра-Мадре]]||''The Treasure of the Sierra Madre''||1948||8,3||[[Джон Х'юстон]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|105||[[Надер і Симін: Розлучення]]||''Jodaeiye Nader az Simin''||2011||8,4||Асгар Фархаді\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|106||[[Індіана Джонс і останній хрестовий похід]]||''Indiana Jones and the Last Crusade''||1989||8,3||[[Стівен Спілберг]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|100||[[У джазі тільки дівчата]]||''Some Like It Hot''||1959||8,3||[[Біллі Вайлдер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|104||[[Третя людина]]||''The Third Man''||1949||8,3||Керол Рід\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|106||[[Тілоохоронець (фільм, 1961)|Тілоохоронець]]||''Yojimbo''||1961||8,2||[[Акіра Куросава]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|108||[[Бетмен: Початок]]||''Batman Begins''||2005||8,3||[[Крістофер Нолан]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|109||[[Метрополіс (фільм)|Метрополіс]]||''Metropolis''||1927||8,3||[[Фріц Ланг]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|110||[[Юність (фільм)|Юність]]||''Boyhood''||2014||8,3||[[Річард Лінклейтер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|111||[[Непрощений (фільм)|\u200EНепрощений]]||''Unforgiven''||1992||8,3||[[Клінт Іствуд]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|112||[[Зірочки на землі]]||''Taare Zameen Par''||2007||8,3||[[Аамір Хан]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|113||[[Обличчя зі шрамом (фільм, 1983)|Обличчя зі шрамом]]||''Scarface''||1983||8,3||[[Браян Де Пальма]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|114||[[Скажений бик]]||''Raging Bull''||1980||8,3||[[Мартін Скорсезе]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|115||[[Вперед і вгору]]||''Up''||2009||8,3||[[Піт Доктер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|116||[[Китайський квартал (фільм)|Китайський квартал]]||''Chinatown''||1974||8,3||[[Роман Полянський]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|117||[[3 ідіоти]]||''3 Idiots''||2009||8,2||[[Раджкумар Хірані]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|118||[[Велика втеча]]||''The Great Escape''||1963||8,3||Джон Еліот Стьорджес\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|119||[[Бункер (фільм)|Бункер]]||''Downfall''||2004||8,2||Олівер Хіршбіґель\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|120||[[Міцний горішок]]||''Die Hard''||1988||8,2||Джон Мактірнан\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|121||[[У порту (фільм)|У порту]]||''On the Waterfront''||1954||8,2||[[Еліа Казан]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|122||[[Загублена (фільм)|\u200EЗагублена]]||''Gone Girl''||2014||8,3||[[Девід Фінчер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|123||[[Лабіринт Фавна]]||''Pan's Labyrinth''||2006||8,3||[[Гільєрмо дель Торо]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|124||[[Містер Сміт вирушає до Вашингтону]]||''Mr. Smith Goes to Washington''||1939||8,2||[[Френк Капра]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|125||[[Полювання (фільм, 2012)|Полювання]]||''Jagten''||2012||8,2||[[Томас Вінтерберґ]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|126||[[Протистояння (фільм, 1995)|Протистояння]]||''Heat''||1995||8,2||[[Майкл Манн]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|127||[[Міст через річку Квай]]||''The Bridge on the River Kwai''||1957||8,2||[[Девід Лін]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|128||[[Бандит (фільм, 1996)|\u200EБандит]]||''Eskiya''||1996||8,3||Явуз Турґул\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|129||[[Розумник Вілл Хантінг]]||''Good Will Hunting''||1997||8,2||[[Гас Ван Сент]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|130||[[Сьома печатка (фільм)|Сьома печатка]]||''Det Sjunde Inseglet''||1957||8,2||[[Інґмар Берґман]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|131||[[Мій сусід Тоторо]]||''My Neighbor Totoro''||1988||8,2||[[Міядзакі Хаяо]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|132||[[Жити (фільм, 1952)|Жити]]||''Ikiru''||1985||8,2||[[Акіра Куросава]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|133||[[Золота лихоманка (фільм)|Золота лихоманка]]||''The Gold Rush''||1925||8,1||[[Чарлі Чаплін]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|134||[[Вовк з Уолл-стріт]]||''The Wolf of Wall Street''||2013||8,2||[[Мартін Скорсезе]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|135||[[Людина-слон]]||''The Elephant Man''||1980||8,3||[[Девід Лінч]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|136||[[Сунична галявина]]||''Wild Strawberries''||1957||8,2||[[Інґмар Берґман]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|137||[[Ран (фільм)|Ран]]||''Ran''||1985||8,2||[[Акіра Куросава]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|138||[[Той, хто біжить по лезу]]||''Blade Runner''||1982||8,2||[[Рідлі Скотт]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|139||[[Генерал (фільм)|Генерал]]||''The General''||1926||8,2||Клайд Брукман, [[Бастер Кітон]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|140||[[Карти, гроші та два стволи, що димлять|Карти, гроші, два стволи]]||''Lock, Stock and Two Smoking Barrels''||1998||8,2||[[Ґай Річі]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|141||[[Таємниця в його очах]]||''The Secret in Their Eyes''||2009||8,2||[[Хуан Хосе Кампанелья]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|142||[[Казино (фільм, 1995)|Казино]]||''Casino''||1995||8,2||[[Мартін Скорсезе]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|143||[[Гран Торіно]]||''Gran Torino''||2008||8,2||[[Клінт Іствуд]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|144||[[Великий Лебовський]]||''The Big Lebowski''||1998||8,2||[[брати Коен]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|145||[[Воїн (фільм, 2011)|Воїн]]||''Warrior''||2011||8,2|||Гевін О'Коннор\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|146||[[Ребекка (фільм)|Ребекка]]||''Rebecca''||1940||8,3||[[Альфред Хічкок]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|147||[[V означає Вендетта]]||''V for Vendetta''||2006||8,2||Джеймс МакТіг\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|148||[[Мандрівний замок Хаула]]||''Hauru no ugoku shiro ''||2004||8,0||[[Хаяо Міядзакі]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|149||[[Колір шафрану]]||''Rang De Basanti''||2006||8,0||[[Ракеш Омпракаш Мехра]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|150||[[Мисливець на оленів]]||''The Deer Hunter''||1978||8,2||[[Майкл Чіміно]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|151||[[Холоднокровний Люк]]||''Cool Hand Luke''||1967||8,2||Стюарт Розенберг\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|152||[[Це сталося якось вночі]]||''It Happened One Night''||1934||8,2||[[Френк Капра]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|153||[[Як приборкати дракона]]||''How to Train Your Dragon''||2010||8,2||Ден Сандерс, Дін Беблуа\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|154||[[Фарґо (фільм)|Фарґо]]||''Fargo''||1996||8,2||[[брати Коен]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|155||[[Нюрнбернський процес (фільм)|Нюрнбернський процес]]||''Judgment at Nuremberg''||1961||8,1||[[Стенлі Крамер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|155||[[На голці]]||''Trainspotting''||1996||8,1||[[Денні Бойл]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|157||[[Гонка (фільм, 2013)|Гонка]]||''Rush''||2013||8,2||[[Рон Говард]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|158||[[Віднесені вітром (фільм)|Віднесені вітром]]||''Gone with the Wind''||1939||8,1||[[Віктор Флемінг]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|159||[[В диких умовах]]||''Into the Wild''||2007||8,1||[[Шон Пенн]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|160||[[Мальтійський сокіл]]||''The Maltese Falcon''||1941||8,3||[[Джон Х'юстон]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|161||[[Ігри розуму (фільм)|Ігри розуму]]||''A Beautiful Mind''||2001||8,1||[[Рон Говард]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|162||[[У випадку вбивства набирайте «М»|У випадку вбивства наберіть «М»]]||''Dial M for Murder''||1954||8,1||[[Альфред Хічкок]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|163||[[Плата за страх]]||''Le salaire de la peur''||1953||8,1||[[Анрі-Жорж Клузо]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|164||[[Шосте чуття (фільм)|Шосте чуття]]||''The Sixth Sense''||1999||8,1||[[М. Найт Ш'ямалан]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|165||[[Готель «Руанда»]]||''Hotel Rwanda''||2004||8,1||Террі Джордж\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|166||[[Щось (фільм, 1982)|Щось]]||''The Thing''||1982||8,3||[[Джон Карпентер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|167||[[Вартові галактики]]||''Guardians of the Galaxy''||2014||8,5||[[Джеймс Ганн]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|168||[[У пошуках Немо]]||''Finding Nemo''||2003||8,1||[[Ендрю Стентон]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|169||[[Старим тут не місце]]||''No Country for Old Men''||2007||8,1||[[брати Коен]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|170||[[Люблячі серця]]||''Dil Chahta Hai''||2001||8,1||Фархан Ахтар\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|171||[[Мері та Макс]]||''Mary and Max''||2009||8,1||Адам Еліот\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|172||[[Буч Кессіді і Санденс Кід (фільм)|Буч Кессіді і Санденс Кід]]||''Butch Cassidy and the Sundance Kid''||1969||8,1||[[Джордж Рой Хілл]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|173||[[Вбити Білла]]||''Kill Bill: Vol. 1''||2003||8,1||[[Квентін Тарантіно]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|174||[[Взвод (фільм)|Взвод]]||''Platoon''||1986||8,1||[[Олівер Стоун]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|175||[[Життя Брайана за Монті Пайтон]]||''Life of Brian''||1979||8,1||[[Террі Джонс]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|176||[[Пожежі (фільм, 2010)|Пожежі]]||''Incendies''||2010||8,1||Дені Вільньов\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|177||[[12 років рабства]]||''12 Years a Slave''||2013||8,1||[[Стів Макквін]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|178||[[Телемережа (фільм)|Телемережа]]||''Network''||1976||8,1||[[Сідні Люмет]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|179||[[Дотик зла]]||''Touch of Evil''||1958||8,1||[[Орсон Уеллс]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|180||[[Дияволиці]]||''Les diaboliques''||1955||8,1||[[Анрі-Жорж Клузо]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|181||[[Енні Холл]]||''Annie Hall''||1977||8,1||[[Вуді Аллен]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|182||[[Принцеса наречена]]||''The Princess Bride''||1987||8,0||[[Роб Райнер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|183||[[Місто гріхів]]||''Sin City''||2005||8,1||[[Роберт Родрігес]], [[Френк Міллер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|184||[[Нафта (фільм)|Нафта]]||''There Will Be Blood''||2007||8,1||[[Пол Томан Андерсон]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|185||[[Залишся зі мною]]||''Stand by Me''||1986||8,1||[[Роб Райнер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|186||[[Готель \"Гранд Будапешт\"]]||''The Grand Budapest Hotel''||2014||8,1||[[Вес Андерсон]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|187||[[Бен-Гур (фільм, 1959)|Бен-Гур]]||''Ben-Hur''||1959||8,1||[[Вільям Вайлер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|188||[[Чотириста ударів]]||''Les quatre cents coups''||1959||8,1||[[Франсуа Трюффо]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|189||[[Amores perros]]||''Amores Perros''||2000||8,1||[[Алехандро Гонсалес Іньярріту]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|190||[[В ім'я батька (фільм)|В ім'я батька]]||''In the Name of the Father''||1993||8,0||[[Джим Шерідан]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|191||[[Крихітка на мільйон доларів]]||''Million Dollar Baby''||2004||8,1||[[Клінт Іствуд]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|192||[[Грона гніву (фільм)|Грона гніву]]||''The Grapes of Wrath''||1940||8,1||[[Джон Форд (режисер)|Джон Форд]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|193||[[Чарівник країни Оз]]||''The Wizard of Oz''||1939||8,1||[[Віктор Флемінг]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|194||[[Хатіко: Вірний друг]]||''Hachiko: A Dog's Story''||2009||8,1||[[Лассе Хальстрем]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|195||[[Персона (фільм)|Персона]]||''Persona''||1966||8,0||[[Інгмар Бергман]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|196||[[Найкращі роки нашого життя]]||''The Best Years of Our Lives''||1946||8,1||[[Біллі Вайлдер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|197||[[Месники (фільм, 2012)|Месники]]||''The Avengers''||2012||8,1||[[Джосс Ведон]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|198||[[Банди Вассейпура]]||''Gangs of Wasseypur ''||2012||8,1||Анураг Каш'яп\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|199||[[Навсікая з Долини Вітрів]]||''風の谷のナウシカ''||1984||8,1||[[Міядзакі Хаяо]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|200||[[Ультиматум Борна]]||''The Bourne Ultimatum''||2007||8,1||Пол Ґрінґрасс\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|201||[[Ганді (фільм)|Ганді]]||''Gandhi''||1982||8,1||[[Річард Аттенборо]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|202||[[Донні Дарко]]||''Donnie Darko''||2001||8,1||Річард Келлі\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|203||[[Вісім з половиною]]||''Otto e mezzo''||1963||8,1||[[Федеріко Фелліні]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|204||[[Незнайомці в потягу]]||''Strangers on a Train''||1951||8,2||[[Альфред Хічкок]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|205||[[Гра в імітацію (фільм, 2014)|Гра в імітацію ]]||''The Imitation Game''||2014||8,1||Мортен Тильдум\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|206||[[Подвійна рокіровка]]||''Infernal Affairs''||2002||8,1||Ендрю Лау, Алан Мак\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|207||[[Люди Ікс: Дні минулого майбутнього (фільм)|Люди Ікс: Дні минулого майбутнього]]||''X-Men: Days of Future Past''||2014||8,2||[[Браян Сінгер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|208||[[Сталкер (фільм)|Сталкер]]||''Сталкер''||1979||8,0||[[Андрій Тарковський]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|209||[[Щелепи (фільм)|Щелепи]]||''Jaws''||1975||8,2||[[Стівен Спілберг]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|210||[[Дванадцять мавп (фільм)|Дванадцять мавп]]||''Twelve Monkeys''||1995||8,1||[[Террі Гіліам]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|211||[[Лагаан: Одного разу в Індії]]||''Lagaan: Once Upon a Time in India''||2001||8,0||Ашутош Говарікер\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|212||[[Рівно ополудні]]||''High Noon''||1952||8,2||[[Фред Циннеманн]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|213||[[Острів проклятих (фільм)]]||''Shutter Island''||2010||8,0||[[Мартін Скорсезе]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|214||[[Лиха слава]]||''Notorious''||1946||8,2||[[Альфред Хічкок]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|215||[[Термінатор (фільм)|Термінатор]]||''The Terminator''||1984||8,0||[[Джеймс Камерон]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|216||[[Промова короля]]||''The King's Speech''||2010||8,2||[[Том Гупер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|217||[[Перед сходом сонця]]||''Before Sunrise''||1995||8,0||[[Річард Лінклейтер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|218||[[Фанні та Олександр]]||''Fanny och Alexander''||1984||8,0||[[Інґмар Берґман]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|219||[[День Бабака (фільм)|День бабака]]||''Groundhog Day''||1993||8,0||Гарольд Реміс\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|220||[[Гаррі Поттер та Смертельні Реліквії: частина 2]]||''Harry Potter and the Deathly Hallows: Part 2 ''||2011||8,3||[[Девід Йєтс]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|221||[[Іп Ман (фільм)|Іп Ман]]||''Yip Man''||2008||8,0||Вілсон Іп\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|222||[[Битва за Алжир (фільм)|Битва за Алжир]]||''La battaglia di Algeri''||1966||8,0||[[Джилло Понтекорво]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|223||[[Роккі]]||''Rocky''||1976||8,0||Джон Евілдсен\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|224||[[Ніч мисливця]]||''The Night of the Hunter''||1955||8,0||[[Чарлз Лоутон]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|225||[[Собачий полудень]]||''Dog Day Afternoon''||1975||8,0||[[Сідні Люмет]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|226||[[Корпорація монстрів]]||''Monsters, Inc.''||2001||7,9||[[Піт Доктер]], [[Девід Сільверман]], [[Лі Анкрич]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|227||[[Ненависть (фільм)|Ненависть]]||''La haine ''||1995||8,0||[[Матьє Кассовіц]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|228||[[Спогади про вбивство]]||''Salinui chueok''||2003||8,0||[[Пон Джунхо]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|229||[[Хто боїться Вірджинії Вульф?]]||''Who's Afraid of Virginia Woolf?''||1966||8,0||[[Майк Ніколс]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|230||[[Дорога (фільм, 1954)|Дорога]]||''La strada''||1954||8,0||[[Федеріко Фелліні]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|231||[[Пірати Карибського моря: Прокляття «Чорної перлини»]]||''Pirates of the Caribbean: The Curse of the Black Pearl''||2003||8,0||[[Ґор Вербінські]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|232||[[Баррі Ліндон]]||''Barry Lyndon''||1975||8,0||[[Стенлі Кубрик]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|233||[[За жменю доларів]]||''Per un pugno di dollari''||1964||8,0||[[Серджо Леоне]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|234||[[Небесний замок Лапута]]||''天空の城ラピュタ''||1986||8,0||[[Хаяо Міядзакі]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|235||[[Шоу Трумана]]||''The Truman Show''||1998||8,0||Пітер Вір\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|236||[[Великий сон]]||''The Big Sleep''||1946||8,1||[[Говард Гоукс]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|237||[[Прислуга (фільм)|Прислуга]]||''The Help''||2011||8,0||Тейт Тейлор\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|238||[[Парк Юрського періоду (фільм)|Парк Юрського періоду]]||''Jurassic Park''||1993||8,1||[[Стівен Спілберг]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|239||[[Випускник (фільм)|Випускник]]||''The Graduate''||1967||8,0||[[Майк Ніколс]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|240||[[Римські канікули (фільм)|Римські канікули]]||''Roman Holiday''||1953||8,0||[[Вільям Вайлер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|241||[[Король більярду]]||''The Hustler''||1961||8,0||Роберт Россен\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|242||[[Повернення на Батьківщину]]||'' Swades: We, the People ''||2004||8,0||Ашутош Говарікер\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|243||[[Красуня і чудовисько (фільм, 1991)|Красуня і чудовисько]]||''Beauty and the Beast''||1991||8,1||[[Гарі Труздейл]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|244||[[Метелик (фільм)|Метелик]]||''Papillon''||1973||8,0||[[Франклін Шеффнер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|245||[[Свято (фільм, Данія)|Свято]]||''Festen''||1998||8,0||[[Томас Вінтерберґ]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|246||[[Любовний настрій]]||''花样 年华/In the Mood for Love''||2000||8,0||[[Вонг Карвай]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|247||[[Мотузка]]||''Rope''||1948||8,0||[[Альфред Хічкок]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|248||[[Полонянки (фільм, 2013)|Полонянки]]||''Prisoners''||2013||8,0||Дені Вільньов\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|249||[[Табір для військовополонених №17]]||''Stalag 17''||1953||8,0||[[Біллі Вайлдер]]\n" +
            "|- bgcolor=\"#d0ffd0\"\n" +
            "|250||[[Солодке життя (фільм)|Солодке життя]]||''La Docce Vita''||1960||8,0||[[Федеріко Фелліні]]\n" +
            "|}\n";

}
