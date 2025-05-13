package src;

import java.util.*;
public class Main {
    static boolean wordBreakRec(int ind, String s, String[] dict, int[] dp, List<String> path, List<String> result) {
        if (ind >= s.length()) {
            result.addAll(path);  // store the path
            return true;
        }

        if (dp[ind] == 0) return false; // already known as not possible

        boolean possible = false;

        for (String temp : dict) {
            if (temp.length() > s.length() - ind) continue;

            boolean ok = true;
            int k = ind;

            for (int j = 0; j < temp.length(); j++) {
                if (temp.charAt(j) != s.charAt(k)) {
                    ok = false;
                    break;
                }
                k++;
            }

            if (ok) {
                path.add(temp);  // choose
                boolean res = wordBreakRec(ind + temp.length(), s, dict, dp, path, result);
                if (res) possible = true;
                path.remove(path.size() - 1); // backtrack
            }
        }

        dp[ind] = possible ? 1 : 0;
        return possible;
    }

    public static boolean wordBreak(String s, String[] dict, List<String> result) {
        int n = s.length();
        int[] dp = new int[n + 1];
        Arrays.fill(dp, -1);
        return wordBreakRec(0, s, dict, dp, new ArrayList<>(), result);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Please enter a String:");
        String s = in.next();

        System.out.println("Please enter the size of your Dictionary (How Many Words):");
        int n = in.nextInt();

        String[] dict = new String[n];
        System.out.println("Please type in your Dictionary:");
        for (int i = 0; i < n; i++) {
            System.out.print("Enter word " + (i + 1) + ": ");
            dict[i] = in.next();
        }
        in.close();

        List<String> result = new ArrayList<>();

        boolean canSegment = wordBreak(s, dict, result);
        System.out.println("\n--- Result ---");
        System.out.println(canSegment ? "true" : "false");

        if (canSegment) {
            System.out.print("Words used: ");
            for (String word : result) {
                System.out.print(word + " ");
            }
            System.out.println();
        }
    }
}
