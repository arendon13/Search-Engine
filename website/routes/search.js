var Promise = require('bluebird');
var oid = require('mongodb').ObjectID;
/**
 * Main Search functionality. Both search methods employ similar logic.
 * High-Level Overview:
 * 	1. Query db for objects matching search term.
 * 	2. Sort according to confidence (rank + tf-idf, or tag-confidence)
 * 	3. Return data once all promises have been resolved
 */
module.exports = function(app){

	var db = app.get('mongo');
	/**
	 * Route for image results.
	 * Ranked by tag confidence.
	 */
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
										url: location['url']
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

	/**
	 * Route for text results.
	 * Calculates tf-idf at runtime.
	 */
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
									var idf = Math.log10(6044 / length); 
									var tfidf = tf * idf;
									data.push({ url: location['url'], rank: ((tfidf * 0.1) + (location['rank'] * 0.9)), td_idf: tfidf, raw_rank: location['rank']});
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

/**
 * Text Comparator.
 * @returns 0, -1, or 1 based on rank comparison.
 */
function compareText(a, b){
	if (a.rank < b.rank){
		return 1
	}
	if (a.rank > b.rank){
		return -1
	}
	return 0
}
/**
 * Image comparator. 
 * @returns 0, -1, or 1 based on confidence comparison
 */
function compareImages(a, b){
	if (a.prob < b.prob){
		return 1
	}
	if (a.prob > b.prob){
		return -1
	}
	return 0

}
