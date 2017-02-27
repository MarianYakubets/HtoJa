package com.htoja.mifik.htoja.data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by marian on 27.02.17.
 */

public class Dictionary {
    private static List<String> words = Arrays.asList("горила", "слон", "папуга", "лінивець", "жираф",
            "носоріг", "панда", "ведмідь", "вовк", "лис", "ворон", "лось", "єнот", "білка", "заєць",
            "скунс", "бегемот", "олень");

    public static List<String> getWords() {
        return words;
    }
}
