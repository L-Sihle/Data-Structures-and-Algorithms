import java.util.ArrayList;
import java.util.Stack;

public class BrowserHistory {
    private Stack<String> historyStack;

    public BrowserHistory() {
        historyStack = new Stack<>();
    }
    public void visitPages(ArrayList<String> pages) {
        visitPages(pages, 0);
    }
    public void visitPages(ArrayList<String> pages, int index) {
        if (pages == null || index >= pages.size()) {
            return;
        }
        String page = pages.get(index);
        historyStack.push(page);
        System.out.println("[TRACE] Pushed onto stack: " + page);

        visitPages(pages, index + 1);
    }

    public void goBack(int steps) {
        if (steps <= 0) {
            return;
        }
        if (historyStack.isEmpty()) {
            System.out.println("Cannot go back. Browser history is empty.");
            return;
        }
        if (historyStack.size() == 1) {
            System.out.println("Reached the first page: " + historyStack.peek());
            return;
        }
        String poppedPage = historyStack.pop();
        System.out.println("[TRACE] Popped from stack: " + poppedPage);
        goBack(steps - 1);
    }
    public void goBack() {
        goBack(1);
    }

    public void displayHistory() {
        if (historyStack.isEmpty()) {
            System.out.println("\nBrowser History: [Empty]");
            return;
        }

        System.out.println("\nCurrent Browser History");
        for (int i = 0; i < historyStack.size(); i++) {
            String page = historyStack.get(i);
            if (i == historyStack.size() - 1) {
                System.out.println((i + 1) + ". " + page + "  <-- [CURRENT PAGE]");
            } else {
                System.out.println((i + 1) + ". " + page);
            }
        }
    }

    public int getHistorySize() {
        return historyStack.size();
    }
    public String getCurrentPage() {
        if (historyStack.isEmpty()) {
            return "None";
        }
        return historyStack.peek();
    }
}
