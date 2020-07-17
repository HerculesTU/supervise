(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror"],A)}else{A(CodeMirror)}}})(function(A){A.defineMode("vb",function(Y,Q){var e="error";function O(h){return new RegExp("^(("+h.join(")|(")+"))\\b","i")}var a=new RegExp("^[\\+\\-\\*/%&\\\\|\\^~<>!]");var G=new RegExp("^[\\(\\)\\[\\]\\{\\}@,:`=;\\.]");var D=new RegExp("^((==)|(<>)|(<=)|(>=)|(<>)|(<<)|(>>)|(//)|(\\*\\*))");var B=new RegExp("^((\\+=)|(\\-=)|(\\*=)|(%=)|(/=)|(&=)|(\\|=)|(\\^=))");var S=new RegExp("^((//=)|(>>=)|(<<=)|(\\*\\*=))");var b=new RegExp("^[_A-Za-z][_A-Za-z0-9]*");var X=["class","module","sub","enum","select","while","if","function","get","set","property","try"];var C=["else","elseif","case","catch"];var L=["next","loop"];var d=["and","or","not","xor","in"];var F=O(d);var R=["as","dim","break","continue","optional","then","until","goto","byval","byref","new","handles","property","return","const","private","protected","friend","public","shared","static","true","false"];var M=["integer","string","double","decimal","boolean","short","char","float","single"];var Z=O(R);var J=O(M);var N='"';var P=O(X);var T=O(C);var I=O(L);var c=O(["end"]);var V=O(["do"]);var f=null;A.registerHelper("hintWords","vb",X.concat(C).concat(L).concat(d).concat(R).concat(M));function W(h,i){i.currentIndent++}function g(h,i){i.currentIndent--}function K(l,k){if(l.eatSpace()){return null}var j=l.peek();if(j==="'"){l.skipToEnd();return"comment"}if(l.match(/^((&H)|(&O))?[0-9\.a-f]/i,false)){var h=false;if(l.match(/^\d*\.\d+F?/i)){h=true}else{if(l.match(/^\d+\.\d*F?/)){h=true}else{if(l.match(/^\.\d+F?/)){h=true}}}if(h){l.eat(/J/i);return"number"}var i=false;if(l.match(/^&H[0-9a-f]+/i)){i=true}else{if(l.match(/^&O[0-7]+/i)){i=true}else{if(l.match(/^[1-9]\d*F?/)){l.eat(/J/i);i=true}else{if(l.match(/^0(?![\dx])/i)){i=true}}}}if(i){l.eat(/L/i);return"number"}}if(l.match(N)){k.tokenize=E(l.current());return k.tokenize(l,k)}if(l.match(S)||l.match(B)){return null}if(l.match(D)||l.match(a)||l.match(F)){return"operator"}if(l.match(G)){return null}if(l.match(V)){W(l,k);k.doInCurrentLine=true;return"keyword"}if(l.match(P)){if(!k.doInCurrentLine){W(l,k)}else{k.doInCurrentLine=false}return"keyword"}if(l.match(T)){return"keyword"}if(l.match(c)){g(l,k);g(l,k);return"keyword"}if(l.match(I)){g(l,k);return"keyword"}if(l.match(J)){return"keyword"}if(l.match(Z)){return"keyword"}if(l.match(b)){return"variable"}l.next();return e}function E(j){var h=j.length==1;var i="string";return function(l,k){while(!l.eol()){l.eatWhile(/[^'"]/);if(l.match(j)){k.tokenize=K;return i}else{l.eat(/['"]/)}}if(h){if(Q.singleLineStringErrors){return e}else{k.tokenize=K}}return i}}function U(l,j){var h=j.tokenize(l,j);var i=l.current();if(i==="."){h=j.tokenize(l,j);i=l.current();if(h==="variable"){return"variable"}else{return e}}var k="[({".indexOf(i);if(k!==-1){W(l,j)}if(f==="dedent"){if(g(l,j)){return e}}k="])}".indexOf(i);if(k!==-1){if(g(l,j)){return e}}return h}var H={electricChars:"dDpPtTfFeE ",startState:function(){return{tokenize:K,lastToken:null,currentIndent:0,nextLineIndent:0,doInCurrentLine:false}},token:function(j,i){if(j.sol()){i.currentIndent+=i.nextLineIndent;i.nextLineIndent=0;i.doInCurrentLine=0}var h=U(j,i);i.lastToken={style:h,content:j.current()};return h},indent:function(j,h){var i=h.replace(/^\s+|\s+$/g,"");if(i.match(I)||i.match(c)||i.match(T)){return Y.indentUnit*(j.currentIndent-1)}if(j.currentIndent<0){return 0}return j.currentIndent*Y.indentUnit},lineComment:"'"};return H});A.defineMIME("text/x-vb","vb")});