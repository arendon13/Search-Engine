var Details = React.createClass({
  render: function() {
    return ( <span>
              <div className="img-details text-center">
                <h3>Details</h3><br />
                <img className="custom-thumbnail img-responsive center-block" src={this.props.data.path} /><br />
                <strong>Probability:</strong> {this.props.data.prob}<br />
                <strong>Source:</strong> <a href={this.props.data.url}> {this.props.data.url} </a> <br />
                <strong> Local Path: </strong> {this.props.data.path} <br />
                <button className="btn btn-primary btn-block" onClick={this.props.onClick}> Back </button>
              </div>
             </span> );
  }
});

var Image = React.createClass({
  getInitialState: function() {
    return { showDetails: false }
  },
  handleClick: function() {
    console.log('Clicking');
    this.setState({ showDetails: !this.state.showDetails });
  },
  render: function() {
    return (
            <span>
              {this.state.showDetails ? <Details data={this.props.data} onClick={this.handleClick}/> : <img className='img-result' src={this.props.data.path} onClick={this.handleClick} /> }
            </span>
          );
  }
});

var ResultList = React.createClass({
  render: function() {
    var resultNodes = this.props.data.map(function(result, i) {
      return (
        <Image data={result} key={i} />
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
  },
  render: function(){
    return (
      <form className="searchForm" onSubmit={this.handleSubmit} >
        <div className='form-group'>
          <div className='input-group'>
            <input 
              type='text' 
              className='form-control' 
              name="query"
              value={this.state.query}
              onChange={this.handleQueryChange} />
            <span className="input-group-btn">
              <input type='submit' className='btn btn-primary' value='Search' />
            </span>
          </div>
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
  <Results url='/search/image'/>,
  document.getElementById('content')
  );
