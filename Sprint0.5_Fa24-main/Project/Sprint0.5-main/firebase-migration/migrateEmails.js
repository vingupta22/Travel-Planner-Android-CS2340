// File: migrateEmails.js

const admin = require('firebase-admin');
const serviceAccount = require('./serviceAccountKey.json'); // Ensure this path is correct

// Replace with your actual Realtime Database URL
const databaseURL = "https://cs2340facteam41-default-rtdb.firebaseio.com/"; // Example URL

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: databaseURL
});

const auth = admin.auth();
const db = admin.database();

// Function to list all users and migrate their emails
const listAllUsers = (nextPageToken) => {
  auth.listUsers(1000, nextPageToken)
    .then((listUsersResult) => {
      listUsersResult.users.forEach((userRecord) => {
        const userId = userRecord.uid;
        const email = userRecord.email ? userRecord.email.toLowerCase() : null;

        if (email) {
          // Write to Realtime Database under 'users/{userId}/email'
          db.ref('users/' + userId).update({
            email: email
          })
          .then(() => {
            console.log(`Stored email for user: ${email}`);
          })
          .catch((error) => {
            console.error(`Error storing email for user: ${email}`, error);
          });
        }
      });

      if (listUsersResult.pageToken) {
        // List next batch of users.
        listAllUsers(listUsersResult.pageToken);
      }
    })
    .catch((error) => {
      console.error('Error listing users:', error);
    });
};

// Start listing users from the beginning, 1000 at a time.
listAllUsers();
