var path = require('path');

module.exports = function(app){

	var db = app.get('mongo');

	var search = require('./search.js')(app);

	app.get('/', function(req, res) {
		res.sendFile(path.resolve('index.html'));
	});

	app.get('/img', function(req, res) {
		res.sendFile(path.resolve('../public/image.html'));
	});
}

