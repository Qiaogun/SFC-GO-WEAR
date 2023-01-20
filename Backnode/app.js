const http = require('http');
const url = require('url');
const routes = require('./routes');

const hostname = '127.0.0.1';
const port = 5555;

const server = http.createServer((req, res) => {
  const parsedUrl = url.parse(req.url, true);
  const path = parsedUrl.pathname;
  const handler = routes[path] || notFound;
  handler(req, res);
});

function notFound(req, res) {
    res.statusCode = 404;
    res.end('404 Not Found');
}

server.listen(port, hostname, () => {
  console.log(`Server running at http://${hostname}:${port}/`);
});
