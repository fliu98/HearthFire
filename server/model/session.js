const mongoose = require('mongoose');

var SessionSchema = new mongoose.Schema({
	user_id: {
		type: String,
		required: true,
		index: true
	},
	session_key: {
		type: String,
		required: true
	}
}, {timestamps: true});

var Session = module.exports = mongoose.model('Session', SessionSchema);

module.exports.startSession = function(userId, respond) {
	Session.findOne({ 'user_id': userId }, 'session_key',
	 function(err, session) {

	 	if (err) {
	 		console.log(err);
	 	}

	 	var sessionKey = generateRandomKey();

	 	if (session) {
	 		session.session_key = sessionKey;
	 		session.save(function (err, session) {
	 			if (err) {
	 				console.log(err);
	 			}
	 		});
	 	} else {
	 		var newSession = new Session(
	 			{ user_id: userId,
	 			 session_key: sessionKey });
	 		newSession.save(function (err, newSession) {
	 			if (err) {
	 				console.log(err);
	 			}
	 		});
	 	}

	 	respond(userId + '-' + sessionKey);
	});
}

module.exports.verifySession = function(userId, providedKey, next) {
	Session.findOne({ 'user_id': userId}, 'session_key',
		function(err, session) {

			if (err) {
				console.log(err);
			}

			if (session) {
				if (session.session_key === providedKey) {
					console.log('Verification successful for user: ' + userId);
					next(userId);
				}
			} else {
				console.log('No session found for user: ' + userId);
			}
	});
}

function generateRandomKey() {
	return parseInt(Math.random() * (90000000 - 10000000) + 10000000);
}