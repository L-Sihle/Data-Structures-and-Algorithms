import java.util.Scanner;

public class HashLab {
    private static final Scanner INPUT = new Scanner(System.in);
    private static final int EMPTY = 0, OCCUPIED = 1, DELETED = 2;
    private static class Node {
        int key;
        Node next;
        Node(int key, Node next) { this.key = key; this.next = next; }
    }
    
    private static class Table {
        final int size;
        Node[] chains;
        int[] linearKeys, doubleKeys, linearState, doubleState;
        int count;
        long chainCollisions, linearCollisions, doubleCollisions;
        long chainProbes, linearProbes, doubleProbes;
        Table(int size) {
            this.size = size;
            chains = new Node[size];
            linearKeys = new int[size]; doubleKeys = new int[size];
            linearState = new int[size]; doubleState = new int[size];
        }
        int h1(int key) { return Math.floorMod(key, size); }
        int h2(int key) { return 1 + Math.floorMod(key, size - 1); }
        int index(int key, int attempt, boolean doubly) {
            return (int) ((h1(key) + (long) attempt * (doubly ? h2(key) : 1)) % size);
        }
        int findOpen(int key, boolean doubly, boolean record) {
            int[] keys = doubly ? doubleKeys : linearKeys;
            int[] states = doubly ? doubleState : linearState;
            for (int i = 0; i < size; i++) {
                int p = index(key, i, doubly);
                if (record) { if (doubly) doubleProbes++; else linearProbes++; }
                if (states[p] == EMPTY) return -1;
                if (states[p] == OCCUPIED && keys[p] == key) return p;
            }
            return -1;
        }
        boolean contains(int key) {
            int p = h1(key);
            for (Node n = chains[p]; n != null; n = n.next) if (n.key == key) return true;
            return false;
        }
        boolean search(int key) {
            int p = h1(key);
            boolean foundChain = false;
            for (Node n = chains[p]; n != null; n = n.next) {
                chainProbes++;
                if (n.key == key) { foundChain = true; break; }
            }
            boolean foundLinear = findOpen(key, false, true) >= 0;
            boolean foundDouble = findOpen(key, true, true) >= 0;
            System.out.println("Separate chaining: " + (foundChain ? "FOUND at bucket " + p : "NOT FOUND"));
            System.out.println("Linear probing: " + (foundLinear ? "FOUND" : "NOT FOUND"));
            System.out.println("Double hashing: " + (foundDouble ? "FOUND" : "NOT FOUND"));
            return foundChain && foundLinear && foundDouble;
        }
        int insertionSlot(int key, boolean doubly) {
            int[] states = doubly ? doubleState : linearState;
            int firstDeleted = -1;
            for (int i = 0; i < size; i++) {
                int p = index(key, i, doubly);
                if (states[p] == DELETED && firstDeleted < 0) firstDeleted = p;
                if (states[p] == EMPTY) return firstDeleted >= 0 ? firstDeleted : p;
            }
            return firstDeleted;
        }
        void insertOpen(int key, boolean doubly) {
            int[] keys = doubly ? doubleKeys : linearKeys;
            int[] states = doubly ? doubleState : linearState;
            int firstDeleted = -1;
            for (int i = 0; i < size; i++) {
                int p = index(key, i, doubly);
                if (doubly) doubleProbes++; else linearProbes++;
                if (states[p] == OCCUPIED) {
                    if (doubly) doubleCollisions++; else linearCollisions++;
                } else if (states[p] == DELETED) {
                    if (firstDeleted < 0) firstDeleted = p;
                } else {
                    int target = firstDeleted >= 0 ? firstDeleted : p;
                    keys[target] = key; states[target] = OCCUPIED;
                    return;
                }
            }
            if (firstDeleted >= 0) {
                keys[firstDeleted] = key; states[firstDeleted] = OCCUPIED;
            }
        }
        void insert(int key) {
            if (contains(key)) { System.out.println("Duplicate key " + key + " rejected."); return; }
            if (count == size) { System.out.println("Table capacity reached (" + size + "). Key not inserted."); return; }
            int p = h1(key);
            if (chains[p] != null) chainCollisions++;
            chains[p] = new Node(key, chains[p]);
            chainProbes++;
            insertOpen(key, false);
            insertOpen(key, true);
            count++;
            System.out.println("Inserted " + key + " into all three tables (h1=" + p + ").");
        }
        void delete(int key) {
            if (!contains(key)) { System.out.println("Key " + key + " not found; nothing deleted."); return; }
            int p = h1(key);
            Node prev = null, cur = chains[p];
            while (cur != null && cur.key != key) { chainProbes++; prev = cur; cur = cur.next; }
            chainProbes++;
            if (prev == null) chains[p] = cur.next; else prev.next = cur.next;
            int lp = findOpen(key, false, true), dp = findOpen(key, true, true);
            if (lp >= 0) linearState[lp] = DELETED;
            if (dp >= 0) doubleState[dp] = DELETED;
            count--;
            System.out.println("Deleted " + key + " from all three tables.");
        }
        void display() {
            System.out.println("\nIndex | Separate chaining       | Linear probing | Double hashing");
            System.out.println(" ");
            for (int i = 0; i < size; i++) {
                StringBuilder chain = new StringBuilder();
                for (Node n = chains[i]; n != null; n = n.next) {
                    if (chain.length() > 0) chain.append(" -> ");
                    chain.append(n.key);
                }
                System.out.printf("%-5d | %-23s | %-14s | %s%n", i,
                    chain.length() == 0 ? "-" : chain.toString(),
                    slot(linearKeys, linearState, i), slot(doubleKeys, doubleState, i));
            }
        }
        String slot(int[] keys, int[] states, int i) {
            return states[i] == EMPTY ? "EMPTY" : states[i] == DELETED ? "DELETED" : String.valueOf(keys[i]);
        }
        void stats() {
            System.out.println("\nTable size: " + size + ", keys: " + count);
            System.out.printf("Load factor (open addressing): %.2f%%%n", 100.0 * count / size);
            System.out.println("                       Collisions     Probe attempts");                //Trust me to do manual alignment ;) 
            System.out.printf("Separate chaining      %-14d %d%n", chainCollisions, chainProbes);
            System.out.printf("Linear probing         %-14d %d%n", linearCollisions, linearProbes);
            System.out.printf("Double hashing         %-14d %d%n", doubleCollisions, doubleProbes);
            System.out.println("Probe attempts count inspected slots/nodes during operations.");
            System.out.println("Collision counts record occupied-slot encounters on insertion (or nonempty chain buckets).");
            System.out.println("Statistics are cumulative since the last reset.");
        }
    }
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = INPUT.nextLine().trim();
            try { return Integer.parseInt(line); }
            catch (NumberFormatException ex) { System.out.println("Invalid integer. Please try again."); }
        }
    }
    private static boolean prime(int n) {
        if (n < 2) return false;
        for (int i = 2; (long)i * i <= n; i++) if (n % i == 0) return false;
        return true;
    }
    private static int readSize() {
        while (true) {
            int n = readInt("Enter a prime table size (at least 3): ");
            if (n >= 3 && prime(n)) return n;
            System.out.println("Table size must be a prime number >= 3 (e.g. 7, 11, 13).");
        }
    }
    private static void menu() {
        System.out.println("\n                HashLab");
        System.out.println("1) Insert one key");
        System.out.println("2) Insert several keys");
        System.out.println("3) Search for a key");
        System.out.println("4) Delete a key");
        System.out.println("5) Display all three tables");
        System.out.println("6) Statistics");
        System.out.println("7) Reset with a new table size");
        System.out.println("8) Exit");
        System.out.println(" ");
    }
    public static void main(String[] args) {
        System.out.println("Welcome to HashLab!");
        Table table = new Table(readSize());
        while (true) {
            menu();
            int choice = readInt("Choice: ");
            switch (choice) {
                case 1: table.insert(readInt("Key: ")); break;
                case 2:
                    int n = readInt("How many keys? ");
                    if (n < 0) { System.out.println("Count cannot be negative."); break; }
                    for (int i = 0; i < n; i++) table.insert(readInt("Key " + (i + 1) + ": "));
                    break;
                case 3: table.search(readInt("Key to search: ")); break;
                case 4: table.delete(readInt("Key to delete: ")); break;
                case 5: table.display(); break;
                case 6: table.stats(); break;
                case 7: table = new Table(readSize()); System.out.println("Tables reset. Previous keys and statistics cleared."); break;
                case 8: System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid menu choice. Select 1 to 8.");
            }
        }
    }
}
