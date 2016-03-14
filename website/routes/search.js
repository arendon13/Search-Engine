var Promise = require('bluebird');
var oid = require('mongodb').ObjectID;

module.exports = function(app){

	var db = app.get('mongo');

	app.get('/search/image', function(req, res) {
		var data = [];
		results = db.collection('ImgIndex').findOneAsync({ "term" : req.query.query})
			.then(function(results){
				if (results !== null){
					var keys = Object.keys(results['locations']);
					Promise.map(keys, function(id){
						return new Promise(function(resolve, reject){
							location = db.collection('DocumentMetadata').findOneAsync({"_id" : new oid(id)})
								.then(function(location){
									data.push({ url: location['url'], prob: results['locations'][id] });
									resolve();
								})
						})
					})
					.then(function(){
						data.sort(function compare(a, b){
							if (a.prob < b.prob){
								return 1
							}
							if (a.prob > b.prob){
								return -1
							}
							return 0
						})
						res.send(data);
					})
				} else {
					res.send([]);
				}
			})
	});

	app.get('/search/text', function(req, res) {
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