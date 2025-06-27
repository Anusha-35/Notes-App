📒 NotesApp – Java File I/O Notes Manager with User Login
This is a Java console application that lets each user register, log in, and manage their own notes. Notes are saved to files, allowing persistent storage with per-user separation.

✅ Features
🔐 Register/Login – New and existing users authenticate using usernames and hashed passwords.

📂 Per-User Notes – Each user has a unique notes file (notes_<username>.txt).

📝 Write Notes – Overwrite or append notes with titles and timestamps.

🔎 Search Notes – Find notes containing specific keywords.

📖 Read Notes – View all saved notes for the logged-in user.

🗑 Delete Notes – Option to delete all notes for the current user.

🔑 Password Hashing – Passwords are securely stored using SHA-256 (basic security).

📂 Files
NotesApp.java – Main application source code.

users.txt – Automatically created; stores registered usernames and password hashes.

notes_<username>.txt – Notes file for each registered user (e.g., notes_alice.txt).

🗝 Example Usage
pgsql
Copy
Edit
1. Register
2. Login
3. Exit
Choose an option (1-3): 1
Choose a username: alice
Choose a password: secret123
User registered successfully!

1. Register
2. Login
3. Exit
Choose an option (1-3): 2
Enter username: alice
Enter password: secret123
Login successful! Welcome, alice!
📌 Key Concepts Demonstrated
File I/O: FileReader, BufferedReader, FileWriter.

Exception handling and try-with-resources.

Password hashing with SHA-256.

Basic user authentication and persistent user data.

Per-user data storage.
