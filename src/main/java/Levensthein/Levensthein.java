package Levensthein;


public class Levensthein {

    public double getDistance(String source, String target) {
        int n = source.length();
        int m = target.length();

        if(n == 0) return m;
        if(m == 0) return n;

        int[][] costs = new int[n+1][m+1];
        for(int i = 0; i <= n; i++)
            costs[i][0] = i;
        for(int j = 0; j <= m; j++)
            costs[0][j] = j;

        for(int i = 1; i <= n; ++i) {
            for(int j = 1; j <= m; ++j) {
                if(source.charAt(i-1) == target.charAt(j-1))
                    costs[i][j] = costs[i-1][j-1];
                else
                    costs[i][j] = Math.min(costs[i-1][j]+1, Math.min(costs[i][j-1]+1, costs[i-1][j-1] + 1));
            }
        }

        return costs[n][m];
    }

    public double getNormalizedDistance(String source, String target) {
        return 1 - (getDistance(source, target) / Math.max(source.length(), target.length()));
    }
}
