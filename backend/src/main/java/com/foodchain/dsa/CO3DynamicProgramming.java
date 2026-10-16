package com.foodchain.dsa;

public class CO3DynamicProgramming {

    // 1. LEVENSHTEIN DISTANCE
    public static int levenshtein(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";

        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (Character.toLowerCase(a.charAt(i - 1)) == Character.toLowerCase(b.charAt(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int insert = dp[i][j - 1];
                    int delete = dp[i - 1][j];
                    int replace = dp[i - 1][j - 1];
                    dp[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
                }
            }
        }
        return dp[n][m];
    }

    public static int[][] levenshteinMatrix(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";

        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (Character.toLowerCase(a.charAt(i - 1)) == Character.toLowerCase(b.charAt(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i][j - 1], Math.min(dp[i - 1][j], dp[i - 1][j - 1]));
                }
            }
        }
        return dp;
    }

    // 2. DAMERAU-LEVENSHTEIN DISTANCE
    public static int damerauLevenshtein(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";

        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = Character.toLowerCase(a.charAt(i - 1)) == Character.toLowerCase(b.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);

                // Transposition
                if (i > 1 && j > 1 &&
                    Character.toLowerCase(a.charAt(i - 1)) == Character.toLowerCase(b.charAt(j - 2)) &&
                    Character.toLowerCase(a.charAt(i - 2)) == Character.toLowerCase(b.charAt(j - 1))) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1);
                }
            }
        }
        return dp[n][m];
    }

    // 3. BITMASK DP (Delivery/Task Assignment Problem)
    public static int bitmaskMinCost(int[][] cost) {
        if (cost == null || cost.length == 0) return 0;
        int n = cost.length;
        if (n > 20) throw new IllegalArgumentException("Bitmask DP dimension too large (max 20).");

        int totalMasks = 1 << n;
        int[] dp = new int[totalMasks];
        java.util.Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;

        for (int mask = 0; mask < totalMasks; mask++) {
            int worker = Integer.bitCount(mask);
            if (worker >= n) continue;

            for (int task = 0; task < n; task++) {
                if ((mask & (1 << task)) == 0) {
                    int newMask = mask | (1 << task);
                    dp[newMask] = Math.min(dp[newMask], dp[mask] + cost[worker][task]);
                }
            }
        }
        return dp[totalMasks - 1];
    }

    // 4. MATRIX CHAIN MULTIPLICATION
    public static long matrixChain(int[] dimensions) {
        if (dimensions == null || dimensions.length < 2) return 0;
        int n = dimensions.length - 1;
        long[][] dp = new long[n][n];

        for (int length = 2; length <= n; length++) {
            for (int i = 0; i <= n - length; i++) {
                int j = i + length - 1;
                dp[i][j] = Long.MAX_VALUE;

                for (int k = i; k < j; k++) {
                    long cost = dp[i][k] + dp[k + 1][j] + (long) dimensions[i] * dimensions[k + 1] * dimensions[j + 1];
                    dp[i][j] = Math.min(dp[i][j], cost);
                }
            }
        }
        return dp[0][n - 1];
    }

    // 5. OPTIMAL BINARY SEARCH TREE (OBST)
    public static long obst(int[] keys, int[] freq) {
        if (keys == null || freq == null || keys.length == 0) return 0;
        int n = Math.min(keys.length, freq.length);
        long[][] dp = new long[n][n];
        long[][] sum = new long[n][n];

        for (int i = 0; i < n; i++) {
            dp[i][i] = freq[i];
            sum[i][i] = freq[i];
        }

        for (int length = 2; length <= n; length++) {
            for (int i = 0; i <= n - length; i++) {
                int j = i + length - 1;
                sum[i][j] = sum[i][j - 1] + freq[j];
                dp[i][j] = Long.MAX_VALUE;

                for (int root = i; root <= j; root++) {
                    long left = root > i ? dp[i][root - 1] : 0;
                    long right = root < j ? dp[root + 1][j] : 0;
                    long cost = left + right + sum[i][j];
                    dp[i][j] = Math.min(dp[i][j], cost);
                }
            }
        }
        return dp[0][n - 1];
    }
}
