<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
          "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
  <head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">
    <title>webim</title>
    <%@ include file="/sy/base/view/inHeader.jsp"%>

    <link rel='stylesheet' href='./jquery-ui.css'>
    <script src='./scripts/strophe.js'></script>
    <script src='./scripts/flXHR.js'></script>
    <script src='./scripts/strophe.flxhr.js'></script>
    <script src='./scripts/iso8601_support.js'></script>
    <script src='./scripts/strophe.rsm.js'></script>
    <script src='./scripts/strophe.archive.js'></script>
    <script src='./scripts/strophe.hismsg.js'></script>
     <script src='./scripts/strophe.recentcontact.js'></script>
    
    
  <script type="text/javascript" src="/sy/base/frame/coms/file/swfupload.js"></script>
  <script type="text/javascript" src="/sy/base/frame/coms/file/js/swfupload.queue.js"></script>
  <script type="text/javascript" src="/sy/base/frame/coms/file/js/fileprogress.js"></script>
  <script type="text/javascript" src="/sy/base/frame/coms/file/js/handlers.js"></script>
   

    <link rel='stylesheet' href='gab.css'>
    <script src='gab.js'></script>
  </head>
  <body>
    <h1>Gab</h1>

    <div id='toolbar'>
      <span class='button' id='new-contact'>add contact...</span> ||
      <span class='button' id='new-chat'>chat with...</span> ||
      <span class='button' id='disconnect'>disconnect</span>
    </div>

    <div id='chat-area'>
      <ul></ul>

    </div>
    
    <div id='roster-area'>
      <ul></ul>
    </div>

    <!-- login dialog -->
    <div id='login_dialog' class='hidden'>
      <label>JID:</label><input type='text' id='jid' value='liwei@rhim.server'>
      <label>Password:</label><input type='password' id='password'>
    </div>

    <!-- contact dialog -->
    <div id='contact_dialog' class='hidden'>
      <label>JID:</label><input type='text' id='contact-jid'>
      <label>Name:</label><input type='text' id='contact-name'>
    </div>

    <!-- chat dialog -->
    <div id='chat_dialog' class='hidden'>
      <label>JID:</label><input type='text' id='chat-jid'>
    </div>

    <!-- approval dialog -->
    <div id='approve_dialog' class='hidden'>
      <p><span id='approve-jid'></span> has requested a subscription
        to your presence.  Approve or deny?</p>
    </div>
  </body>
</html>
