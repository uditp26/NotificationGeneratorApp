const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref("/sender/{uid}")
.onWrite(event => {
    var request = event.after.val();
    var payload = {
        notification:{
            title: "Medicine Consumption Report",
            body: "Tap to continue.",
            click_action: "RECEIVER_ACTIVITY"
        },
    };

    console.log(""+event.before.child('receiverId').val())
    admin.messaging().sendToDevice(event.before.child('receiverId').val(), payload)
    .then(function(response){
        console.log("Successfully sent message: ", response);
        return response;
    })
    .catch(function(error){
        console.log("Error sending message: ", error);
    })
})