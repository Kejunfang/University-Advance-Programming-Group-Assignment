## File Structure
1. **Data** : Store the data, include operator, student and ticket, all the format is in .txt file
2. **Application** : The pages that will show to the user, include login page, admin page, operator page, and user page.
3. **bean**: Store the data of the database, user can take and store the data through these file,we store Operator entity, Student entity,and Ticket entity.
4. **DBHelper** : Help to store the data in the system.

## Pages

### Login Page
1. Have 3 kind of user: User, Operator and Admin
2. If the user are Typing the wrong password, user are not able to log in.
3. If the user are new, which mean there are no username and password in the text file

### User Page
1. User can write the ticket information in the page, which is can write issue type, and the description
2. After user have write the ticket, the ticket will show at the table, if the ticket is resolve, the ticket will show at the resolve table.
3. User can edit their profile if their want.

### Operator Page
1. Operator will need to write the reply for the ticket that is assigned by the admin
2. Operator can change the progress of the ticket , and write the note to the user, user will see the note that operator write.

### Admin Page
1. There is total three control pages in admin page
2. admin can assign the ticket to the operator
3. Admin can edit the user(add/delete)
4. Admin can edit the Operator(add/delete)


## Environment
1. Programming Language: Java, SDK: 24
2. Software Platform: IntelliJ IDEA
3. Database: Text File
4. Database Visualization: IntelliJ IDEA
5. UI Design: JavaFX, version 24.0.1



## Configuration (Must Read):
1. First, download the JavaFX SDK, version: **24.0.1**
2. Once the download is complete, open your IDE and then click on **Project Structure**
<img width="1470" alt="Screenshot 2025-06-25 at 17 58 22" src="https://github.com/user-attachments/assets/7a0077cb-6ae5-4a93-8242-6bac6ab93c19" />

3. Next, open **Global Libraries**, and add the JAR files from the lib folder inside the JavaFX **24.0.1** SDK you downloaded.
<img width="1470" alt="Screenshot 2025-06-25 at 17 59 39" src="https://github.com/user-attachments/assets/fb7a6120-2208-4d41-9b22-08f05ceec37a" />

4. After importing, right-click on the library you just added, then select "Add to Modules" to include it in the program you want to run.
   After that, click Apply, then OK.
<img width="1470" alt="Screenshot 2025-06-25 at 18 01 32" src="https://github.com/user-attachments/assets/a91018fe-474a-4aef-9cbe-690d33da7825" />


## Run Configuration (Must Read):
1. Click on the class you are currently running, then click Edit Configurations.
 <img width="1470" alt="Screenshot 2025-06-25 at 18 02 41" src="https://github.com/user-attachments/assets/b90ca7a9-2e4e-495c-a365-b33446d8c9bf" />

2. Click **Add New (+)**, then select **Application**.
<img width="1470" alt="Screenshot 2025-06-25 at 18 03 22" src="https://github.com/user-attachments/assets/00941e67-b374-4ba2-a1ba-a61b8976bddf" />

3. Then, in the **Main class** field, select the file you want to run.
<img width="1470" alt="Screenshot 2025-06-25 at 18 04 33" src="https://github.com/user-attachments/assets/4c3625f1-6932-43c3-a00f-c46b9bcc4df7" />

4. Then, under **Modify options**, select **Add VM options** to add virtual machine options.
<img width="1470" alt="Screenshot 2025-06-25 at 18 05 16" src="https://github.com/user-attachments/assets/d127edf2-e2d3-41e4-a939-fdd7593d5d2b" />

5. Now it's time to add the VM options.
---

**💻 macOS Example:**

```
--module-path /Users/hillzhang/Libraries/javafx-sdk-24.0.1/lib --add-modules javafx.controls,javafx.fxml
```

> 🟢 *Note: `/path/to/javafx-sdk-24.0.1/lib` refers to the path where you downloaded the JavaFX SDK on your computer.*

---

**🪟 Windows Example:**

```
--module-path "C:\path\to\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml
```

> 🟢 *Make sure to include quotes ("") if your path contains spaces.*



6. After clicking **Apply**, just hit **Run**—and your JavaFX application should run smoothly!