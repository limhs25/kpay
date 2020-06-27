package io.kpay.interview.support.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

@UtilityClass
public class TokenGenerator {

    public static String create(int digit){
       return new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(CharacterPredicates.LETTERS)
                .build().generate(digit);
    }
}
