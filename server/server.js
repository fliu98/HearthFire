var express = require('express');
var app = express();
var port = 3000;

var bodyParser = require('body-parser');

var config = require('./config');
var mongoose = require('mongoose');
var mongoUri = config.mongoUri;
var GoogleAuth = require('google-auth-library');
var googleServicesClientId = config.googleServicesClientId;

var auth = new GoogleAuth;
var client = new auth.OAuth2(googleServicesClientId, '', '');

mongoose.Promise = global.Promise;
var models = require('./model');
var DeckModel = models.Deck;
var SessionModel = models.Session;

mongoose.connect(mongoUri, { useMongoClient: true });
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function() {

	app.use(bodyParser.urlencoded({ extended: true }));
	app.use(bodyParser.json());

	app.get('/', (req, res) => {
		res.send('Server is listening');
	});

	app.post('/login', (req, res) => {
		var token = req.body['idToken'];
		client.verifyIdToken(
			token,
			googleServicesClientId,
			function(e, login) {
				if (e) {
					console.log('Failed to verify Id Token: ', e);
					res.status(401).send(e);
					return;
				}
				var payload = login.getPayload();
				var userid = payload['sub'];

				SessionModel.startSession(userid,
					function(retValue) {
						res.status(200).send(retValue);
					}
				);
			});
	});

	app.post('/save-deck', (req, res) => {
		var userId = req.body['userId'];
		var sessionKey = req.body['sessionKey'];
		SessionModel.verifySession(userId, sessionKey, function() {
		 	DeckModel.saveDeck(req.body, function(id){
		 		console.log('Deck saved: (id ' + id + ')');
		 		res.status(200).json({ deckId: id });
		 	})
		 });
	});

	app.get('/get-decks', (req, res) => {
		var userId = req.query['userId'];
		var sessionKey = req.query['sessionKey'];
		SessionModel.verifySession(userId, sessionKey, function() {
			DeckModel.getDecks(userId, function(decks) {
				console.log('Found ' + decks.length
					+ ' decks for user: ' + userId);
				res.status(200).json(decks);
			})
		});
	});

	app.listen(port, () => {
		console.log('listening on port 3000');
	});

});