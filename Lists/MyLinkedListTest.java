public class MyLinkedListTest {
    private static int assertions = 0;

    private static void check(boolean condition, String message) {
        assertions++;
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void equal(Object expected, Object actual, String message) {
        check(java.util.Objects.equals(expected, actual),
                message + " (expected: " + expected + ", actual: " + actual + ")"
        );
    }

    private static final class Item {
        final String code;
        Item(String code) { this.code = code; }

        @Override
        public boolean equals(Object other) {
            return other instanceof Item &&
                    java.util.Objects.equals(code, ((Item) other).code);
        }
        @Override
        public int hashCode() {
            return java.util.Objects.hashCode(code);
        }
        @Override
        public String toString() {
            return code;
        }
    }

    public static void main(String[] args) {
        MyLinkedList<String> words = new MyLinkedList<>();
        equal(null, words.getFirst(), "Empty list first");
        equal(null, words.getLast(), "Empty list last");
        equal(null, words.removeFirst(), "Remove from empty list");
        check(!words.delete("missing"), "Deleting from empty list returns false");

        words.append("B");
        words.prepend("A");
        words.append("C");
        words.append("D");
        equal("[A,B,C,D]", words.toString(), "Prepend and append order");
        equal("A", words.getFirst(), "Head after prepend");
        equal("D", words.getLast(), "Tail after append");

        check(words.delete("B"), "Delete middle item");
        equal("[A,C,D]", words.toString(), "Middle correctly linked");
        check(words.delete("A"), "Delete head");
        equal("C", words.getFirst(), "Head updated");
        check(words.delete("D"), "Delete tail");
        equal("C", words.getLast(), "Tail updated");
        check(!words.delete("Z"), "Unknown element not deleted");
        equal("C", words.removeFirst(), "Remove remaining node");
        equal(null, words.getFirst(), "Head reset when empty");
        equal(null, words.getLast(), "Tail reset when empty");
        words.append("again");
        equal("again", words.getLast(), "Append after list becomes empty");
        words.clear();
        equal(null, words.getFirst(), "Clear resets head");
        equal(null, words.getLast(), "Clear resets tail");

        MyLinkedList<Item> objects = new MyLinkedList<>();
        objects.append(new Item("item-one"));
        objects.append(new Item("item-two"));
        check(objects.delete(new Item("item-one")), "Delete non-Comparable generic item by equality");
        equal(new Item("item-two"), objects.getFirst(), "Remaining object intact");
        check(objects.delete(new Item("item-two")), "Delete last non-Comparable item");
        equal(null, objects.getFirst(), "Generic list becomes empty");

        MyLinkedList<String> duplicates = new MyLinkedList<>();
        duplicates.append("same");
        duplicates.append("same");
        check(duplicates.delete("same"), "Delete first matching duplicate");
        equal("[same]", duplicates.toString(), "Only one duplicate removed");

        MyLinkedList<String> nullable = new MyLinkedList<>();
        nullable.append(null);
        nullable.append("after-null");
        check(nullable.delete(null), "Null elements can be deleted safely");
        equal("after-null", nullable.getFirst(), "Head updated after null deletion");
        System.out.println("PASS: MyLinkedListTest (" + assertions + " assertions)");
    }
}//lowkey enjoy writing rhe test class
