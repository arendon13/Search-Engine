var express = require('express');
var path = require('path');
var app = express();
var MongoClient = require('mongodb').MongoClient;
var collection = require('mongodb').Collection;
var db;
var url = 'mongodb://localhost:27017/CrawledData'
var Promise = require('bluebird');

Promise.promisifyAll(MongoClient);
Promise.promisifyAll(collection.prototype);

//set static directories
app.use(express.static(__dirname + '/public'));
app.use('/images', express.static(__dirname + '/../CrawlerStorage'));

MongoClient.connect(url, function(err, database) {

	if (err) {
		console.log('Something went wrong. Is MongoDB running?');
		throw err;
	}
	// Set mongo object for use in other parts of the server application.
	app.set('mongo', database);
	require('./routes')(app);

	// Start Listening
	app.listen(8080, function(err){
		console.log('Listening on port 8080');
	});

});
