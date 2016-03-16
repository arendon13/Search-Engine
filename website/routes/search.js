var Promise = require('bluebird');
var oid = require('mongodb').ObjectID;

module.exports = function(app){

	var db = app.get('mongo');

	app.get('/search/image', function(req, res) {
		var data = [];
		results = db.collection('ImgIndex').findOneAsync({ "tag" : req.query.query.toLowerCase()})
			.then(function(results){
				if (results !== null){

					var keys = Object.keys(results['locations']);

					Promise.map(keys, function(id){
						return new Promise(function(resolve, reject){
							location = db.collection('DocumentMetadata').findOneAsync({"_id" : new oid(id)})
								.then(function(location){
									raw_path = location['path'].split('/')[1];
									data.push({ 
										path: 'images/' + raw_path,
										prob: results['locations'][id],
										ext: location['ext'],
										url: location['url'],
								 	});
									resolve();
								})
						})
					})
					.then(function(){
						data.sort(compareImages)
						res.send(data);
					})
				} else {
					res.send([]);
				}
			})
	});

	app.get('/search/text', function(req, res) {
		var data = [];
		results = db.collection('Index').findOneAsync({ "term" : req.query.query.toLowerCase()})
			.then(function(results){
				if (results !== null){

					var keys = Object.keys(results['locations'])
					var length = keys.length;
					
					Promise.map(keys, function(id){
						return new Promise(function(resolve, reject){
							location = db.collection('DocumentMetadata').findOneAsync({"_id" : new oid(id)})
								.then(function(location){
									var totalTerms = Object.keys(location['content']).length;
									var termNum = results['locations'][id];
									var tf = Math.log(1 + (termNum / totalTerms));
									var idf = Math.log10(1150 / length);
									var tfidf = tf * idf;
									data.push({ url: location['url'], rank: ((tfidf + location['rank']) / 2), td_idf: tfidf, raw_rank: location['rank']});
									resolve();
								})
						})
					})
					.then(function(){
						data.sort(compareText);
						res.send(data);
				})

				} else {
					res.send([]);
				}
			});
	})
}

function compareText(a, b){
	if (a.rank < b.rank){
		return 1
	}
	if (a.rank > b.rank){
		return -1
	}
	return 0
}

function compareImages(a, b){
	if (a.prob < b.prob){
		return 1
	}
	if (a.prob > b.prob){
		return -1
	}
	return 0

}
