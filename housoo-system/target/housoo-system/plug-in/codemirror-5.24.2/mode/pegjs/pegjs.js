(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"),require("../javascript/javascript"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror","../javascript/javascript"],A)}else{A(CodeMirror)}}})(function(A){A.defineMode("pegjs",function(C){var D=A.getMode(C,"javascript");function B(E){return E.match(/^[a-zA-Z_][a-zA-Z0-9_]*/)}return{startState:function(){return{inString:false,stringType:null,inComment:false,inCharacterClass:false,braced:0,lhs:true,localState:null}},token:function(I,H){if(I){if(!H.inString&&!H.inComment&&((I.peek()=='"')||(I.peek()=="'"))){H.stringType=I.peek();I.next();H.inString=true}}if(!H.inString&&!H.inComment&&I.match(/^\/\*/)){H.inComment=true}if(H.inString){while(H.inString&&!I.eol()){if(I.peek()===H.stringType){I.next();H.inString=false}else{if(I.peek()==="\\"){I.next();I.next()}else{I.match(/^.[^\\\"\']*/)}}}return H.lhs?"property string":"string"}else{if(H.inComment){while(H.inComment&&!I.eol()){if(I.match(/\*\//)){H.inComment=false}else{I.match(/^.[^\*]*/)}}return"comment"}else{if(H.inCharacterClass){while(H.inCharacterClass&&!I.eol()){if(!(I.match(/^[^\]\\]+/)||I.match(/^\\./))){H.inCharacterClass=false}}}else{if(I.peek()==="["){I.next();H.inCharacterClass=true;return"bracket"}else{if(I.match(/^\/\//)){I.skipToEnd();return"comment"}else{if(H.braced||I.peek()==="{"){if(H.localState===null){H.localState=A.startState(D)}var E=D.token(I,H.localState);var G=I.current();if(!E){for(var F=0;F<G.length;F++){if(G[F]==="{"){H.braced++}else{if(G[F]==="}"){H.braced--}}}}return E}else{if(B(I)){if(I.peek()===":"){return"variable"}return"variable-2"}else{if(["[","]","(",")"].indexOf(I.peek())!=-1){I.next();return"bracket"}else{if(!I.eatSpace()){I.next()}}}}}}}}}return null}}},"javascript")});