package org.tdtu.ecommerceapi.utils;

public class RegexUtils {
    /**
     * @note ^ asserts the start of the string.
     * @note [\\w\\s\\-()]+ matches one or more word characters (alphanumeric and underscore),
     * whitespace characters, hyphens, parentheses, or underscores.
     * @note \\.matches a literal dot character.
     * @note [a-zA-Z]{2,4} matches two to four alphabetic characters.
     * @note $ asserts the end of the string.
     */
    public static final String SIMPLE_FILENAME_PATTERN = "^[\\w\\s\\-()]+\\.[a-zA-Z]{2,4}$";

    /**
     * @note "([\\w.]+?)": This part captures a group of word characters (alphanumeric and underscore)
     * and dots. The +? makes the match non-greedy, meaning it will match the smallest possible
     * sequence of characters. This group is enclosed in parentheses for capturing.
     * @note (: | < | > | = | ! = | < = | > = | % | \ \ ( \ \)): This part matches one of the specified symbols: -, :, <, >,
     * =, !=, <=, >=, %, or (). The symbols are enclosed in parentheses for grouping.
     * @note ([ \ \ w \ \ s \ \ ( \ \):@;,._-]+?): This part captures a group of word characters, whitespace
     * characters, and specific symbols like (), :, @, ,, ;, ., _, and -. The +? makes the match
     * non-greedy. This group is enclosed in parentheses for capturing.
     * @note \\|: This part matches the literal | character.
     * @note In summary, this regular expression captures patterns with three components separated by
     * |. The first component captures a sequence of word characters and dots, the second
     * component captures one of the specified symbols, and the third component captures a
     * sequence of word characters, whitespace characters, and specific symbols. The | character
     * is used as a delimiter to separate these components.
     */
    public static final String FILTER_REQUEST_PATTERN =
            "([\\w.]+?)(:|-|<|>|=|!=|<=|>=|%|\\(\\))([\\w\\s\\(\\):@;,._-]+?)\\|";
}