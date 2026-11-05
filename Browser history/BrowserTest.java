import java.util.ArrayList;
import java.util.Scanner;

public class BrowserTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BrowserHistory browser = new BrowserHistory();

        System.out.println("=================================================");
        System.out.println("  Browser Navigation Simulation (Recursion & Stack)");
        System.out.println("=================================================");

        // Prompt user for number of pages to visit
        System.out.print("\nEnter the number of web pages you want to visit: ");
        int numPages = 0;
        try {
            numPages = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Defaulting to 0 pages.");
        }

        // Store entered pages in an ArrayList
        ArrayList<String> pagesToVisit = new ArrayList<>();
        for (int i = 1; i <= numPages; i++) {
            System.out.print("Enter URL/Name for page " + i + ": ");
            String page = scanner.nextLine().trim();
            if (!page.isEmpty()) {
                pagesToVisit.add(page);
            } else {
                pagesToVisit.add("Page_" + i);
            }
        }

        // Recursively visit pages and trace pushes
        System.out.println("\n--- Pushing pages onto history stack (Recursive Visit) ---");
        browser.visitPages(pagesToVisit);

        // Display complete browser history after visits
        browser.displayHistory();

        // Prompt user for Back button count
        if (browser.getHistorySize() > 0) {
            System.out.print("\nHow many times would you like to press the Back button? ");
            int backSteps = 0;
            try {
                backSteps = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Defaulting to 0 steps.");
            }

            // Recursively navigate backwards and trace pops
            System.out.println("\n--- Popping pages from history stack (Recursive Back) ---");
            browser.goBack(backSteps);

            // Display browser history after back navigation
            browser.displayHistory();
        } else {
            System.out.println("\nNo pages visited. History is empty.");
        }

        scanner.close();
        System.out.println("\nSimulation completed successfully.");
    }
}
