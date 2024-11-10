// File: migrateEmails.js
const admin = require('firebase-admin');
const serviceAccount = require('./serviceAccountKey.json'); // Path to your service account key

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://<YOUR_DATABASE_NAME>.firebaseio.com" // Replace with your Realtime Database URL
});

const auth = admin.auth();
const db = admin.database();

// Function to list all users
const listAllUsers = (nextPageToken) => {
  auth.listUsers(1000, nextPageToken)
    .then((listUsersResult) => {
      listUsersResult.users.forEach((userRecord) => {
        const userId = userRecord.uid;
        const email = userRecord.email ? userRecord.email.toLowerCase() : null;

        if(email){
          // Write to Realtime Database
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
