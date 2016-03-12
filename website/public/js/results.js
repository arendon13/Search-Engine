var ResultList = React.createClass({
  render: function() {
    var resultNodes = this.props.data['urls'].map(function(result) {
      return (
        <div>
          <h2 className="resultHeader"> {result.split('.')[1].charAt(0).toUpperCase() + result.split('.')[1].slice(1)} </h2>
          <a className="result" href={result}> {result} </a>
        </div>
        );
      });
    return (
      <div className="result">
      <strong> TF-IDF : {this.props.data['tfIdf']} </strong> <br />
      {resultNodes}
      <strong> {resultNodes.length} Result(s) </strong>
      </div>
      );
  }
});

var SearchForm = React.createClass({
  getInitialState: function(){
    return {query: ''}
  },
  handleQueryChange: function(e) {
    this.setState( { query: e.target.value });
  },
  handleSubmit: function(e){
    e.preventDefault();
    var query = this.state.query.trim();
    this.props.onSearch(query);
    this.setState({query: ''});
  },
  render: function(){
    return (
      <form className="searchForm" onSubmit={this.handleSubmit} >
        <div className='form-group'>
        <input 
         type='text' 
         className='form-control' 
         name="query"
         value={this.state.query}
         onChange={this.handleQueryChange} />
        </div>
        <div className='form-group text-center'>
        <input type='submit' className='btn btn-primary' value='Search' />
        </div>
      </form>
      )  
  }
});

var Results = React.createClass({
  getInitialState: function() {
    return { data : {tdIdf: 0, urls : []}};
  },
  handleSearch: function(query){
    $.ajax({
            url: this.props.url,
            type: 'get',
            data: { 'query' : query },
            success: function(data) {
              this.setState({data : {tfIdf : data.tfidf, urls: data.urls }});
            }.bind(this),
            error: function(xhr, status, err) {
              console.log('fail');
            }.bind(this)
          });
  },
  render: function() {
    return(
      <div className="results">
        <SearchForm onSearch={this.handleSearch} />
        <ResultList data={this.state.data} />
      </div>
      );
  }
});
ReactDOM.render(
  <Results url='/search'/>,
  document.getElementById('content')
  );
