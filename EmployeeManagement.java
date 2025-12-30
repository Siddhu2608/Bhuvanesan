import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.*;

public class EmployeeManagement{
    static MongoClient client;
    static MongoDatabase db;
    static MongoCollection<Document> details;
    public static void main(String args[]){
    try{
        client=MongoClients.create("mongodb://localhost:27017");
        db=client.getDatabase("Employee");
        details=db.getCollection("details");

        Scanner scan=new Scanner(System.in);

        while(true){
            System.out.println("-----EMPLOYEE MANAGEMENT SYSTEM");
            System.out.println("1.ADD EMPLOYEE");
            System.out.println("2.SEARCH EMPLOYEE");
            System.out.println("3.REMOVE EMPLOYEE");
            System.out.println("4.VIEW EMPLOYEES");
            System.out.println("5.ASSIGN PROJECTS");
            System.out.println("6.EXIT");
            System.out.println("Enter Your Choice : ");
            int choice=scan.nextInt();
            scan.nextLine();
            switch (choice) {
                    case 1:
                        addEmployee(scan);
                        break;

                    case 2:
                        searchEmployee(scan);
                        break;

                    case 3:
                        removeEmployee(scan);
                        break;

                    case 4:
                        viewEmployee();
                        break;

                    case 5:
                        assignProject(scan);
                        break;

                    case 6:
                        System.out.println("Exiting...");
                        client.close();
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    static void addEmployee(Scanner scan){
        System.out.println("Enter Employee ID : ");
        int empId=scan.nextInt();
        scan.nextLine();
        System.out.println("Enter Employee Name : ");
        String empName=scan.nextLine();
        System.out.println("Enter the role of the Employee : ");
        String empRole=scan.nextLine();
        System.out.println("Enter the salary for the Role : ");
        double empSalary=scan.nextDouble();

        Document d=new Document("_id",empId)
                    .append("Name",empName)
                    .append("Role",empRole)
                    .append("Salary",empSalary);

        details.insertOne(d);
        System.out.println("The Employee " + empName + " is Successfully Appointed for the Role of " + empRole);
    }

    static void searchEmployee(Scanner scan){
        System.out.println("Enter Employee ID to Search : ");
        int empId=scan.nextInt();
        scan.nextLine();

        Document d=details.find(Filters.eq("_id", empId)).first();

        if(d!=null){
            System.out.println("Employee details are Found Successfully :"+d.toJson());
        }else{
            System.out.println("There is no Employee Present with the given ID");
        }
    }

    static void removeEmployee(Scanner scan){
        System.out.println("Enter Employee ID to Remove : ");
        int empId=scan.nextInt();
        scan.nextLine();

        details.deleteOne(Filters.eq("_id", empId));
        System.out.println("The Employee has been removed due to his/her poor working");
    }

    static void viewEmployee(){
        System.out.println("-----EMPLOYEE DETAILS-----");
        for(Document d:details.find()){
            System.out.println(d.toJson());
        }
    }

    static void assignProject(Scanner scan){
        System.out.println("Enter Employee ID to Assign Projects : ");
        int empId=scan.nextInt();
        scan.nextLine();

        Document doc=details.find(Filters.eq("_id", empId)).first();

        String role=doc.getString("Role");
        if(role.equals("EMPLOYEE")){
            System.out.println("Enter the Project to be assigned : ");
            String empProject=scan.nextLine();

            details.updateOne(
                Filters.eq("_id", empId),
                new Document("$push",new Document("Projects", empProject))
            );
            System.out.println("The project has been Successfully assigned to the Employee");
        }
        else if(role.equals("MANAGER")){
            System.out.println("Enter the Project to be assigned : ");
            String empProject=scan.nextLine();

            details.updateOne(
                Filters.eq("_id", empId),
                new Document("$push",new Document("Projects", empProject))
            );
            System.out.println("The project has been Successfully assigned to the Manager");
        }
        else if(role.equals("ADMIN")){
            System.out.println("Enter the Project to be assigned : ");
            String empProject=scan.nextLine();

            details.updateOne(
                Filters.eq("_id", empId),
                new Document("$push",new Document("Projects", empProject))
            );
            System.out.println("The project has been Successfully assigned to the Admin");
        }
    }
}
