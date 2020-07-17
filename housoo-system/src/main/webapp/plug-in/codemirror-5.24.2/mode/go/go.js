(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror"],A)}else{A(CodeMirror)}}})(function(A){A.defineMode("go",function(D){var C=D.indentUnit;var G={"break":true,"case":true,"chan":true,"const":true,"continue":true,"default":true,"defer":true,"else":true,"fallthrough":true,"for":true,"func":true,"go":true,"goto":true,"if":true,"import":true,"interface":true,"map":true,"package":true,"range":true,"return":true,"select":true,"struct":true,"switch":true,"type":true,"var":true,"bool":true,"byte":true,"complex64":true,"complex128":true,"float32":true,"float64":true,"int8":true,"int16":true,"int32":true,"int64":true,"string":true,"uint8":true,"uint16":true,"uint32":true,"uint64":true,"int":true,"uint":true,"uintptr":true,"error":true,"rune":true};var L={"true":true,"false":true,"iota":true,"nil":true,"append":true,"cap":true,"close":true,"complex":true,"copy":true,"delete":true,"imag":true,"len":true,"make":true,"new":true,"panic":true,"print":true,"println":true,"real":true,"recover":true};var M=/[+\-*&^%:=<>!|\/]/;var B;function H(Q,P){var O=Q.next();if(O=='"'||O=="'"||O=="`"){P.tokenize=K(O);return P.tokenize(Q,P)}if(/[\d\.]/.test(O)){if(O=="."){Q.match(/^[0-9]+([eE][\-+]?[0-9]+)?/)}else{if(O=="0"){Q.match(/^[xX][0-9a-fA-F]+/)||Q.match(/^0[0-7]+/)}else{Q.match(/^[0-9]*\.?[0-9]*([eE][\-+]?[0-9]+)?/)}}return"number"}if(/[\[\]{}\(\),;\:\.]/.test(O)){B=O;return null}if(O=="/"){if(Q.eat("*")){P.tokenize=E;return E(Q,P)}if(Q.eat("/")){Q.skipToEnd();return"comment"}}if(M.test(O)){Q.eatWhile(M);return"operator"}Q.eatWhile(/[\w\$_\xa1-\uffff]/);var N=Q.current();if(G.propertyIsEnumerable(N)){if(N=="case"||N=="default"){B="case"}return"keyword"}if(L.propertyIsEnumerable(N)){return"atom"}return"variable"}function K(N){return function(S,Q){var P=false,R,O=false;while((R=S.next())!=null){if(R==N&&!P){O=true;break}P=!P&&N!="`"&&R=="\\"}if(O||!(P||N=="`")){Q.tokenize=H}return"string"}}function E(Q,O){var P=false,N;while(N=Q.next()){if(N=="/"&&P){O.tokenize=H;break}P=(N=="*")}return"comment"}function F(R,Q,N,P,O){this.indented=R;this.column=Q;this.type=N;this.align=P;this.prev=O}function J(P,O,N){return P.context=new F(P.indented,O,N,null,P.context)}function I(O){if(!O.context.prev){return}var N=O.context.type;if(N==")"||N=="]"||N=="}"){O.indented=O.context.indented}return O.context=O.context.prev}return{startState:function(N){return{tokenize:null,context:new F((N||0)-C,0,"top",false),indented:0,startOfLine:true}},token:function(Q,O){var P=O.context;if(Q.sol()){if(P.align==null){P.align=false}O.indented=Q.indentation();O.startOfLine=true;if(P.type=="case"){P.type="}"}}if(Q.eatSpace()){return null}B=null;var N=(O.tokenize||H)(Q,O);if(N=="comment"){return N}if(P.align==null){P.align=true}if(B=="{"){J(O,Q.column(),"}")}else{if(B=="["){J(O,Q.column(),"]")}else{if(B=="("){J(O,Q.column(),")")}else{if(B=="case"){P.type="case"}else{if(B=="}"&&P.type=="}"){P=I(O)}else{if(B==P.type){I(O)}}}}}}O.startOfLine=false;return N},indent:function(R,O){if(R.tokenize!=H&&R.tokenize!=null){return 0}var Q=R.context,P=O&&O.charAt(0);if(Q.type=="case"&&/^(?:case|default)\b/.test(O)){R.context.type="}";return Q.indented}var N=P==Q.type;if(Q.align){return Q.column+(N?0:1)}else{return Q.indented+(N?0:C)}},electricChars:"{}):",closeBrackets:"()[]{}''\"\"``",fold:"brace",blockCommentStart:"/*",blockCommentEnd:"*/",lineComment:"//"}});A.defineMIME("text/x-go","go")});