/*
 * Copyright 2019 nightfall.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ua.net.uid.utils.helpers;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Transliterate {
    private static final Pattern SYMBOLS = Pattern.compile("[^\\p{L}\\p{Nd}]+");
    private static final char[][] CHARS = {
            // Latin
            {'À', 'A'}, {'Á', 'A'}, {'Â', 'A'}, {'Ã', 'A'}, {'Ä', 'A'}, {'Å', 'A'}, {'Æ', 'A', 'E'}, {'Ç', 'C'}, {'È', 'E'}, {'É', 'E'}, {'Ê', 'E'}, {'Ë', 'E'},
            {'Ì', 'I'}, {'Í', 'I'}, {'Î', 'I'}, {'Ï', 'I'}, {'Ð', 'D'}, {'Ñ', 'N'}, {'Ò', 'O'}, {'Ó', 'O'}, {'Ô', 'O'}, {'Õ', 'O'}, {'Ö', 'O'}, {'Ő', 'O'},
            {'Ø', 'O'}, {'Ù', 'U'}, {'Ú', 'U'}, {'Û', 'U'}, {'Ü', 'U'}, {'Ű', 'U'}, {'Ý', 'Y'}, {'Þ', 'T', 'H'}, {'ß', 's', 's'},
            {'à', 'a'}, {'á', 'a'}, {'â', 'a'}, {'ã', 'a'}, {'ä', 'a'}, {'å', 'a'}, {'æ', 'a', 'e'}, {'ç', 'c'}, {'è', 'e'}, {'é', 'e'}, {'ê', 'e'}, {'ë', 'e'},
            {'ì', 'i'}, {'í', 'i'}, {'î', 'i'}, {'ï', 'i'}, {'ð', 'd'}, {'ñ', 'n'}, {'ò', 'o'}, {'ó', 'o'}, {'ô', 'o'}, {'õ', 'o'}, {'ö', 'o'}, {'ő', 'o'},
            {'ø', 'o'}, {'ù', 'u'}, {'ú', 'u'}, {'û', 'u'}, {'ü', 'u'}, {'ű', 'u'}, {'ý', 'y'}, {'þ', 't', 'h'}, {'ÿ', 'y'},
            //Latin symbols
            {'©', '(', 'c', ')'},
            //Greek
            {'Α', 'A'}, {'Β', 'B'}, {'Γ', 'G'}, {'Δ', 'D'}, {'Ε', 'E'}, {'Ζ', 'Z'}, {'Η', 'H'}, {'Θ', '8'}, {'Ι', 'I'}, {'Κ', 'K'}, {'Λ', 'L'}, {'Μ', 'M'},
            {'Ν', 'N'}, {'Ξ', '3'}, {'Ο', 'O'}, {'Π', 'P'}, {'Ρ', 'R'}, {'Σ', 'S'}, {'Τ', 'T'}, {'Υ', 'Y'}, {'Φ', 'F'}, {'Χ', 'X'}, {'Ψ', 'P', 'S'}, {'Ω', 'W'},
            {'Ά', 'A'}, {'Έ', 'E'}, {'Ί', 'I'}, {'Ό', 'O'}, {'Ύ', 'Y'}, {'Ή', 'H'}, {'Ώ', 'W'}, {'Ϊ', 'I'}, {'Ϋ', 'Y'},
            {'α', 'a'}, {'β', 'b'}, {'γ', 'g'}, {'δ', 'd'}, {'ε', 'e'}, {'ζ', 'z'}, {'η', 'h'}, {'θ', '8'}, {'ι', 'i'}, {'κ', 'k'}, {'λ', 'l'}, {'μ', 'm'},
            {'ν', 'n'}, {'ξ', '3'}, {'ο', 'o'}, {'π', 'p'}, {'ρ', 'r'}, {'σ', 's'}, {'τ', 't'}, {'υ', 'y'}, {'φ', 'f'}, {'χ', 'x'}, {'ψ', 'p', 's'}, {'ω', 'w'},
            {'ά', 'a'}, {'έ', 'e'}, {'ί', 'i'}, {'ό', 'o'}, {'ύ', 'y'}, {'ή', 'h'}, {'ώ', 'w'}, {'ς', 's'}, {'ϊ', 'i'}, {'ΰ', 'y'}, {'ϋ', 'y'}, {'ΐ', 'i'},
            //Turkish
            {'Ş', 'S'}, {'İ', 'I'}, {'Ç', 'C'}, {'Ü', 'U'}, {'Ö', 'O'}, {'Ğ', 'G'}, {'ş', 's'}, {'ı', 'i'}, {'ç', 'c'}, {'ü', 'u'}, {'ö', 'o'}, {'ğ', 'g'},
            //Russian
            {'А', 'A'}, {'Б', 'B'}, {'В', 'V'}, {'Г', 'G'}, {'Д', 'D'}, {'Е', 'E'}, {'Ё', 'Y', 'o'}, {'Ж', 'Z', 'h'}, {'З', 'Z'}, {'И', 'I'}, {'Й', 'J'}, {'К', 'K'},
            {'Л', 'L'}, {'М', 'M'}, {'Н', 'N'}, {'О', 'O'}, {'П', 'P'}, {'Р', 'R'}, {'С', 'S'}, {'Т', 'T'}, {'У', 'U'}, {'Ф', 'F'}, {'Х', 'H'}, {'Ц', 'C'},
            {'Ч', 'C', 'h'}, {'Ш', 'S', 'h'}, {'Щ', 'S', 'h'}, {'Ъ'}, {'Ы', 'Y'}, {'Ь'}, {'Э', 'E'}, {'Ю', 'Y', 'u'}, {'Я', 'Y', 'a'},
            {'а', 'a'}, {'б', 'b'}, {'в', 'v'}, {'г', 'g'}, {'д', 'd'}, {'е', 'e'}, {'ё', 'y', 'o'}, {'ж', 'z', 'h'}, {'з', 'z'}, {'и', 'i'}, {'й', 'j'}, {'к', 'k'},
            {'л', 'l'}, {'м', 'm'}, {'н', 'n'}, {'о', 'o'}, {'п', 'p'}, {'р', 'r'}, {'с', 's'}, {'т', 't'}, {'у', 'u'}, {'ф', 'f'}, {'х', 'h'}, {'ц', 'c'},
            {'ч', 'c', 'h'}, {'ш', 's', 'h'}, {'щ', 's', 'h'}, {'ъ'}, {'ы', 'y'}, {'ь'}, {'э', 'e'}, {'ю', 'y', 'u'}, {'я', 'y', 'a'},
            //Ukrainian
            {'Є', 'Y', 'e'}, {'І', 'I'}, {'Ї', 'Y', 'i'}, {'Ґ', 'G'}, {'є', 'y', 'e'}, {'і', 'i'}, {'ї', 'y', 'i'}, {'ґ', 'g'},
            //Czech
            {'Č', 'C'}, {'Ď', 'D'}, {'Ě', 'E'}, {'Ň', 'N'}, {'Ř', 'R'}, {'Š', 'S'}, {'Ť', 'T'}, {'Ů', 'U'}, {'Ž', 'Z'},
            {'č', 'c'}, {'ď', 'd'}, {'ě', 'e'}, {'ň', 'n'}, {'ř', 'r'}, {'š', 's'}, {'ť', 't'}, {'ů', 'u'}, {'ž', 'z'},
            //Polish
            {'Ą', 'A'}, {'Ć', 'C'}, {'Ę', 'e'}, {'Ł', 'L'}, {'Ń', 'N'}, {'Ó', 'o'}, {'Ś', 'S'}, {'Ź', 'Z'}, {'Ż', 'Z'},
            {'ą', 'a'}, {'ć', 'c'}, {'ę', 'e'}, {'ł', 'l'}, {'ń', 'n'}, {'ó', 'o'}, {'ś', 's'}, {'ź', 'z'}, {'ż', 'z'},
            //Latvian
            {'Ā', 'A'}, {'Č', 'C'}, {'Ē', 'E'}, {'Ģ', 'G'}, {'Ī', 'i'}, {'Ķ', 'k'}, {'Ļ', 'L'}, {'Ņ', 'N'}, {'Š', 'S'}, {'Ū', 'u'}, {'Ž', 'Z'},
            {'ā', 'a'}, {'č', 'c'}, {'ē', 'e'}, {'ģ', 'g'}, {'ī', 'i'}, {'ķ', 'k'}, {'ļ', 'l'}, {'ņ', 'n'}, {'š', 's'}, {'ū', 'u'}, {'ž', 'z'}
    };

    static {
        //noinspection ComparatorCombinators
        Arrays.sort(CHARS, (char[] o1, char[] o2) -> (int) o1[0] - (int) o2[0]);
    }

    public static String process(String text, String delimiter) {
        text = Normalizer.normalize(text, Normalizer.Form.NFC);
        return SYMBOLS.matcher(transliterate(text)).replaceAll(delimiter);
    }

    public static void transliterate(StringBuilder builder, CharSequence string) {
        int length = string.length();
        for (int i = 0; i < length; ++i) {
            put(builder, string.charAt(i));
        }
    }

    public static CharSequence transliterate(CharSequence string) {
        StringBuilder builder = new StringBuilder(string.length() + 10);
        transliterate(builder, string);
        return builder;
    }

    private static void put(StringBuilder output, char chr) {
        int l = 0, r = CHARS.length - 1;
        if (chr >= CHARS[0][0]) {
            while (l <= r) {
                int m = (r + l) >> 1;
                char[] item = CHARS[m];
                if (chr < item[0]) {
                    r = m - 1;
                } else if (chr > item[0]) {
                    l = m + 1;
                } else {
                    output.append(item, 1, item.length - 1);
                    return;
                }
            }
        }
        output.append(chr);
    }

    /* keyboard
    [
        'ru' => [
            'code' => 'rus',
            'inverted' => [
                'q'=>'й','w'=>'ц','e'=>'у','r'=>'к','t'=>'е','y'=>'н','u'=>'г','i'=>'ш','o'=>'щ','p'=>'з','['=>'х','{'=>'х',']'=>'ъ','}'=>'ъ',
                'a'=>'ф','s'=>'ы','d'=>'в','f'=>'а','g'=>'п','h'=>'р','j'=>'о','k'=>'л','l'=>'д',';'=>'ж',':'=>'ж',"'"=>'э','"'=>'э',
                'z'=>'я','x'=>'ч','c'=>'с','v'=>'м','b'=>'и','n'=>'т','m'=>'ь',','=>'б','<'=>'б','.'=>'ю','>'=>'ю','`'=>'ё','~'=>'ё'
            ]
        ],
        'uk' => [
            'code' => 'ukr',
            'inverted' => [
                'q'=>'й','w'=>'ц','e'=>'у','r'=>'к','t'=>'е','y'=>'н','u'=>'г','i'=>'ш','o'=>'щ','p'=>'з','['=>'х','{'=>'х',']'=>'ї','}'=>'ї',
                'a'=>'ф','s'=>'і','d'=>'в','f'=>'а','g'=>'п','h'=>'р','j'=>'о','k'=>'л','l'=>'д',';'=>'ж',':'=>'ж',"'"=>'є','"'=>'є',
                'z'=>'я','x'=>'ч','c'=>'с','v'=>'м','b'=>'и','n'=>'т','m'=>'ь',','=>'б','<'=>'б','.'=>'ю','>'=>'ю','\\'=>'ґ','|'=>'ґ'
            ]
        ]
    ]
     */
}
