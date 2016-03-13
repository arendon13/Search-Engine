var Promise = require('bluebird');
var oid = require('mongodb').ObjectID;
var path = require('path');

module.exports = function(app){
	var db = app.get('mongo');
	app.get('/', function(req, res) {
		res.sendFile(path.resolve('index.html'));
	});

	app.get('/img', function(req, res) {
		res.sendFile(path.resolve('public/image.html'));
	});

	// app.get('/next', function(req, res) {
	// 	var data = [];
	// 	var count = 0;
	// 	results = db.collection('Index').findOneAsync({ "term" : req.query.query})
	// 	 .then(function(results) {
	// 	 	for (var key in results['locations']){

	// 			count++;
	// 			var ln = Object.keys(results['locations']).length;
	// 			var tf = 0;
	// 	 		location = db.collection('DocumentMetadata').findOne({"_id" : new oid(key)}, function(err, result){
	// 				var e = entry();
	// 	 			e.url = result['url']
	// 	 			e.tfidf = (tf / ln );
	// 				data.push(e);

	// 	 			count--;

	// 	 			if (count === 0){
	// 	 				res.send(data);
	// 	 			}

	// 	 		})
	// 	 	}
	// 	 })
	// });

	app.get('/search', function(req, res) {
		console.log(typeof(req.query.query));
		if (req.query.query.startsWith(':image')){
			var term = req.query.query.split(" ");
			var query = term[1];
			console.log(query);
		}
		var data = [];
		results = db.collection('Index').findOneAsync({ "term" : req.query.query})
			.then(function(results){
				if (results !== null){
					var keys = Object.keys(results['locations'])
					var length = keys.length;
					Promise.map(keys, function(id){
						return new Promise(function(resolve, reject){
							location = db.collection('DocumentMetadata').findOneAsync({"_id" : new oid(id)})
								.then(function(location){
									data.push({ url: location['url'], tfidf: (results['locations'][id] / length) });
									resolve();
								})
						})
					})
					.then(function(){
					data.sort(function compare(a, b){
						if (a.tfidf < b.tfidf){
							return 1
						}
						if (a.tfidf > b.tfidf){
							return -1
						}
						return 0
					})
					res.send(data);
					// console.log(data);
					// console.log('done');
				})

				} else {
					res.send([]);
				}
			});
	})
}

function entry() {
	return {
		tfidf : 0,
		url: ''
	};
};

