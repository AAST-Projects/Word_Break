// File: WordBreakSolver.java
package src;

import java.io.*;
import java.util.*;

public class WordBreakSolver {

    public static boolean wordBreakRec(int ind, String s, String[] dict, int[] dp,
                                       List<String> path, List<List<String>> allResults) {
        if (ind == s.length()) {
            allResults.add(new ArrayList<>(path));
            return true;
        }

        if (dp[ind] == 0) return false;

        boolean possible = false;

        for (String word : dict) {
            if (s.startsWith(word, ind)) {
                path.add(word);
                boolean res = wordBreakRec(ind + word.length(), s, dict, dp, path, allResults);
                if (res) possible = true;
                path.remove(path.size() - 1);
            }
        }

        dp[ind] = possible ? 1 : 0;
        return possible;
    }

    public static boolean wordBreak(String s, String[] dict,
                                    List<List<String>> allResults, int[] dp) {
        Arrays.fill(dp, -1);
        boolean result = wordBreakRec(0, s, dict, dp, new ArrayList<>(), allResults);

        System.out.println("\n--- DP Table ---");
        System.out.print("Index:   ");
        for (int i = 0; i < s.length(); i++) System.out.print(i + " ");
        System.out.print("\nValues:  ");
        for (int i = 0; i < s.length(); i++) System.out.print((dp[i] == -1 ? "." : dp[i]) + " ");
        System.out.println("\n(Note: 1 = possible, 0 = not possible, . = unvisited)");

        return result;
    }

    public static void exportToFile(String filename, String s, boolean canSegment,
                                    List<List<String>> results, int[] dp) {
        try (PrintWriter out = new PrintWriter(filename)) {
            out.println("Input string: " + s);
            out.println("Can be segmented: " + canSegment);

            if (canSegment) {
                out.println("\nValid segmentations:");
                int count = 1;
                for (List<String> path : results) {
                    out.println(count++ + ". " + String.join(" ", path));
                }

                out.println("\nSplit point indices:");
                for (List<String> path : results) {
                    int index = 0;
                    for (String word : path) {
                        out.println(index + " → " + (index + word.length()) + ": '" + word + "'");
                        index += word.length();
                    }
                    out.println("---");
                }
            } else {
                out.println("No valid segmentations found.");
            }

            out.println("\nDP Table:");
            out.print("Index:   ");
            for (int i = 0; i < s.length(); i++) out.print(i + " ");
            out.print("\nValues:  ");
            for (int i = 0; i < s.length(); i++) out.print((dp[i] == -1 ? "." : dp[i]) + " ");
            out.println();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}

// File: Main.java
package src;

import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
        import java.util.*;
        import java.io.*;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            // Run with file input
            runFromFile(args[0]);
        } else {
            // Launch GUI
            SwingUtilities.invokeLater(Main::launchGUI);
        }
    }

    private static void runFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            String s = scanner.nextLine().trim();
            int n = Integer.parseInt(scanner.nextLine().trim());
            String[] dict = new String[n];
            for (int i = 0; i < n; i++) {
                dict[i] = scanner.nextLine().trim();
            }

            List<List<String>> allResults = new ArrayList<>();
            int[] dp = new int[s.length() + 1];

            boolean canSegment = WordBreakSolver.wordBreak(s, dict, allResults, dp);

            System.out.println("\n--- Result ---");
            System.out.println("Can be segmented: " + canSegment);
            if (canSegment) {
                int count = 1;
                for (List<String> path : allResults) {
                    System.out.println(count++ + ". " + String.join(" ", path));
                }
            }
            WordBreakSolver.exportToFile("wordbreak_output.txt", s, canSegment, allResults, dp);
        } catch (Exception e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    private static void launchGUI() {
        JFrame frame = new JFrame("Word Break Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        JTextField inputField = new JTextField();
        JTextArea dictArea = new JTextArea(5, 30);
        JTextArea resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);

        JButton solveButton = new JButton("Solve");

        solveButton.addActionListener(e -> {
            String s = inputField.getText().trim();
            String[] dict = dictArea.getText().split("\\s+");

            List<List<String>> allResults = new ArrayList<>();
            int[] dp = new int[s.length() + 1];
            boolean canSegment = WordBreakSolver.wordBreak(s, dict, allResults, dp);

            StringBuilder sb = new StringBuilder();
            sb.append("Can be segmented: ").append(canSegment).append("\n\n");

            if (canSegment) {
                int count = 1;
                for (List<String> path : allResults) {
                    sb.append(count++).append(". ").append(String.join(" ", path)).append("\n");
                }

                sb.append("\nDP Table:\nIndex:   ");
                for (int i = 0; i < s.length(); i++) sb.append(i).append(" ");
                sb.append("\nValues:  ");
                for (int i = 0; i < s.length(); i++) sb.append(dp[i] == -1 ? "." : dp[i]).append(" ");
            } else {
                sb.append("No valid segmentations found.");
            }

            resultArea.setText(sb.toString());
        });

        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new GridLayout(3, 1));
        top.add(new JLabel("Input String:"));
        top.add(inputField);
        top.add(new JLabel("Dictionary Words (space-separated):"));
        top.add(new JScrollPane(dictArea));
        panel.add(top, BorderLayout.NORTH);
        panel.add(solveButton, BorderLayout.CENTER);
        panel.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}