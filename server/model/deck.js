const mongoose = require('mongoose');

var DeckSchema = new mongoose.Schema({
	deck_id: {
		type: String,
		required:true,
		index: true,
		unique: true
	},
	name: {
		type: String,
		required: true
	},
	creator_id: {
		type: String,
		required: true,
		index: true
	},
	hero_class: {
		type: Number,
		required: true
	},
	description: {
		type: String
	},
	deck_list: {
		type: [Number],
		required: true
	}
}, {timestamps: true});

var Deck = module.exports = mongoose.model('Deck', DeckSchema);

module.exports.saveDeck = function(deckObject, next) {
	var deckId = deckObject['deckId'];

	if (deckId) {
		Deck.findOne({ 'deck_id': deckId }, 'name creator_id description deck_list',
			function(err, deck) {

				if (err) {
					console.log(err);
				}

				if (deck) {
					deck.name = deckObject['deckName'];
					deck.creator_id = deckObject['userId'];
					deck.hero_class =deckObject['heroClass'];
					deck.description = deckObject['deckDescription'];
					deck.deck_list = JSON.parse(deckObject['deckList']);
					deck.save(function (err, deck) {
						if (err) {
							console.log(err);
						}
					});
					next(deckObject['deckId']);
				} else {
					console.log("No deck found with id: " + deckId);
				}
		});
	} else {
		generateUniqueId( function(id) {
			var newDeck = new Deck({
				deck_id: id,
				name: deckObject['deckName'],
				creator_id: deckObject['userId'],
				hero_class: deckObject['heroClass'],
				description: deckObject['deckDescription'],
				deck_list: JSON.parse(deckObject['deckList'])
			});
			newDeck.save(function (err, deck) {
				if (err) {
					console.log(err);
				}
			});
			next(id);
		});
	}
}

module.exports.getDecks = function(creatorId, next) {
	Deck.find({ 'creator_id': creatorId }, function (err, decks) {
		if (err) {
			console.log(err);
		}
		next(decks);
	});
}

function generateUniqueId(next) {
	var id = parseInt(Math.random() * (900000000 - 100000000) + 100000000);
	Deck.findOne({ 'deck_id': id }, 'deck_id', function (err, deck) {
		if (err) {
			console.log(err);
		}
		if (deck) {
			generateUniqueId();
		} else {
			next(id);
		}
	});
}