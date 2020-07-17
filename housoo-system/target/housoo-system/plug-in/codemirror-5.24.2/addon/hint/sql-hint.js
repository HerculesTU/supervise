(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"),require("../../mode/sql/sql"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror","../../mode/sql/sql"],A)}else{A(CodeMirror)}}})(function(S){var O;var F;var A;var T={QUERY_DIV:";",ALIAS_KEYWORD:"AS"};var I=S.Pos,J=S.cmpPos;function M(V){return Object.prototype.toString.call(V)=="[object Array]"}function E(W){var V=W.doc.modeOption;if(V==="sql"){V="text/x-sql"}return S.resolveMode(V).keywords}function B(V){return typeof V=="string"?V:V.text}function H(V,W){if(M(W)){W={columns:W}}if(!W.text){W.text=V}return W}function R(Y){var W={};if(M(Y)){for(var V=Y.length-1;V>=0;V--){var X=Y[V];W[B(X).toUpperCase()]=H(B(X),X)}}else{if(Y){for(var Z in Y){W[Z.toUpperCase()]=H(Z,Y[Z])}}}return W}function U(V){return O[V.toUpperCase()]}function G(X){var W={};for(var V in X){if(X.hasOwnProperty(V)){W[V]=X[V]}}return W}function C(W,Y){var X=W.length;var V=B(Y).substr(0,X);return W.toUpperCase()===V.toUpperCase()}function L(Y,X,Z,V){if(M(Z)){for(var W=0;W<Z.length;W++){if(C(X,Z[W])){Y.push(V(Z[W]))}}}else{for(var a in Z){if(Z.hasOwnProperty(a)){var b=Z[a];if(!b||b===true){b=a}else{b=b.displayText?{text:b.text,displayText:b.displayText}:b.text}if(C(X,b)){Y.push(V(b))}}}}}function K(V){if(V.charAt(0)=="."){V=V.substr(1)}return V.replace(/`/g,"")}function D(X){var W=B(X).split(".");for(var V=0;V<W.length;V++){W[V]="`"+W[V]+"`"}var Y=W.join(".");if(typeof X=="string"){return Y}X=G(X);X.text=Y;return X}function Q(V,i,a,W){var Y=false;var d=[];var X=i.start;var c=true;while(c){c=(i.string.charAt(0)==".");Y=Y||(i.string.charAt(0)=="`");X=i.start;d.unshift(K(i.string));i=W.getTokenAt(I(V.line,i.start));if(i.string=="."){c=true;i=W.getTokenAt(I(V.line,i.start))}}var Z=d.join(".");L(a,Z,O,function(j){return Y?D(j):j});L(a,Z,F,function(j){return Y?D(j):j});Z=d.pop();var e=d.join(".");var h=false;var g=e;if(!U(e)){var f=e;e=N(e,W);if(e!==f){h=true}}var b=U(e);if(b&&b.columns){b=b.columns}if(b){L(a,Z,b,function(j){var k=e;if(h==true){k=g}if(typeof j=="string"){j=k+"."+j}else{j=G(j);j.text=k+"."+j.text}return Y?D(j):j})}return X}function P(Y,Z){if(!Y){return}var W=/[,;]/g;var V=Y.split(" ");for(var X=0;X<V.length;X++){Z(V[X]?V[X].replace(W,""):"")}}function N(k,W){var f=W.doc;var e=f.getValue();var V=k.toUpperCase();var X="";var Z="";var b=[];var h={start:I(0,0),end:I(W.lastLine(),W.getLineHandle(W.lastLine()).length)};var g=e.indexOf(T.QUERY_DIV);while(g!=-1){b.push(f.posFromIndex(g));g=e.indexOf(T.QUERY_DIV,g+1)}b.unshift(I(0,0));b.push(I(W.lastLine(),W.getLineHandle(W.lastLine()).text.length));var j=null;var Y=W.getCursor();for(var d=0;d<b.length;d++){if((j==null||J(Y,j)>0)&&J(Y,b[d])<=0){h={start:j,end:b[d]};break}j=b[d]}var c=f.getRange(h.start,h.end,false);for(var d=0;d<c.length;d++){var a=c[d];P(a,function(l){var i=l.toUpperCase();if(i===V&&U(X)){Z=X}if(i!==T.ALIAS_KEYWORD){X=l}});if(Z){break}}return Z}S.registerHelper("hint","sql",function(W,b){O=R(b&&b.tables);var a=b&&b.defaultTable;var e=b&&b.disableKeywords;F=a&&U(a);A=E(W);if(a&&!F){F=N(a,W)}F=F||[];if(F.columns){F=F.columns}var V=W.getCursor();var Y=[];var Z=W.getTokenAt(V),X,c,d;if(Z.end>V.ch){Z.end=V.ch;Z.string=Z.string.slice(0,V.ch-Z.start)}if(Z.string.match(/^[.`\w@]\w*$/)){d=Z.string;X=Z.start;c=Z.end}else{X=c=V.ch;d=""}if(d.charAt(0)=="."||d.charAt(0)=="`"){X=Q(V,Z,Y,W)}else{L(Y,d,O,function(f){return f});L(Y,d,F,function(f){return f});if(!e){L(Y,d,A,function(f){return f.toUpperCase()})}}return{list:Y,from:I(V.line,X),to:I(V.line,c)}})});