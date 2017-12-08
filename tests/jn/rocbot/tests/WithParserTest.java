package jn.rocbot.tests;

import java.util.Arrays;;

public class WithParserTest {
    private void println(String s){
        System.out.println(s);
    }

    private WithParserTest(){
        println(testParenthes("sb & (ml or (mb & vl))"));
        println(testParenthes(testParenthes("sb & (ml or (mb & vl))")));
    }

    private String testParenthes(String string){
        char[] array = string.toCharArray();
        try {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == '(') {
                    int j = array.length;
                    do {
                        j--;
                        if (array[j] == ')') {
                            return new String(Arrays.copyOfRange(array, i, j + 1));
                        }
                    } while (j > i);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            throw new ArrayIndexOutOfBoundsException();
        }
        return "";
    }

    public static void main(String[] args) {
        new WithParserTest();
    }
}
