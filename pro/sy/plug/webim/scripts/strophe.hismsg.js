//ruahoim history message xep suport
Strophe.addConnectionPlugin('hismsg', {
  _connection: null,

  init: function(connection) {
    this._connection = connection;
    Strophe.addNamespace('HISMSG', 'urn:xmpp:hismsg');
  },

  /**
   * @param jid - 目标用户
   * @param rsm - 扩展参数（可选）
   * @param callback - callback
   * @param beforeId - after message Id (可选)
   * 
   */
  listHisMsgs: function(jid, rsm, callback, beforeId) {
    var xml = ""; 
    if (beforeId) {
    	xml = $iq({type: 'get', id: this._connection.getUniqueId('list')}).c('list', {xmlns: Strophe.NS.HISMSG, 'with': jid, 'before':beforeId});
    } else {
    	xml = $iq({type: 'get', id: this._connection.getUniqueId('list')}).c('list', {xmlns: Strophe.NS.HISMSG, 'with': jid});
    }
    if (rsm) { xml = xml.cnode(rsm.toXML()); }
    this._connection.sendIQ(xml, this._handleListConnectionResponse.bind(this, callback));
  },
  
  _handleListConnectionResponse: function(callback, stanza) {
    var collections = [];
    var chats = stanza.getElementsByTagName('chat');
    for (var i = 0; i < chats.length; i++) {
      var element = chats[i];
      var to = element.getAttribute('with'); 
      var from = element.getAttribute('owner');
      var time = element.getAttribute('time');
      var msgid = element.getAttribute("msgid");
      var body = Strophe.getText(element.getElementsByTagName('body')[0]);
      var newTimestamp = new Date().setTime(time);
    		  
      collections.push(new Strophe.ArchivedMessage(newTimestamp, from, to, body, msgid));
    }
    var responseRsm = new Strophe.RSM({xml: stanza.getElementsByTagName('set')[0]});
    callback(collections, responseRsm);
  }
});

Strophe.ArchivedCollection = function(connection, jid, start) {
  this.connection = connection;
  this.jid = jid;
  this.start = start;
  this.startDate = (new Date()).setISO8601(start);
};

Strophe.ArchivedMessage = function(timestamp, from, to, body, id) {
  this.id = id;
  this.timestamp = timestamp;
  this.from = from;
  this.to = to;
  this.body = body;
};


