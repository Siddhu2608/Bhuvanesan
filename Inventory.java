import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.*;

public class Inventory {

    static MongoClient client;
    static MongoDatabase db;
    static MongoCollection<Document> items;

    public static void main(String[] args) {

        try {
           
            client = MongoClients.create("mongodb://localhost:27017");
            db = client.getDatabase("inventoryDB");
            items = db.getCollection("items");

            Scanner scan = new Scanner(System.in);

            while (true) {
                System.out.println("\n----- INVENTORY MANAGEMENT SYSTEM -----");
                System.out.println("1. ADD ITEMS");
                System.out.println("2. VIEW ITEMS");
                System.out.println("3. DELETE ITEMS");
                System.out.println("4. UPDATE ITEMS");
                System.out.println("5. SEARCH ITEMS");
                System.out.println("6. EXIT");
                System.out.print("Enter your choice: ");

                int choice = scan.nextInt();
                scan.nextLine();

                switch (choice) {
                    case 1:
                        additems(scan);
                        break;

                    case 2:
                        viewitems();
                        break;

                    case 3:
                        deleteitems(scan);
                        break;

                    case 4:
                        updateitems(scan);
                        break;

                    case 5:
                        searchitems(scan);
                        break;

                    case 6:
                        System.out.println("Exiting...");
                        client.close();
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void additems(Scanner scan) {
        System.out.print("Enter Product ID: ");
        int id = scan.nextInt();
        scan.nextLine();

        System.out.print("Enter Product Name: ");
        String name = scan.nextLine();

        System.out.print("Enter Quantity: ");
        int qty = scan.nextInt();

        System.out.print("Enter Price: ");
        double price = scan.nextDouble();

        Document doc = new Document("_id", id)
                .append("Name", name)
                .append("Quantity", qty)
                .append("Price", price);

        items.insertOne(doc);
        System.out.println("Item added successfully!");
    }

    static void viewitems() {
        System.out.println("--- Items in Inventory ---");
        for (Document d : items.find()) {
            System.out.println(d.toJson());
        }
    }

    static void deleteitems(Scanner scan) {
        System.out.print("Enter Item ID to delete: ");
        int id = scan.nextInt();

        items.deleteOne(Filters.eq("_id", id));
        System.out.println("Item removed successfully!");
    }

    static void updateitems(Scanner scan) {
        System.out.print("Enter Item ID to update: ");
        int id = scan.nextInt();

        System.out.print("Enter new Quantity: ");
        int qty = scan.nextInt();

        items.updateOne(
                Filters.eq("_id", id),
                new Document("$set", new Document("Quantity", qty))
        );

        System.out.println("Updated successfully!");
    }

    static void searchitems(Scanner scan) {
        System.out.print("Enter Item ID to search: ");
        int id = scan.nextInt();

        Document doc = items.find(Filters.eq("_id", id)).first();

        if (doc != null) {
            System.out.println("Item Found: " + doc.toJson());
        } else {
            System.out.println("Item not found!");
        }
    }
}
