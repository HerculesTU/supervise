(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror"],A)}else{A(CodeMirror)}}})(function(A){A.defineMode("pascal",function(){function B(L){var K={},I=L.split(" ");for(var J=0;J<I.length;++J){K[I[J]]=true}return K}var G=B("and array begin case const div do downto else end file for forward integer boolean char function goto if in label mod nil not of or packed procedure program record repeat set string then to type until var while with");var D={"null":true};var C=/[+\-*&%=<>!?|\/]/;function F(L,K){var J=L.next();if(J=="#"&&K.startOfLine){L.skipToEnd();return"meta"}if(J=='"'||J=="'"){K.tokenize=E(J);return K.tokenize(L,K)}if(J=="("&&L.eat("*")){K.tokenize=H;return H(L,K)}if(/[\[\]{}\(\),;\:\.]/.test(J)){return null}if(/\d/.test(J)){L.eatWhile(/[\w\.]/);return"number"}if(J=="/"){if(L.eat("/")){L.skipToEnd();return"comment"}}if(C.test(J)){L.eatWhile(C);return"operator"}L.eatWhile(/[\w\$_]/);var I=L.current();if(G.propertyIsEnumerable(I)){return"keyword"}if(D.propertyIsEnumerable(I)){return"atom"}return"variable"}function E(I){return function(N,L){var K=false,M,J=false;while((M=N.next())!=null){if(M==I&&!K){J=true;break}K=!K&&M=="\\"}if(J||!K){L.tokenize=null}return"string"}}function H(L,J){var K=false,I;while(I=L.next()){if(I==")"&&K){J.tokenize=null;break}K=(I=="*")}return"comment"}return{startState:function(){return{tokenize:null}},token:function(K,J){if(K.eatSpace()){return null}var I=(J.tokenize||F)(K,J);if(I=="comment"||I=="meta"){return I}return I},electricChars:"{}"}});A.defineMIME("text/x-pascal","pascal")});