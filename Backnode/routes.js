const routes = {
    '/hello': handleHello,
    '/goodbye': handleGoodbye,
    // other routes
  };
  
  function handleHello(req, res) {
    res.statusCode = 200;
    res.setHeader('Content-Type', 'text/plain');
    res.end('Hello World\n');
  }
  
  function handleGoodbye(req, res) {
    res.statusCode = 200;
    res.setHeader('Content-Type', 'text/plain');
    res.end('Goodbye World\n');
  }
  
  module.exports = routes;
  