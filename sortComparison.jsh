import java.io.*;
import java.nio.file.*;
import java.util.*;

// Method to compare two cards based on rank and suit
public static int cardCompare(String card1, String card2) {
    int rank1 = getRank(card1);
    int suit1 = getSuitPriority(card1.charAt(card1.length() - 1));
    int rank2 = getRank(card2);
    int suit2 = getSuitPriority(card2.charAt(card2.length() - 1));

    if (suit1 != suit2) {
        return Integer.compare(suit1, suit2);
    }
    return Integer.compare(rank1, rank2);
}

// Get the rank of a card (Ace, Jack, Queen, King or numeric)
private static int getRank(String card) {
    String rankStr = card.substring(0, card.length() - 1);
    switch (rankStr) {
        case "Ace": case "A": return 1;
        case "Jack": case "J": return 11;
        case "Queen": case "Q": return 12;
        case "King": case "K": return 13;
        default: return Integer.parseInt(rankStr);
    }
}

// Get the suit priority (H: Hearts, C: Clubs, D: Diamonds, S: Spades)
private static int getSuitPriority(char suit) {
    switch (suit) {
        case 'H': return 0; // Hearts
        case 'C': return 1; // Clubs
        case 'D': return 2; // Diamonds
        case 'S': return 3; // Spades
        default: throw new IllegalArgumentException("Invalid suit: " + suit);
    }
}

// Bubble Sort implementation
public static ArrayList<String> bubbleSort(ArrayList<String> list) {
    boolean swapped;
    do {
        swapped = false;
        for (int i = 0; i < list.size() - 1; i++) {
            String card1 = list.get(i);
            String card2 = list.get(i + 1);
            if (cardCompare(card1, card2) > 0) {
                Collections.swap(list, i, i + 1);
                swapped = true;
            }
        }
    } while (swapped);
    return list;
}

// Merge Sort implementation
public static List<String> mergeSort(List<String> list) {
    if (list.size() <= 1) {
        return list;
    }
    int mid = list.size() / 2;
    List<String> left = mergeSort(list.subList(0, mid));
    List<String> right = mergeSort(list.subList(mid, list.size()));
    return merge(left, right);
}

// Merge two sorted lists
private static List<String> merge(List<String> left, List<String> right) {
    List<String> result = new ArrayList<>();
    int i = 0, j = 0;
    while (i < left.size() && j < right.size()) {
        if (cardCompare(left.get(i), right.get(j)) <= 0) {
            result.add(left.get(i));
            i++;
        } else {
            result.add(right.get(j));
            j++;
        }
    }

    result.addAll(left.subList(i, left.size()));
    result.addAll(right.subList(j, right.size()));
    return result;
}

// Method to measure the execution time of Bubble Sort
public static long measureBubbleSort(String filename) throws IOException {
    ArrayList<String> cards = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            cards.add(line.trim());
        }
    }

    long startTime = System.currentTimeMillis();
    bubbleSort(cards);
    return System.currentTimeMillis() - startTime;
}

// Method to measure the execution time of Merge Sort
public static long measureMergeSort(String filename) throws IOException {
    List<String> cards = Files.readAllLines(Paths.get(filename));
    long startTime = System.currentTimeMillis();
    mergeSort(cards);
    return System.currentTimeMillis() - startTime;
}

// Method to compare Bubble Sort and Merge Sort
public static void sortComparison(String[] filenames) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("sortComparison.csv"))) {
        writer.write(", " + filenames[0].replaceAll("\\D+", "") + ", " + filenames[1].replaceAll("\\D+", "") + ", " + filenames[2].replaceAll("\\D+", ""));
        writer.newLine();

        writer.write("bubbleSort, ");
        for (String filename : filenames) {
            long time = measureBubbleSort(filename);
            writer.write(time + ", ");
        }
        writer.newLine();

        writer.write("mergeSort, ");
        for (String filename : filenames) {
            long time = measureMergeSort(filename);
            writer.write(time + ", ");
        }
        writer.newLine();
    }
    printCSVResults();
}

// Helper method to print the results from the CSV file to the console
public static void printCSVResults() throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader("sortComparison.csv"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}

// Main method to run the sort comparison
public static void main(String[] args) throws IOException {
    String[] filenames = {"sort10.txt", "sort100.txt", "sort10000.txt"};
    sortComparison(filenames);
    System.out.println("Comparison results written to sortComparison.csv");
}
