var path = require('path');
/**
 * Our routes for both sides of our search engine, image and text search. 
 */
module.exports = function(app){

	var search = require('./search.js')(app);

	app.get('/', function(req, res) {
		res.sendFile(path.resolve('public/index.html'));
	});

	app.get('/img', function(req, res) {
		res.sendFile(path.resolve('public/image.html'));
	});
}

