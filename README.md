# 🧠 Word Break Solver (Java + Memoization)

This is a simple Java console application that solves the **Word Break Problem** using recursion and memoization (top-down dynamic programming). Given a string and a dictionary of words, the program checks whether the string can be segmented into a space-separated sequence of one or more dictionary words, and also **prints the actual words** used in the segmentation.

---

## 🚀 Getting Started

Follow these steps to clone and run the project on your machine.

### ✅ Prerequisites

Make sure you have the Java Development Kit (JDK) installed. You can check by running:

```bash
java -version
```

If not installed, download it from: https://www.oracle.com/java/technologies/javase-downloads.html

---

### 📁 Clone the Repository

```bash
git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name
```

Replace `your-username` and `your-repo-name` with your actual GitHub information.

---

### 🛠 Compile and Run

```bash
javac Main.java
java Main
```

---

## ✨ Features

- ✅ Verifies if a string can be segmented using a given set of dictionary words.
- 📦 Prints the actual words used in segmentation.
- ⚡ Uses memoization (top-down DP) to reduce time complexity.
- 🧠 Shows clear prompts for user input.
- 🔍 Clean and readable recursive logic.

---

## 🧪 Sample Run

```
Please enter a String:
ilikegfg

Please enter the size of your Dictionary (How Many Words):
3

Please type in your Dictionary:
Enter word 1: i
Enter word 2: like
Enter word 3: gfg

--- You entered ---
i
like
gfg

true
Words used: i like gfg
```

---

## 🔍 How It Works

1. **Recursive Check**: Starting from index `0`, the program checks each possible dictionary word.
2. **Matching**: If the word matches a substring starting at current index, it recurses from the end of that word.
3. **Memoization**: An integer `dp[]` array is used to store whether a segment starting at each index can be broken successfully, to avoid reprocessing.
4. **Path Tracking**: A list is used to store the sequence of matched words and print them upon success.

---

## ✅ Possible Future Enhancements

- 📊 Interactive visualization of the DP table.
- 🌐 Web version using JavaScript or Flask.
- 📋 Support for multiple valid segmentations.
- 📁 Dictionary import from a file.

---

## 📄 License

This project is free to use, modify, and distribute for educational purposes.

---

👨‍💻 Developed by **Mohamed Tamer**, **Abdelrahman Soliman**, and **Yousef Tarek** with ❤️ for the Computing Algorithms course.
