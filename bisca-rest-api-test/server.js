var express = require('express'),
    bodyParser = require('body-parser'),
    compression = require('compression'),
    cors = require('cors'),
    players = require('./server/players-service'),
    app = express();

app.set('port', process.env.PORT || 3002);

app.use(cors());
app.use(bodyParser.json());
app.use(compression());

app.use('/', express.static(__dirname + '/www'));

app.get('/players', players.findAll);

app.listen(app.get('port'), function () {
    console.log('Realty server listening on port ' + app.get('port'));
});
