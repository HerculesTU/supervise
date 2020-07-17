(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror"],A)}else{A(CodeMirror)}}})(function(A){A.defineMode("puppet",function(){var B={};var C=/({)?([a-z][a-z0-9_]*)?((::[a-z][a-z0-9_]*)*::)?[a-zA-Z0-9_]+(})?/;function D(H,I){var J=I.split(" ");for(var G=0;G<J.length;G++){B[J[G]]=H}}D("keyword","class define site node include import inherits");D("keyword","case if else in and elsif default or");D("atom","false true running present absent file directory undef");D("builtin","action augeas burst chain computer cron destination dport exec file filebucket group host icmp iniface interface jump k5login limit log_level log_prefix macauthorization mailalias maillist mcx mount nagios_command nagios_contact nagios_contactgroup nagios_host nagios_hostdependency nagios_hostescalation nagios_hostextinfo nagios_hostgroup nagios_service nagios_servicedependency nagios_serviceescalation nagios_serviceextinfo nagios_servicegroup nagios_timeperiod name notify outiface package proto reject resources router schedule scheduled_task selboolean selmodule service source sport ssh_authorized_key sshkey stage state table tidy todest toports tosource user vlan yumrepo zfs zone zpool");function E(K,I){var H,J,G=false;while(!K.eol()&&(H=K.next())!=I.pending){if(H==="$"&&J!="\\"&&I.pending=='"'){G=true;break}J=H}if(G){K.backUp(1)}if(H==I.pending){I.continueString=false}else{I.continueString=true}return"string"}function F(M,K){var L=M.match(/[\w]+/,false);var H=M.match(/(\s+)?\w+\s+=>.*/,false);var J=M.match(/(\s+)?[\w:_]+(\s+)?{/,false);var I=M.match(/(\s+)?[@]{1,2}[\w:_]+(\s+)?{/,false);var G=M.next();if(G==="$"){if(M.match(C)){return K.continueString?"variable-2":"variable"}return"error"}if(K.continueString){M.backUp(1);return E(M,K)}if(K.inDefinition){if(M.match(/(\s+)?[\w:_]+(\s+)?/)){return"def"}M.match(/\s+{/);K.inDefinition=false}if(K.inInclude){M.match(/(\s+)?\S+(\s+)?/);K.inInclude=false;return"def"}if(M.match(/(\s+)?\w+\(/)){M.backUp(1);return"def"}if(H){M.match(/(\s+)?\w+/);return"tag"}if(L&&B.hasOwnProperty(L)){M.backUp(1);M.match(/[\w]+/);if(M.match(/\s+\S+\s+{/,false)){K.inDefinition=true}if(L=="include"){K.inInclude=true}return B[L]}if(/(^|\s+)[A-Z][\w:_]+/.test(L)){M.backUp(1);M.match(/(^|\s+)[A-Z][\w:_]+/);return"def"}if(J){M.match(/(\s+)?[\w:_]+/);return"def"}if(I){M.match(/(\s+)?[@]{1,2}/);return"special"}if(G=="#"){M.skipToEnd();return"comment"}if(G=="'"||G=='"'){K.pending=G;return E(M,K)}if(G=="{"||G=="}"){return"bracket"}if(G=="/"){M.match(/.*?\//);return"variable-3"}if(G.match(/[0-9]/)){M.eatWhile(/[0-9]+/);return"number"}if(G=="="){if(M.peek()==">"){M.next()}return"operator"}M.eatWhile(/[\w-]/);return null}return{startState:function(){var G={};G.inDefinition=false;G.inInclude=false;G.continueString=false;G.pending=false;return G},token:function(H,G){if(H.eatSpace()){return null}return F(H,G)}}});A.defineMIME("text/x-puppet","puppet")});