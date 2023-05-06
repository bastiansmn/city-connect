package fr.hydrogen.cityconnect.utils;

public class StringUtils {

    /**
     * This function removes the accents from a string
     * @param input
     * @return the string without accents
     */
    public static String removeAccents(String input) {
        return org.apache.commons.lang3.StringUtils.stripAccents(input);
    }
    
    /**
     * This function returns the Levenshtein distance between two strings
     * @param s1
     * @param s2
     * @return the Levenshtein distance between s1 and s2
     */
    public static Integer levenshteinDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] d = new int[m+1][n+1];
        for (int i = 0; i <= m; i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            d[0][j] = j;
        }
        for (int j = 1; j <= n; j++) {
            for (int i = 1; i <= m; i++) {
                int cost = s1.charAt(i-1) == s2.charAt(j-1) ? 0 : 1;
                d[i][j] = Math.min(Math.min(d[i-1][j]+1, d[i][j-1]+1), d[i-1][j-1]+cost);
            }
        }
        return d[m][n];
    }

}
