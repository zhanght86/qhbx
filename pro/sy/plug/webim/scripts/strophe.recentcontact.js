//ruahoim recent contacts xep suport
Strophe.addConnectionPlugin('recentcontact', {
  _connection: null,

  init: function(connection) {
    this._connection = connection;
    Strophe.addNamespace('RECENTCONTACT', 'urn:xmpp:recentcontact');
  },

  listContacts: function(rsm, callback) {
    var xml = $iq({type: 'get', id: this._connection.getUniqueId('query')}).c('query', {xmlns: Strophe.NS.RECENTCONTACT});
    if (rsm) { xml = xml.cnode(rsm.toXML()); }
    this._connection.sendIQ(xml, this._handleListConnectionResponse.bind(this, callback));
  },
  
  _handleListConnectionResponse: function(callback, stanza) {
    var collections = [];
    var list = stanza.getElementsByTagName('item');
    for (var i = 0; i < list.length; i++) {
      var element = list[i];
      var jid = element.getAttribute('jid'); 
      var name = element.getAttribute('name');
      var subscription = element.getAttribute('subscription');
      var endtime = element.getAttribute('endtime');
      var group = Strophe.getText(element.getElementsByTagName('group')[0]);
    		  
      collections.push(new Strophe.Contact(jid, name, group, subscription, endtime));
    }
    var responseRsm = new Strophe.RSM({xml: stanza.getElementsByTagName('set')[0]});
    callback(collections, responseRsm);
  }
});


Strophe.Contact = function(jid, name, group, subscription, endtime) {
  this.jid = jid;
  this.name = name;
  this.group = group;
  this.subscription = subscription;
  this.endtime = endtime;
};


