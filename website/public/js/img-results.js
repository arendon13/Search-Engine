var ResultList = React.createClass({
  render: function() {
    var resultNodes = this.props.data.map(function(result) {
      return (
					<a href={result.url}>
						<img className='img-result' src={result.url} />
					</a>
        );
      });
    return (
      <div className="result">
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
    return { data : []};
  },
  handleSearch: function(query){
    $.ajax({
            url: this.props.url,
            type: 'get',
            data: { 'query' : query },
            success: function(data) {
              console.log(data);
              this.setState({data : data});
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
  <Results url='/img-search'/>,
  document.getElementById('content')
  );
