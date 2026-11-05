import java.util.ArrayList;
import java.util.Scanner;

public class BrowserTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BrowserHistory browser = new BrowserHistory();
        
        System.out.println(" Browser Navigation Simulation (Recursion & Stack)");
        System.out.print("\nEnter the number of web pages you want to visit: ");
        
        int numPages = 0;
        try {
            numPages = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invaid input.");
        }

        ArrayList<String> pagesToVisit = new ArrayList<>();
        for (int i = 1; i <= numPages; i++) {
            System.out.print("Enter Name for page " + i + ": ");
            String page = scanner.nextLine().trim();
            if (!page.isEmpty()) {
                pagesToVisit.add(page);
            } else {
                pagesToVisit.add("Page_" + i);
            }
        }

        System.out.println("\nPushing pages onto history stack");
        browser.visitPages(pagesToVisit);
        browser.displayHistory();

        if (browser.getHistorySize() > 0) {
            System.out.print("\nHow many times would you like to press the Back button? ");
            int backSteps = 0;
            try {
                backSteps = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
            System.out.println("\nPopping pages from history stack");
            browser.goBack(backSteps);
            browser.displayHistory();
        } else {
            System.out.println("\nNo pages visited. History is empty.");
        }

        scanner.close();
        System.out.println("Simulation completed successfully.");
    }
}
