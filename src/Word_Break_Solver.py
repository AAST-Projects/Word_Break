# File: word_break_solver.py

import sys
import os
from typing import List
import string

def word_break_rec(ind: int, s: str, dictionary: List[str], dp: List[int],
                   path: List[str], all_results: List[List[str]]) -> bool:
    if ind == len(s):
        all_results.append(list(path))
        return True

    if dp[ind] == 0:
        return False

    possible = False

    for word in dictionary:
        if s.startswith(word, ind):
            path.append(word)
            res = word_break_rec(ind + len(word), s, dictionary, dp, path, all_results)
            if res:
                possible = True
            path.pop()

    dp[ind] = 1 if possible else 0
    return possible

def word_break(s: str, dictionary: List[str], all_results: List[List[str]], dp: List[int]) -> bool:
    for i in range(len(dp)):
        dp[i] = -1

    result = word_break_rec(0, s, dictionary, dp, [], all_results)

    print("\n--- DP Table ---")
    print("Index:   ", ' '.join(str(i) for i in range(len(s))))
    print("Values:  ", ' '.join(str(dp[i]) if dp[i] != -1 else '.' for i in range(len(s))))
    print("(Note: 1 = possible, 0 = not possible, . = unvisited)")

    return result

def export_to_file(filename: str, s: str, can_segment: bool,
                   results: List[List[str]], dp: List[int]) -> str:
    try:
        script_dir = os.path.dirname(os.path.abspath(__file__))
        output_path = os.path.join(script_dir, filename)

        with open(output_path, 'w', encoding='utf-8') as out:
            out.write(f"Input string: {s}\n")
            out.write(f"Can be segmented: {can_segment}\n")

            if can_segment:
                out.write("\nValid segmentations:\n")
                for count, path in enumerate(results, 1):
                    out.write(f"{count}. {' '.join(path)}\n")

                out.write("\nSplit point indices:\n")
                for path in results:
                    index = 0
                    for word in path:
                        out.write(f"{index} → {index + len(word)}: '{word}'\n")
                        index += len(word)
                    out.write("---\n")
            else:
                out.write("No valid segmentations found.\n")

            out.write("\nDP Table:\n")
            out.write("Index:   " + ' '.join(str(i) for i in range(len(s))) + "\n")
            out.write("Values:  " + ' '.join(str(dp[i]) if dp[i] != -1 else '.' for i in range(len(s))) + "\n")

        return output_path
    except Exception as e:
        print(f"Error writing to file: {e}")
        return ""


if __name__ == '__main__':
    if len(sys.argv) > 1:
        # Run from file
        try:
            with open(sys.argv[1], 'r') as f:
                lines = [line.strip() for line in f.readlines()]
                s = lines[0]
                s = s.translate(str.maketrans('', '', string.punctuation))  # Remove punctuation
                n = int(lines[1])
                dictionary = lines[2:2 + n]

            all_results = []
            dp = [-1] * (len(s) + 1)
            can_segment = word_break(s, dictionary, all_results, dp)

            print("\n--- Result ---")
            print(f"Can be segmented: {can_segment}")
            if can_segment:
                for i, result in enumerate(all_results, 1):
                    print(f"{i}. {' '.join(result)}")

            output_path = export_to_file("wordbreak_output.txt", s, can_segment, all_results, dp)
            print(f"\nResults exported to: {output_path}")
        except Exception as e:
            print(f"Error: {e}")
    else:
        import tkinter as tk
        from tkinter import scrolledtext

        root = tk.Tk()
        root.title("Word Break Solver")

        # GUI State
        current_input = ['']
        current_dict = [[]]
        current_results = [[]]
        current_dp = [[]]

        def solve():
            s = input_str.get().strip()
            dictionary = dict_input.get("1.0", tk.END).strip().split()

            if not s or not dictionary:
                result_display.delete("1.0", tk.END)
                result_display.insert(tk.END, "Please provide both an input string and dictionary.")
                return

            all_results.clear()
            dp = [-1] * (len(s) + 1)
            can_segment = word_break(s, dictionary, all_results, dp)

            output = f"Can be segmented: {can_segment}\n\n"
            if can_segment:
                for i, path in enumerate(all_results, 1):
                    output += f"{i}. {' '.join(path)}\n"
                output += "\nDP Table:\nIndex:   " + ' '.join(str(i) for i in range(len(s))) + "\n"
                output += "Values:  " + ' '.join(str(dp[i]) if dp[i] != -1 else '.' for i in range(len(s)))
            else:
                output += "No valid segmentations found."

            result_display.delete("1.0", tk.END)
            result_display.insert(tk.END, output)

            # Store current values for export
            current_input[0] = s
            current_dict[0] = dictionary
            current_results[0] = list(all_results)
            current_dp[0] = dp

        def export():
            if not current_input[0]:
                result_display.insert(tk.END, "\n\nNothing to export.")
                return

            file_path = export_to_file("wordbreak_output.txt",
                                       current_input[0],
                                       bool(current_results[0]),
                                       current_results[0],
                                       current_dp[0])

            if file_path:
                result_display.insert(tk.END, f"\n\nExported to:\n{file_path}")
            else:
                result_display.insert(tk.END, "\n\nFailed to export.")

        # Input fields
        tk.Label(root, text="Input String:").pack()
        input_str = tk.Entry(root, width=50)
        input_str.pack()

        tk.Label(root, text="Dictionary Words (space-separated):").pack()
        dict_input = scrolledtext.ScrolledText(root, height=5, width=60)
        dict_input.pack()

        # Buttons
        tk.Button(root, text="Solve", command=solve).pack(pady=5)
        tk.Button(root, text="Export to File", command=export).pack()

        # Output
        result_display = scrolledtext.ScrolledText(root, height=15, width=80)
        result_display.pack()

        # Global state
        all_results = []

        root.mainloop()
