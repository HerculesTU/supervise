(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"),require("../markdown/markdown"),require("../../addon/mode/overlay"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror","../markdown/markdown","../../addon/mode/overlay"],A)}else{A(CodeMirror)}}})(function(B){var A=/^((?:(?:aaas?|about|acap|adiumxtra|af[ps]|aim|apt|attachment|aw|beshare|bitcoin|bolo|callto|cap|chrome(?:-extension)?|cid|coap|com-eventbrite-attendee|content|crid|cvs|data|dav|dict|dlna-(?:playcontainer|playsingle)|dns|doi|dtn|dvb|ed2k|facetime|feed|file|finger|fish|ftp|geo|gg|git|gizmoproject|go|gopher|gtalk|h323|hcp|https?|iax|icap|icon|im|imap|info|ipn|ipp|irc[6s]?|iris(?:\.beep|\.lwz|\.xpc|\.xpcs)?|itms|jar|javascript|jms|keyparc|lastfm|ldaps?|magnet|mailto|maps|market|message|mid|mms|ms-help|msnim|msrps?|mtqp|mumble|mupdate|mvn|news|nfs|nih?|nntp|notes|oid|opaquelocktoken|palm|paparazzi|platform|pop|pres|proxy|psyc|query|res(?:ource)?|rmi|rsync|rtmp|rtsp|secondlife|service|session|sftp|sgn|shttp|sieve|sips?|skype|sm[bs]|snmp|soap\.beeps?|soldat|spotify|ssh|steam|svn|tag|teamspeak|tel(?:net)?|tftp|things|thismessage|tip|tn3270|tv|udp|unreal|urn|ut2004|vemmi|ventrilo|view-source|webcal|wss?|wtai|wyciwyg|xcon(?:-userid)?|xfire|xmlrpc\.beeps?|xmpp|xri|ymsgr|z39\.50[rs]?):(?:\/{1,3}|[a-z0-9%])|www\d{0,3}[.]|[a-z0-9.\-]+[.][a-z]{2,4}\/)(?:[^\s()<>]|\([^\s()<>]*\))+(?:\([^\s()<>]*\)|[^\s`*!()\[\]{};:'".,<>?«»“”‘’]))/i;B.defineMode("gfm",function(C,H){var G=0;function F(J){J.code=false;return null}var E={startState:function(){return{code:false,codeBlock:false,ateSpace:false}},copyState:function(J){return{code:J.code,codeBlock:J.codeBlock,ateSpace:J.ateSpace}},token:function(M,L){L.combineTokens=null;if(L.codeBlock){if(M.match(/^```+/)){L.codeBlock=false;return null}M.skipToEnd();return null}if(M.sol()){L.code=false}if(M.sol()&&M.match(/^```+/)){M.skipToEnd();L.codeBlock=true;return null}if(M.peek()==="`"){M.next();var K=M.pos;M.eatWhile("`");var J=1+M.pos-K;if(!L.code){G=J;L.code=true}else{if(J===G){L.code=false}}return null}else{if(L.code){M.next();return null}}if(M.eatSpace()){L.ateSpace=true;return null}if(M.sol()||L.ateSpace){L.ateSpace=false;if(H.gitHubSpice!==false){if(M.match(/^(?:[a-zA-Z0-9\-_]+\/)?(?:[a-zA-Z0-9\-_]+@)?(?:[a-f0-9]{7,40}\b)/)){L.combineTokens=true;return"link"}else{if(M.match(/^(?:[a-zA-Z0-9\-_]+\/)?(?:[a-zA-Z0-9\-_]+)?#[0-9]+\b/)){L.combineTokens=true;return"link"}}}}if(M.match(A)&&M.string.slice(M.start-2,M.start)!="]("&&(M.start==0||/\W/.test(M.string.charAt(M.start-1)))){L.combineTokens=true;return"link"}M.next();return null},blankLine:F};var D={underscoresBreakWords:false,taskLists:true,fencedCodeBlocks:"```",strikethrough:true};for(var I in H){D[I]=H[I]}D.name="markdown";return B.overlayMode(B.getMode(C,D),E)},"markdown");B.defineMIME("text/x-gfm","gfm")});