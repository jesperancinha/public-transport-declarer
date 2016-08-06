var express = require('express'),
    bodyParser = require('body-parser'),
    compression = require('compression'),
    cors = require('cors'),
    players = require('./server/players-service'),
    sessions = require('./server/sessions-service'),
    jwt = require('jsonwebtoken'),
    _       = require('lodash'),
    http = require('http'),
    app = express();

var users = [{
  id: 1,
  username: 'joao',
  password: 'joao'
}];

function createToken(user) {
  return jwt.sign(_.omit(user, 'password'), 'this is a secret', { });
}


app.use(cors());
app.use(bodyParser.json());
app.use(compression());

app.use('/', express.static(__dirname + '/www'));

app.get('/players', players.findAll);

app.post('/sessions/create', function(req, res) {
  var user = _.find(users, {username: req.body.username});

  res.status(201).send({
    id_token: createToken(user)
  });
});


var port = process.env.PORT || 3002;

http.createServer(app).listen(port, function (err) {
  console.log('listening in http://localhost:' + port);
});

